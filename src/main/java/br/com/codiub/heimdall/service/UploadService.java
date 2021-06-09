package br.com.codiub.heimdall.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPCreateFolder;
import br.com.codiub.heimdall.ftp.FTPUpload;
import br.com.codiub.heimdall.ftp.FTPUtil;
import br.com.codiub.heimdall.response.Response;
import br.com.codiub.heimdall.response.UploadFileResponse;
import br.com.codiub.heimdall.utils.NTP;
import br.com.codiub.heimdall.utils.Utils;

@Service
public class UploadService {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	
	public ResponseEntity<Response<UploadFileResponse>> uploadFile(@Valid String folder, @Valid MultipartFile file,
			Boolean trueName) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		ResponseEntity<Response<UploadFileResponse>> response = null;
		Response<UploadFileResponse> result = new Response<>();

		File fileTemp = null;	        
		try {
			
			if(trueName == null) {
				trueName = false;
			}

			//NORMALIZANDO A URL DA PASTA REMOVEND / e \
			folder = Utils.cleanFolderUrl(folder);
			
			//folder = FTPUtil.ROOT_DIR+"/"+folder;			
			FTPUpload ftpUpload = new FTPUpload(ftpConection);
			FTPCreateFolder ftpCreateFolder = new FTPCreateFolder(ftpConection);


			if(trueName) {
				fileTemp = new File(Files.createTempDir(), file.getOriginalFilename());
			}else {
				fileTemp = new File(Files.createTempDir(), simpleDateFormat.format(NTP.getDateTime())+"."+FilenameUtils.getExtension(file.getOriginalFilename()));	
			}
			
			
			fileTemp.deleteOnExit();				
			FileOutputStream fos = new FileOutputStream(fileTemp); 
			fos.write(file.getBytes());
			fos.close();


			if(ftpCreateFolder.checkDirectoryExists(folder)) {
				ftpUpload.upload(fileTemp, folder);
				UploadFileResponse uploadFileResponse = new UploadFileResponse();
				uploadFileResponse.setFileName(fileTemp.getName());
				uploadFileResponse.setFolderName(folder+"/");
				uploadFileResponse.setFileDownloadUri(folder+"/"+fileTemp.getName()); 
				uploadFileResponse.setFileType(file.getContentType());
				uploadFileResponse.setDataImportacao(NTP.getDateTime());
				uploadFileResponse.setSize(file.getSize());
				
				result.setResult(uploadFileResponse);
				result.setCode(200);
				
				response = ResponseEntity.status(HttpStatus.OK).body(result);
				
			}else {
				result.setMessage("Diretorio não encontrado!");
				result.setCode(400);
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("Erro Interno: "+e.getMessage());
			result.setCode(500);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
			
		}finally {
			if(fileTemp != null) {
				fileTemp.delete();
			}
			try {					
				ftpConection.closeFTP();
			} catch (Exception e) {
				result.setMessage("Erro Interno: "+e.getMessage());
				result.setCode(500);
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
				e.printStackTrace();
			}
		}

		
		return response;
	}

	public ResponseEntity<Response<UploadFileResponse>> uploadFileReplace(String folder, String fileforReplace,
			MultipartFile file) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		UploadFileResponse uploadFileResponse = null;
		ResponseEntity<Response<UploadFileResponse>> response = null;
		Response<UploadFileResponse> result = new Response<>();
		
		File fileTemp = null;

		try {
			//NORMALIZANDO A URL DA PASTA REMOVEND / e \
			folder = Utils.cleanFolderUrl(folder);
			folder = FTPUtil.ROOT_DIR+folder;
			
			FTPUpload ftpUpload = new FTPUpload(ftpConection);
			FTPCreateFolder ftpCreateFolder = new FTPCreateFolder(ftpConection);

			fileTemp = new File(Files.createTempDir(), fileforReplace);
			//fileTemp.deleteOnExit();				
			FileOutputStream fos = new FileOutputStream(fileTemp); 
			fos.write(file.getBytes());
			fos.close();


			if(ftpCreateFolder.checkDirectoryExists(folder)) {

				if(ftpCreateFolder.checkFileExists(folder+"/"+fileforReplace)) {

					ftpUpload.renameFile(folder, fileforReplace, "temp_"+fileforReplace);

					if(ftpUpload.upload(fileTemp, folder)) {
						//ftpUpload.remove(folder, "temp_"+fileforReplace);	
					}else {
						ftpUpload.renameFile(folder, "temp_"+fileforReplace,fileforReplace);	
					}

					uploadFileResponse = new UploadFileResponse();
					uploadFileResponse.setFileName(fileTemp.getName()); 
					uploadFileResponse.setFolderName(folder+"/");
					uploadFileResponse.setFileDownloadUri(folder+"/"+fileTemp.getName()); 
					uploadFileResponse.setFileType(file.getContentType());
					uploadFileResponse.setDataImportacao(NTP.getDateTime());
					uploadFileResponse.setSize(file.getSize());
					
					result.setResult(uploadFileResponse);
					result.setCode(200);
					response = ResponseEntity.status(HttpStatus.OK).body(result);
				}else {			    		
					result.setMessage("Arquivo não encontrado!");
					result.setCode(400);
					response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
				}

			}else {			    	
				result.setMessage("Diretorio não encontrado!");
				result.setCode(400);
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("Erro Interno: "+e.getMessage());
			result.setCode(500);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}finally {
			if(fileTemp != null) {
				fileTemp.delete();
			}
			try {					
				ftpConection.closeFTP();
			} catch (Exception e) {
				e.printStackTrace();
				
				result.setMessage("Erro Interno: "+e.getMessage());
				result.setCode(500);
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
			}
		}
		
		return response;
	}

	
}
