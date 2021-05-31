package br.com.codiub.heimdall.service;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPCreateFolder;
import br.com.codiub.heimdall.response.Response;

@Service
public class CreateFolderService {

	public ResponseEntity<Response<Object>> createFolder(@Valid String folder) {
		FTPConectionONE ftpConection = new FTPConectionONE();
		Response<Object> result = new Response<>();
		ResponseEntity<Response<Object>> response = null;
		try {
			
			FTPCreateFolder ftpCreateFolder = new FTPCreateFolder(ftpConection);

			if(ftpCreateFolder.checkDirectoryExists(folder)) {				
				result.setMessage("Impossivel criar. Diretorio existente!");
				result.setCode(400);
				
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}else {
				boolean createFolder = ftpCreateFolder.createFolder(folder);
				if(createFolder) {
					result.setMessage("Diretorio criado com sucesso!");
					result.setCode(200);
					response = ResponseEntity.status(HttpStatus.OK).body(result);
				}else {
					result.setMessage("Impossivel criar diretorio!");
					result.setCode(400);	
					response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(result);
				}				
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
