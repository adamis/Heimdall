package br.com.codiub.heimdall.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Utils {

	public static String cleanFolderUrl(String folder) {	
		if((folder.length() > 0 && folder.startsWith("/")) || (folder.length() > 0 && folder.startsWith("\\"))) {
			folder = folder.substring(1, folder.length());
		}		
		if((folder.length() > 0 && folder.endsWith("/")) || (folder.length() > 0 &&folder.endsWith("\\"))) {
			folder = folder.substring(0,folder.length()-1);
		}		
		return folder;
	}
	
	public static HttpHeaders getHeader(String file, boolean download) {
		
		HttpHeaders headers = new HttpHeaders();
		if(download) {
			headers.add("Content-Disposition", "attachment; filename=\"" + file + "\"");			
			headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=utf-8");
			
		}else if(file.contains(".pdf")) {
			headers.add("Content-Disposition", " filename=\"" + file + "\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

		}else if(file.contains(".png")) {
			headers.add("Content-Disposition", " filename=\"" + file + "\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
			
		}else if(file.contains(".jpg") || file.contains(".jpeg")) {
			headers.add("Content-Disposition", " filename=\"" + file + "\"");								
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
			
		}else {
			headers.add("Content-Disposition", "attachment; filename=\"" + file + "\"");
			headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=utf-8");
			
		}
		
		return headers;
	}

}
