package br.com.codiub.heimdall.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPDownload;
import br.com.codiub.heimdall.utils.ResizeImages;
import br.com.codiub.heimdall.utils.Utils;

@Service
public class DownloadService {

	//Download de Arquivos
	public ResponseEntity<Object> downloadFile(String folder,String file) {
		File fileDownload = null;
		ResponseEntity<Object> response = null;

		try {

			folder = Utils.cleanFolderUrl(folder);

			FTPConectionONE ftpConection = new FTPConectionONE();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {
				HttpHeaders headers = Utils.getHeader(file, true);

				response = ResponseEntity.ok()
						.headers(headers)
						.body(Files.readAllBytes(fileDownload.toPath()))
						;	

			}else {
				response = ResponseEntity.badRequest().body("Arquivo não Encontrado: "+folder+file);
			}

		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}finally {
			if(fileDownload != null) {
				fileDownload.delete();
			}
		}

		return response;
	}

	//Download de Arquivos com nome especificado pela requisição 
	public ResponseEntity<Object> downloadFileOtherName(String folder, String file, String newName) {
		ResponseEntity<Object> response = null;

		File fileDownload = null;

		try {

			folder = Utils.cleanFolderUrl(folder);

			FTPConectionONE ftpConection = new FTPConectionONE();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {

				HttpHeaders headers = Utils.getHeader(newName, false);

				response = ResponseEntity.ok()
						.headers(headers)
						.body(Files.readAllBytes(fileDownload.toPath()))
						;	

			}else {
				response = ResponseEntity.badRequest().body("Arquivo não Encontrado: "+folder+"/"+newName+" ("+file+")");
			}

		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}finally {
			if(fileDownload != null) {
				fileDownload.delete();
			}
		}

		return response;
	}

	//Download Imagens com/sem Tratamentos
	public ResponseEntity<Object> downloadImage(String folder, String file, Integer width, Integer height,
			Integer percent, Integer rotate, Integer qualit, boolean download) {


		ResponseEntity<Object> response = null;

		Path path = null;

		try {

			if(qualit != null) {
				if(qualit > 100) {
					qualit = 100;
				}
			}

			if(folder.length()>0 && !folder.endsWith("/")) {
				folder = folder +"/";
			}

			FTPConectionONE ftpConection = new FTPConectionONE();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			File fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {
				HttpHeaders headers = Utils.getHeader(file, download);				

				if(width != null
						|| height != null
						|| percent!= null
						|| rotate!= null
						|| qualit!= null) 
				{
					path = replaceFile(folder, fileDownload, width, height, percent, rotate, qualit).toPath();					
				}else{
					path = fileDownload.toPath();	
				}

				response = ResponseEntity.ok()
						.headers(headers)
						.body(Files.readAllBytes(path))
						;	

			}else {
				response = ResponseEntity.badRequest().body("Arquivo não Encontrado: "+folder+file);
			}

		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}finally {
			if(path != null) {
				path.toFile().delete();
			}
		}

		return response;
	}
	
	
	//--------------------PRIVATE------------------------------------
	private File replaceFile(
			String folder,
			File fileDownload,
			Integer width,
			Integer height,
			Integer percent,
			Integer rotate,
			Integer qualit
			) throws IOException {

		ResizeImages resizeImages = new ResizeImages(fileDownload);

		if(percent != null && percent != 0) {
			resizeImages.setScale(percent);
		}
		
		if (rotate != null) {
			resizeImages.setRotate(rotate);	
		}

		if(qualit !=null) {
			resizeImages.setQualit(qualit);
		}

		if(width != null && height != null) {
			resizeImages.setForceScale(width, height);
		}else if((width != null)) {
			resizeImages.setWidth(width);
		}else if((height != null)) {
			resizeImages.setHeight(height);
		}
		
		//resizeImages.setWaterMaker(watermark, positions);

		BufferedImage scalingImagePercent = resizeImages.getBufferImage();		
		ImageIO.write(scalingImagePercent, "png", fileDownload);		
		return fileDownload;

	}
	
}
