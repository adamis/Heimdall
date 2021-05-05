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
import br.com.codiub.heimdall.ftp.FTPDelete;
import br.com.codiub.heimdall.response.Response;

@RestController
@RequestMapping("/delete")
public class DeleteController {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

	@PostMapping("/folder")
	public  ResponseEntity<Response<Object>> deleteFolder(@Valid @RequestParam("folder") String folder) {


		FTPConectionPMU ftpConection = new FTPConectionPMU();
		Response<Object> result = new Response<>();
		ResponseEntity<Response<Object>> response = null;
		
		try {
			
			FTPDelete ftpDeleteFolder = new FTPDelete(ftpConection);
			
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
	
	@PostMapping("/file")
	public  ResponseEntity<Response<Object>> deleteFile(@Valid @RequestParam("folder") String folder,@Valid @RequestParam("file") String file) {


		FTPConectionPMU ftpConection = new FTPConectionPMU();
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
