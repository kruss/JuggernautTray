package monitor;

import util.logger.ILogger;

public interface IBuild extends Comparable<IBuild> {

	public enum BuildStatus {
		UNKNOWN, OK, ERROR
	}
	
	public void updateBuild(ILogger logger);
	
	public String getIdentifier();
	public String getName();
	public BuildStatus getStatus();
	public String getServerUrl();
	public String getBuildUrl();
}
