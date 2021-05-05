package br.com.codiub.heimdall.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUpload {

	FTPClient ftp;
	FTPConection ftpConection;
	
	public FTPUpload(FTPConection ftpConection) throws Exception {		
		this.ftpConection = ftpConection;
		conect();
	}

	private void conect() throws Exception {
		this.ftp = ftpConection.conectWrite();
		ftp.setFileType(FTP.BINARY_FILE_TYPE);	           
	}

	public void setWorkbase(String workFolder) throws IOException {
		ftp.changeWorkingDirectory(FTPUtil.ROOT_DIR+workFolder);
	}
	
	private void disconect() throws Exception {
		ftpConection.closeFTP();
	}

	public boolean upload(File file,String folder) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
	
		folder = FTPUtil.ROOT_DIR+folder;
		InputStream inputStream = new FileInputStream(file);

	
		
		OutputStream outputStream = ftp.storeFileStream(folder+"/"+file.getName());
		byte[] bytesIn = new byte[4096];
		int read = 0;

		while ((read = inputStream.read(bytesIn)) != -1) {
			outputStream.write(bytesIn, 0, read);
		}
		inputStream.close();
		outputStream.close();

		boolean completed = ftp.completePendingCommand();
		
		disconect();
		return completed;
	}
	
	public boolean renameFile(String folder,String oldFile,String newFile) throws Exception {
		
		if(!ftp.isConnected()) {
			conect();
		}
		
		folder = FTPUtil.ROOT_DIR+folder;
		
		
		
		boolean success = ftp.rename(folder+"/"+oldFile, folder+"/"+newFile);
        
		disconect();
		
		return success;
	}
	
	public boolean renameFolder(String oldFolder,String newFolder) throws Exception {
		if(!ftp.isConnected()) {
			conect();
		}
		
		boolean success = ftp.rename(FTPUtil.ROOT_DIR+oldFolder, FTPUtil.ROOT_DIR+newFolder);

		disconect();
		return success;
	}
	
	public boolean remove(String folder,String file) throws Exception {
		if(!ftp.isConnected()) {
			conect();
		}
		
		boolean deleteFile = ftp.deleteFile(folder+"/"+file);

		disconect();
		
		return deleteFile;
	}

}
