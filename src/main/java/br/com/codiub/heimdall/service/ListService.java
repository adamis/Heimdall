package br.com.codiub.heimdall.service;

import javax.validation.Valid;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPList;

@Service
public class ListService {

	public ResponseEntity<Object> listFolders(@Valid String folder) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		ResponseEntity<Object> response = null;
		try {
		
			FTPList ftpList = new FTPList(ftpConection);
			ftpList.setWorkbase(folder);
			FTPFile[] folders = ftpList.getFolders();
			
			response = ResponseEntity.ok().body(folders);	
			
		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}
		
		return response;
	}

	public ResponseEntity<Object> listFiles(@Valid String folder) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		ResponseEntity<Object> response = null;
		try {
		
			FTPList ftpList = new FTPList(ftpConection);
			ftpList.setWorkbase(folder);
			FTPFile[] folders = ftpList.getFiles();
			
			response = ResponseEntity.ok().body(folders);	
			
		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}
		
		return response;
	}
	
}
