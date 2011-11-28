package util;


public class SystemTools {
	
	public static String getWorkingDir(){
		return System.getProperty("user.dir");
	}
	
	public static String getOSName(){
		return System.getProperty("os.name");
	}
	
	public static boolean isWindowsOS(){
		return getOSName().toLowerCase().contains("windows");
	}
	
	public static boolean isLinuxOS(){
		return getOSName().toLowerCase().contains("linux");
	}
	
	public static boolean isMacOS(){
		return getOSName().toLowerCase().contains("mac");
	}
	
	public static void openBrowser(String url) throws Exception {
		
		if(isWindowsOS()){
			openWindowsBrowser(url);
		}else if(isLinuxOS()){
			openLinuxBrowser(url);
		}else if(isMacOS()){
			openMacBrowser(url);
		}else{
			throw new Exception("Unsuported OS: "+getOSName());
		}
	}
	
	private static void openWindowsBrowser(String path) throws Exception {
		Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \""+path+"\"");
	}
	
	private static void openLinuxBrowser(String path) throws Exception {
		Runtime.getRuntime().exec("firefox "+path);
	}
	
	private static void openMacBrowser(String path) throws Exception {
		Runtime.getRuntime().exec("open file://"+path.replaceAll("\\s", "%20"));
	}
	
	public static void sleep(long millis){
		try{ Thread.sleep(millis); }catch(InterruptedException e){ }
	}
}
