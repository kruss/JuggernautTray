package monitor;

public interface IBuild extends Comparable<IBuild> {

	public enum BuildStatus {
		UNKNOWN, OK, ERROR
	}
	
	public String getIdentifier();
	
	public BuildStatus getStatus();
	void setStatus(BuildStatus status);

	public String getName();
	public String getServerUrl();
	public String getBuildUrl();
}
