package br.com.codiub.heimdall.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.response.Response;
import br.com.codiub.heimdall.service.CreateFolderService;

@RestController
@RequestMapping("/createFolder")
public class CreateFolderController {
	
	@Autowired
	private CreateFolderService createFolderService;
	
	@PostMapping("/create")
	public  ResponseEntity<Response<Object>> createFolder(@Valid @RequestParam("folder") String folder) {
		ResponseEntity<Response<Object>> response = createFolderService.createFolder(folder);
		return response;
	}


}
