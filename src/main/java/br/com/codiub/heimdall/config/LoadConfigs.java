package br.com.codiub.heimdall.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import br.com.codiub.heimdall.HeimdallApiApplication;
import br.com.codiub.heimdall.ftp.FTPUtil;

@Configuration
public class LoadConfigs {


	public void loadConfigs() throws Exception {
		if(
				FTPUtil.SERVER == null &&
				FTPUtil.USER_READ == null &&
				FTPUtil.SENHA_READ == null &&
				FTPUtil.USER_WRITE == null &&
				FTPUtil.SENHA_WRITE == null 			
		) {

			List<String> resourceFileAsString = getResourceFileAsString("config.xml");

			for (String temp : resourceFileAsString) {
				if(temp.contains("<server>")) {
					FTPUtil.SERVER = temp.replace("<server>", "").replace("</server>", "").trim();		
				}

				if(temp.contains("<port>")) {
					FTPUtil.PORT = Integer.parseInt(temp.replace("<port>", "").replace("</port>", "").trim());		
				}

				if(temp.contains("<root_dir>")) {				
					FTPUtil.ROOT_DIR = temp.replace("<root_dir>", "").replace("</root_dir>", "").trim();
				}

				if(temp.contains("<user_read>")) {
					FTPUtil.USER_READ = temp.replace("<user_read>", "").replace("</user_read>", "").trim();		
				}

				if(temp.contains("<senha_read>")) {
					FTPUtil.SENHA_READ = temp.replace("<senha_read>", "").replace("</senha_read>", "").trim();		
				}

				if(temp.contains("<user_write>")) {
					FTPUtil.USER_WRITE = temp.replace("<user_write>", "").replace("</user_write>", "").trim();		
				}

				if(temp.contains("<senha_write>")) {
					FTPUtil.SENHA_WRITE = temp.replace("<senha_write>", "").replace("</senha_write>", "").trim();		
				}

			}

			System.err.println("SERVER: "+FTPUtil.SERVER);
			System.err.println("PORT: "+FTPUtil.PORT);
			System.err.println("ROOT_DIR: "+FTPUtil.ROOT_DIR);
			System.err.println("USER_READ: "+FTPUtil.USER_READ);
			System.err.println("SENHA_READ: "+FTPUtil.SENHA_READ);
			System.err.println("USER_WRITE: "+FTPUtil.USER_WRITE);
			System.err.println("SENHA_WRITE: "+FTPUtil.SENHA_WRITE);
		}else {
			System.err.println("Conexao nao Configurado!");
		}

	}

	public static List<String> getResourceFileAsString(String fileName) throws IOException {
		InputStream is = getResourceFileAsInputStream(fileName);
		if (is != null) {
			String strCurrentLine;
			List<String> list = new ArrayList<String>();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			while ((strCurrentLine = reader.readLine()) != null) {
				list.add(strCurrentLine);
			}

			return list;
		} else {
			throw new RuntimeException("resource not found");
		}
	}

	public static InputStream getResourceFileAsInputStream(String fileName) {
		ClassLoader classLoader = HeimdallApiApplication.class.getClassLoader();
		return classLoader.getResourceAsStream(fileName);
	}
}
