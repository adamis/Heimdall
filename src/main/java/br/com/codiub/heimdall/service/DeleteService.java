package br.com.codiub.heimdall.service;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPDelete;
import br.com.codiub.heimdall.response.Response;

@Service
public class DeleteService {

	
	//DELETAR FOLDER
	public ResponseEntity<Response<Object>> deleteFolder(@Valid String folder) {
		FTPConectionONE ftpConection = new FTPConectionONE();
		Response<Object> result = new Response<>();
		ResponseEntity<Response<Object>> response = null;
		
		try {
			
			FTPDelete ftpDeleteFolder = new FTPDelete(ftpConection);
			
			//Checagem de Pasta
			if(!folder.endsWith("/") || !folder.endsWith("\\")) {
				folder = "/"+folder;
			}
			
			String removeDirectory = ftpDeleteFolder.removeDirectory(folder);
			result.setMessage(""+removeDirectory);
			
			if(removeDirectory.contains("Impossivel") || removeDirectory.contains("ERRO") || removeDirectory.contains("Falha")) {
				result.setCode(400);
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}else {
				result.setCode(200);
				response = ResponseEntity.status(HttpStatus.OK).body(result);
			}			
			
		} catch (Exception e) {
			result.setMessage("Erro Interno: "+e.getMessage());
			result.setCode(500);
			e.printStackTrace();
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}finally {			
			try {					
				ftpConection.closeFTP();
			} catch (Exception e) {
				result.setMessage("Erro Interno: "+e.getMessage());
				result.setCode(500);
				e.printStackTrace();
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
			}
		}
		
		return response;
	}

	//DELETAR FILE
	public ResponseEntity<Response<Object>> deleteFile(@Valid String folder, @Valid String file) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		Response<Object> result = new Response<>();
		ResponseEntity<Response<Object>> response = null;
		
		try {
			String path = folder+"/"+file;			
			FTPDelete ftpDelete = new FTPDelete(ftpConection);
			
			Boolean removeFile = ftpDelete.removeFile(path);
			
			if(removeFile) {
				result.setCode(200);
				result.setMessage("Deletado com Sucesso!");
				response = ResponseEntity.status(HttpStatus.OK).body(result);
				
			}else {
				
				result.setCode(400);
				result.setMessage("Erro ao Excluir este arquivo! "+path);
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}			
			
		} catch (Exception e) {
			result.setMessage("Erro Interno: "+e.getMessage());
			result.setCode(500);
			e.printStackTrace();
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}finally {			
			try {					
				ftpConection.closeFTP();
			} catch (Exception e) {
				result.setMessage("Erro Interno: "+e.getMessage());
				result.setCode(500);
				e.printStackTrace();
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
			}
		}

		return response;
	}
	

}
