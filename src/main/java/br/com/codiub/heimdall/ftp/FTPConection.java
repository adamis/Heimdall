package br.com.codiub.heimdall.ftp;

import org.apache.commons.net.ftp.FTPClient;

public interface FTPConection {
	public FTPClient conectWrite() throws Exception;
	public FTPClient conectRead() throws Exception;
	public void closeFTP() throws Exception;
}
