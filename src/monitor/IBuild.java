package monitor;

import util.logger.ILogger;

public interface IBuild extends Comparable<IBuild> {

	public enum BuildStatus {
		UNKNOWN, OK, ERROR
	}
	
	public String getIdentifier();
	public String getName();
	public BuildStatus getStatus();
	public String getServerUrl();
	public String getBuildUrl();
	
	public void updateBuild(ILogger logger) throws Exception;
}
