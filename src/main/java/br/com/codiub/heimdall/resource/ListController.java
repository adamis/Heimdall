package br.com.codiub.heimdall.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.service.ListService;

@RestController
@RequestMapping("/list")
public class ListController {
	
	@Autowired
	private ListService listService;
	
	@GetMapping("/folder")
	public  ResponseEntity<Object> listFolder(@Valid @RequestParam("folder") String folder) {
		ResponseEntity<Object> response = listService.listFolders(folder);
		return response;
	}

	@GetMapping("/files")
	public  ResponseEntity<Object> listFiles(@Valid @RequestParam("folder") String folder) {
		ResponseEntity<Object> response = listService.listFiles(folder);
		return response;
	}

	@GetMapping("/all")
	public  ResponseEntity<Object> listAll(@Valid @RequestParam("folder") String folder) {
		ResponseEntity<Object> response = listService.listAll(folder);
		return response;
	}
	
}
