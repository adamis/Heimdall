package br.com.codiub.heimdall.resource;

import java.text.SimpleDateFormat;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.ftp.FTPConectionPMU;
import br.com.codiub.heimdall.ftp.FTPCreateFolder;
import br.com.codiub.heimdall.response.Response;

@RestController
@RequestMapping("/createFolder")
public class CreateFolderController {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

	@PostMapping("/create")
	public  ResponseEntity<Response<Object>> createFile(@Valid @RequestParam("folder") String folder) {


		FTPConectionPMU ftpConection = new FTPConectionPMU();
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
