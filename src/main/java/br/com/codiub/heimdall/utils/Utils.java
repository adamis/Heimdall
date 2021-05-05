package br.com.codiub.heimdall.utils;

public class Utils {

	public static String cleanFolderUrl(String folder) {
		
		
		
		if(folder.substring(0,1).equals("/") || folder.substring(0,1).equals("\\")) {
			folder = folder.substring(1, folder.length());
		}
		
		if(folder.substring(folder.length()-1,folder.length()).equals("/") || folder.substring(folder.length()-1,folder.length()).equals("\\")) {
			folder = folder.substring(0,folder.length()-1);
		}
		
		
		
		return folder;
	}
	
}
