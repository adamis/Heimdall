package br.com.codiub.heimdall.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.google.common.io.Files;

public class FTPDownload {
	FTPClient ftp;
	FTPConection ftpConection;
	
	public FTPDownload(FTPConection ftpConection) throws Exception {		
		this.ftpConection = ftpConection;
		conect();
	}
	
	private void conect() throws Exception {
		this.ftp = ftpConection.conectRead();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);		
	}
	
	private void disconect() throws Exception {
		ftpConection.closeFTP();
	}
	
	public File download(String folder,String file) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		File fileTemp = new File(Files.createTempDir(), file);
		fileTemp.deleteOnExit();
		
		file = FTPUtil.ROOT_DIR+"/"+folder+"/"+file;	
		
		FileOutputStream fileOutputStream = new FileOutputStream(fileTemp);
		OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
        boolean success = ftp.retrieveFile(file, outputStream);
        outputStream.close();
        
        disconect();
        
        if(success) {
        	return fileTemp;
        } else {
        	return null;
        }
        
	}
	
}
