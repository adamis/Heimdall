package br.com.codiub.heimdall.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadFileResponse {
	
	    private String fileName;
	    private String folderName;
	    private String fileDownloadUri;
	    private String fileType;
	    private Date dataImportacao;
	    private long size;
	    
}
