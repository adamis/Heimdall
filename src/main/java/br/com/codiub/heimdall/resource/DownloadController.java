package br.com.codiub.heimdall.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codiub.heimdall.service.DownloadService;

@RestController
@RequestMapping("/download")
public class DownloadController {
	
	@Autowired
	private DownloadService downloadService;

	@GetMapping("/file")
	public ResponseEntity<Object> downloadFile(@RequestParam String folder,@RequestParam String file) {
		ResponseEntity<Object> response = downloadService.downloadFile(folder, file);
		return response;
	}
	
	@GetMapping("/file/other-name")
	public ResponseEntity<Object> downloadFileOtherName(@RequestParam String folder,@RequestParam String file, @RequestParam String newName) {		
		ResponseEntity<Object> response = downloadService.downloadFileOtherName(folder, file, newName);	
		return response;
	}
	
	@GetMapping("/imagens")
	public ResponseEntity<Object> downloadImage(@RequestParam String folder,@RequestParam String file,@RequestParam boolean download,
			Integer width,Integer height,Integer percent,Integer rotate,Integer qualit) {
		
		ResponseEntity<Object> response = downloadService.downloadImage(folder,file,width,height,percent,rotate,qualit,download);		
		return response;
	}

	

	

}
