package br.com.codiub.heimdall.ftp;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPCreateFolder {

	FTPClient ftp;
	FTPConection ftpConection;
	
	public FTPCreateFolder(FTPConection ftpConection) throws Exception {
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
	
	public boolean createFolder(String folder) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		folder = FTPUtil.ROOT_DIR+folder;
		boolean control = false;
		if(ftp.cwd(folder)==550){
			if(ftp.makeDirectory(folder)) {
				control = true;
			}else {
				control = false;
			}
		}else if(ftp.cwd(folder)==250){
			control = true;
		}else{
			control = false;
		}
		
		disconect();
		
		return control;
	}
	
	public boolean checkDirectoryExists(String dirPath) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		//conect();
		
		dirPath = FTPUtil.ROOT_DIR+"/"+dirPath;
		
	    ftp.changeWorkingDirectory(dirPath);
	    int returnCode = ftp.getReplyCode();
	    
	    disconect();
	    
	    if (returnCode == 550) {
	        return false;
	    }
	    return true;
	}
	
	public boolean checkFileExists(String filePath) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		filePath = FTPUtil.ROOT_DIR+filePath;
	    InputStream inputStream = ftp.retrieveFileStream(filePath);
	    int returnCode = ftp.getReplyCode();
	    
	    disconect();
	    
	    if (inputStream == null || returnCode == 550) {
	        return false;
	    }
	    
	    return true;
	}
	
	
}
