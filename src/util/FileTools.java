package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FileTools {
	
	public static void writeFile(File file, String string) throws Exception { 

		FileWriter writer = new FileWriter(file.getAbsolutePath());
		int i=0; int c=0;
		while(i < string.length()){ 
			c = string.charAt(i++); 
			writer.write(c); 
		}
		writer.close();
	}
	
	public static String readFile(File file) throws Exception {

		StringBuffer buffer = new StringBuffer((int)file.length());
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
		while((line = reader.readLine()) != null){ 
			buffer.append(line+"\n"); 
		}
		reader.close();
		return buffer.toString();
	}
	
	public static String readUrl(String url) throws Exception {
		
		StringBuffer buffer = new StringBuffer();
        URLConnection connection = (new URL(url)).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
        	buffer.append(line+"\n");
        }
        reader.close();
        return buffer.toString();
	}
}
