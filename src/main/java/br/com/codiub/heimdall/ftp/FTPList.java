package br.com.codiub.heimdall.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPList {

	FTPClient ftp;
	FTPConection ftpConection;

	public FTPList(FTPConection ftpConection) throws Exception {		
		this.ftpConection = ftpConection;
		conect();
	}

	private void conect() throws Exception {		
		this.ftp = ftpConection.conectRead();

		int reply = ftp.getReplyCode();		
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}

	}

	private void disconect() throws Exception {
		ftpConection.closeFTP();
	}

	public void setWorkbase(String workFolder) throws Exception {
		if(!ftp.isConnected()) {
			conect();
		}
		ftp.changeWorkingDirectory(FTPUtil.ROOT_DIR+workFolder);

	}

	public FTPFile[] getFiles() throws Exception {

		if(!ftp.isConnected()) {
			conect();
		}

		FTPFile[] ftpFiles = ftp.listFiles();

		disconect();

		return ftpFiles;
	}

	public FTPFile[] getFolders() throws Exception {

		if(!ftp.isConnected()) {
			conect();
		}

		FTPFile[] ftpFiles = ftp.listDirectories();

		disconect();

		return ftpFiles;
	}

	public FTPFile[] getFilesFolders() throws Exception {

		if(!ftp.isConnected()) {
			conect();
		}

		FTPFile[] ftpFiles = ftp.listFiles();

		disconect();

		return ftpFiles;
	}

}
