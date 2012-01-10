package monitor.build;

public interface IBuild extends Comparable<IBuild> {

	public enum BuildType {
		JUGGERNAUT, BAMBOO
	}
	
	public enum BuildStatus {
		UNKNOWN, OK, ERROR
	}
	
	public BuildMeta getMeta();

	public String getServerUrl();
	public String getBuildUrl();

	public BuildStatus getStatus();
	public void setStatus(BuildStatus status);	
	public void parseStatus(String content) throws Exception;
}
