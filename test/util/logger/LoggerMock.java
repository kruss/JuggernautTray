package util.logger;

public class LoggerMock implements ILogger {
	
	@Override
	public void emph(String message) {}
	
	@Override
	public void info(String message) {}

	@Override
	public void debug(String message) {}

	@Override
	public void warn(String message) {}
	
	@Override
	public void error(String message) {}
	
	@Override
	public void error(Exception e) {}
}
