package br.com.codiub.heimdall.ftp;

import org.apache.commons.net.ftp.FTPClient;

public class FTPConectionPMU implements FTPConection{

	FTPClient ftp = new FTPClient();

	@Override
	public FTPClient conectWrite() throws Exception {
				
		
		if(!ftp.isConnected()) {
			
			ftp.connect(FTPUtil.SERVER);
		
			
			if (!ftp.login(FTPUtil.USER_WRITE, FTPUtil.SENHA_WRITE)) {
				ftp.enterLocalPassiveMode();
				//ftp.logout();
			}else {
				ftp.enterLocalPassiveMode();
				ftp.changeWorkingDirectory(FTPUtil.ROOT_DIR);
			}
		}
		
		return ftp;
	}
	
	@Override
	public FTPClient conectRead() throws Exception {
		
		
		if(!ftp.isConnected()) {
			
			ftp.connect(FTPUtil.SERVER);
		
			
			if (!ftp.login(FTPUtil.USER_READ, FTPUtil.SENHA_READ)) {
				ftp.enterLocalPassiveMode();
				//ftp.logout();
			}else {
				ftp.enterLocalPassiveMode();
				ftp.changeWorkingDirectory(FTPUtil.ROOT_DIR);
			}
		}		
		return ftp;
	}

	@Override
	public void closeFTP() throws Exception {		
		ftp.disconnect();
	}

}
