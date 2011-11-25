package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileTools {
	
	public static void writeFile(File file, String string) throws IOException { 

		FileWriter writer = new FileWriter(file.getAbsolutePath());
		int i=0; int c=0;
		while(i < string.length()){ 
			c = string.charAt(i++); 
			writer.write(c); 
		}
		writer.close();
	}
	
	public static String readFile(File file) throws IOException {

		StringBuffer buffer = new StringBuffer((int)file.length());
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
		while((line = reader.readLine()) != null){ 
			buffer.append(line+"\n"); 
		}
		reader.close();
		return buffer.toString();
	}
	
}
