package util;

import java.io.IOException;

public class SystemTools {

	public static String getWorkingDir(){
		return System.getProperty("user.dir");
	}
	
	public static boolean isWindows(){
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
	
	public static boolean isLinux(){
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}
	
	public static void openBrowser(String url) throws IOException {
		
		if(isWindows()){
			openWindowsBrowser(url);
		}else if(isLinux()){
			openLinuxBrowser(url);
		}else{
			throw new IOException("Could not open prowser for os: "+System.getProperty("os.name"));
		}
	}
	
	private static void openWindowsBrowser(String target) throws IOException {
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \""+target+"\"");
	}
	
	private static void openLinuxBrowser(String target) throws IOException {
		Runtime.getRuntime().exec("firefox "+target);
	}
	
	public static void sleep(long millis){
		try{ Thread.sleep(millis); }catch(InterruptedException e){ }
	}
}
