package br.com.codiub.heimdall.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.response.Response;
import br.com.codiub.heimdall.service.DeleteService;

@RestController
@RequestMapping("/delete")
public class DeleteController {
	
	@Autowired 
	private DeleteService deleteService;
	
	@PostMapping("/folder")
	public  ResponseEntity<Response<Object>> deleteFolder(@Valid @RequestParam("folder") String folder) {
		ResponseEntity<Response<Object>> response = deleteService.deleteFolder(folder);
		return response;
	}
	
	@PostMapping("/file")
	public  ResponseEntity<Response<Object>> deleteFile(@Valid @RequestParam("folder") String folder,@Valid @RequestParam("file") String file) {
		ResponseEntity<Response<Object>> response = deleteService.deleteFile(folder,file);
		return response;
	}


}
