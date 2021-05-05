/**
 * 
 */
package br.com.codiub.heimdall.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPFile;

/**
 * @author adamis.rocha
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FTPConectionPMU ftpConection = new FTPConectionPMU();
		try {
			FTPList ftpList = new FTPList(ftpConection);
			FTPFile[] folders = ftpList.getFolders();
			
			for (int i = 0; i < folders.length; i++) {
				//System.err.println(folders[i].getName());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
