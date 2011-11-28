package util.logger;

public class Logger implements ILogger {

	private boolean verbose;
	
	public Logger(boolean verbose){
		this.verbose = verbose;
	}
	
	@Override
	public void info(String message) {
		System.out.println("\n\t[ "+message+" ]\n");
	}

	@Override
	public void log(String message){
		System.out.println(message);
	}
	
	@Override
	public void debug(String message) {
		if(verbose){
			System.out.println("~> "+message);
		}
	}
	
	@Override
	public void warn(String message) {
		System.out.println("WARN: "+message);
	}
	
	@Override
	public void error(String message){
		System.err.println("ERROR: "+message);
	}
	
	@Override
	public void error(Exception e){
		e.printStackTrace();
	}
}
