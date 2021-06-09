package br.com.codiub.heimdall.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPDelete {

	FTPClient ftp;
	FTPConection ftpConection;

	public FTPDelete(FTPConection ftpConection) throws Exception {		
		this.ftpConection = ftpConection;
		conect();
	}
	
	private void conect() throws Exception {		
		this.ftp = ftpConection.conectWrite();
		int reply = ftp.getReplyCode();
		
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
	}
	
	
	private void disconect() throws Exception {
		ftpConection.closeFTP();
	}
	

	public String removeDirectory(String parentDir) throws Exception {

		if(!ftp.isConnected()) {
			conect();
		}
		
		parentDir = FTPUtil.ROOT_DIR+parentDir;		
		String result = "";
		try {

			if (!FTPUtil.ROOT_DIR.equals(parentDir)) {
				removeDirAndFiles(parentDir, "");

				if(!ftp.isConnected()) {
					conect();
				}
				
				// finally, remove the directory itself
				boolean removed = ftp.removeDirectory(parentDir);
				if (removed) {
					result = "Pasta removida com sucesso! " + parentDir;
				} else {
					result = "Falha ao remover pasta: " + parentDir;
				}
			} else {
				result = "Impossivel deletar esta Pasta!";
			}

		} catch (Exception e) {
			result = "Erro ao remover pasta: " + parentDir+" ERRO: "+e.getMessage();
			e.printStackTrace();
		}
		
		disconect();
		
		return result;
	}

	public Boolean removeFile(String filePath) throws Exception {

		if(!ftp.isConnected()) {
			conect();
		}
		
		boolean deleted = false;
		boolean checkFileExists = checkFileExists(filePath);

		if(checkFileExists) {
			disconect();
			conect();
			
			deleted = ftp.deleteFile(FTPUtil.ROOT_DIR+filePath);

		}	
		
		disconect();

		return deleted;
	}

	private void removeDirAndFiles(String parentDir, String currentDir) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		System.err.println("dirToList: "+dirToList);
		FTPFile[] subFiles = ftp.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					// skip parent directory and the directory itself
					continue;
				}
				String filePath = parentDir + "/" + currentDir + "/" + currentFileName;
				if (currentDir.equals("")) {
					filePath = parentDir + "/" + currentFileName;
				}

				if (aFile.isDirectory()) {
					// remove the sub directory
					removeDirAndFiles(dirToList, currentFileName);					
				} else {
					// delete the file
					boolean deleted = ftp.deleteFile(filePath);
					if (deleted) {
						System.out.println("DELETED the file: " + filePath);						
					} else {
						System.out.println("CANNOT delete the file: " + filePath);
						throw new Exception("Impossivel deletar file:"+filePath);
					}
				}
			}

			// finally, remove the directory itself
			boolean removed = ftp.removeDirectory(dirToList);
			if (removed) {
				System.out.println("REMOVED the directory: " + dirToList);
			} else {
				System.out.println("CANNOT remove the directory: " + dirToList);
			}
		}
		disconect();
	}

	private boolean checkFileExists(String filePath) throws IOException {
		filePath = FTPUtil.ROOT_DIR+filePath;

		InputStream inputStream = ftp.retrieveFileStream(filePath);
		int returnCode = ftp.getReplyCode();	    
		if (inputStream == null || returnCode == 550) {
			return false;
		}
		return true;
	}

}
