package util.logger;

public interface ILogger {
	
	public void info(String message);
	public void log(String message);
	public void debug(String message);
	public void warn(String message);
	public void error(String message);
	public void error(Exception e);
}
