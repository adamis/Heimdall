package br.com.codiub.heimdall.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.ftp.FTPConectionPMU;
import br.com.codiub.heimdall.ftp.FTPDownload;
import br.com.codiub.heimdall.utils.ResizeImages;
import br.com.codiub.heimdall.utils.Utils;

@RestController
@RequestMapping("/download")
public class DownloadController {
	

	@GetMapping("/file")
	public ResponseEntity<Object> downloadFile(@RequestParam String folder,@RequestParam String file) {
		ResponseEntity<Object> response = null;

		File fileDownload = null;

		try {

			folder = Utils.cleanFolderUrl(folder);
			
			FTPConectionPMU ftpConection = new FTPConectionPMU();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "attachment; filename=\"" + file + "\"");
				headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=utf-8");

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
	
	@GetMapping("/file/other-name")
	public ResponseEntity<Object> downloadFileOtherName(@RequestParam String folder,@RequestParam String file, @RequestParam String newName) {
		ResponseEntity<Object> response = null;

		File fileDownload = null;

		try {

			folder = Utils.cleanFolderUrl(folder);
			
			FTPConectionPMU ftpConection = new FTPConectionPMU();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {
				HttpHeaders headers = new HttpHeaders();
				
				

				if(file.contains(".pdf")) {
					headers.add("Content-Disposition", " filename=\"" + newName + "\"");
					headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

				}else if(file.contains(".png")) {
					headers.add("Content-Disposition", " filename=\"" + newName + "\"");
					headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
					
				}else if(file.contains(".jpg") || file.contains(".jpeg")) {
					headers.add("Content-Disposition", " filename=\"" + newName + "\"");
					headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");			
					
				}else {
					headers.add("Content-Disposition", "attachment; filename=\"" + newName + "\"");
					headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=utf-8");
					
				}

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

	
	/**
	 * download de Imagens
	 * @param folder
	 * @param file
	 * @param width
	 * @param height
	 * @param percent
	 * @param rotate
	 * @param qualit
	 * @param download
	 * @return
	 */
	@GetMapping("/imagens")
	public ResponseEntity<Object> downloadImage(
			@RequestParam String folder,
			@RequestParam String file,
			Integer width,
			Integer height,
			Integer percent,
			Integer rotate,
			Integer qualit,
			@RequestParam boolean download
			) {
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

			FTPConectionPMU ftpConection = new FTPConectionPMU();
			FTPDownload ftpDownload;

			ftpDownload = new FTPDownload(ftpConection);

			File fileDownload = ftpDownload.download(folder, file);

			if(fileDownload !=null) {
				HttpHeaders headers = new HttpHeaders();
				if(download) {
					headers.add("Content-Disposition", "attachment; filename=\"" + file + "\"");
				}
				headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
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
