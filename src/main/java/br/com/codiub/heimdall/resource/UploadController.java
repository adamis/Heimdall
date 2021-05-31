package br.com.codiub.heimdall.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.codiub.heimdall.response.Response;
import br.com.codiub.heimdall.response.UploadFileResponse;
import br.com.codiub.heimdall.service.UploadService;

@RestController
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private UploadService uploadService;
	
	@PostMapping("/file")
	public ResponseEntity<Response<UploadFileResponse>> uploadFile(@Valid @RequestParam("folder") String folder, @Valid @RequestBody MultipartFile file, Boolean trueName) {
		ResponseEntity<Response<UploadFileResponse>> response = uploadService.uploadFile(folder,file, trueName);
		return response;
	}

	@PostMapping("/replace")
	public ResponseEntity<Response<UploadFileResponse>> uploadFileReplace(@RequestParam("folder") String folder, @RequestParam("fileforReplace") String fileforReplace ,
																		  @RequestParam("file") MultipartFile file
	) {		
		ResponseEntity<Response<UploadFileResponse>> response = uploadService.uploadFileReplace(folder,fileforReplace, file);		
		return response;
	}

	

}
