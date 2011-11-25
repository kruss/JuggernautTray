package core;

public class Logger {

	public void log(String message){
		System.out.println(message);
	}
	
	public void error(String message){
		System.err.println(message);
	}
	
	public void error(Exception e){
		e.printStackTrace();
	}
}
