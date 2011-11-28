package util.logger;

import java.util.Date;

import util.StringTools;

public class Logger implements ILogger {

	private boolean verbose;
	
	public Logger(boolean verbose){
		this.verbose = verbose;
	}
	
	@Override
	public void emph(String message) {
		System.out.println("\n\t[ "+message+" ]\n");
	}

	@Override
	public void info(String message){
		print("INFO", message);
	}
	
	@Override
	public void debug(String message) {
		if(verbose){
			print("DEBUG", message);
		}
	}
	
	@Override
	public void warn(String message) {
		print("WARN", message);
	}
	
	@Override
	public void error(String message){
		print("ERROR", message);
	}
	
	@Override
	public void error(Exception e){
		print("ERROR", StringTools.trace(e));
	}
	
	private void print(String level, String message){
		System.out.println((new Date()).toString().substring(11, 19)+" <"+level+"> "+message);
	}
}
