package br.com.codiub.heimdall.service;

import javax.validation.Valid;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.codiub.heimdall.ftp.FTPConectionONE;
import br.com.codiub.heimdall.ftp.FTPList;
import br.com.codiub.heimdall.ftp.FTPUtil;
import br.com.codiub.heimdall.utils.Utils;

@Service
public class ListService {

	public ResponseEntity<Object> listFolders(@Valid String folder) {

		FTPConectionONE ftpConection = new FTPConectionONE();
		ResponseEntity<Object> response = null;
		try {
			folder = Utils.cleanFolderUrl(folder);		
			folder = FTPUtil.ROOT_DIR+folder;

			System.err.println(""+folder);
			FTPList ftpList = new FTPList(ftpConection);
			if(ftpList.checkDirectoryExists(folder)) {
				ftpList.setWorkbase(folder);
				FTPFile[] folders = ftpList.getFolders();

				response = ResponseEntity.ok().body(folders);	
			}else {
				response = ResponseEntity.badRequest().body("Diretório não encontrado!");
			}
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
			folder = Utils.cleanFolderUrl(folder);
			System.err.println(""+folder);
			FTPList ftpList = new FTPList(ftpConection);
			if(ftpList.checkDirectoryExists(folder)) {
				ftpList.setWorkbase(folder);
				FTPFile[] folders = ftpList.getFiles();

				response = ResponseEntity.ok().body(folders);	
			}else {
				response = ResponseEntity.badRequest().body("Diretório não encontrado!");
			}
		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}

		return response;
	}

	public ResponseEntity<Object> listAll(@Valid String folder) {
		FTPConectionONE ftpConection = new FTPConectionONE();
		ResponseEntity<Object> response = null;
		try {
			folder = Utils.cleanFolderUrl(folder);		

			System.err.println(""+folder);
			FTPList ftpList = new FTPList(ftpConection);
			if(ftpList.checkDirectoryExists(folder)) {

				ftpList.setWorkbase(folder);
				FTPFile[] folders = ftpList.getFilesFolders();

				response = ResponseEntity.ok().body(folders);	
			}else {
				response = ResponseEntity.badRequest().body("Diretório não encontrado!");
			}
		} catch (Exception e) {
			response = ResponseEntity.badRequest().body("Erro Interno!");
			e.printStackTrace();
		}

		return response;
	}

}
