package br.com.codiub.heimdall.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.codiub.heimdall.config.LoadConfigs;

@Component
public class FTPConectionPMU implements FTPConection{
	
	@Autowired
	private LoadConfigs loadConfigs = new LoadConfigs();
	
	FTPClient ftp = new FTPClient();

	@Override
	public FTPClient conectWrite() throws Exception {
		loadConfigs.loadConfigs();		
		
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
		loadConfigs.loadConfigs();
		
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
		loadConfigs.loadConfigs();
		ftp.disconnect();
	}

}
