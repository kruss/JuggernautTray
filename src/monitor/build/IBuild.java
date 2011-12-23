package monitor.build;

public interface IBuild extends Comparable<IBuild> {

	public enum BuildType {
		JUGGERNAUT, BAMBOO
	}
	
	public enum BuildStatus {
		UNKNOWN, OK, ERROR
	}
	
	public BuildMeta getMeta();
	public BuildStatus getStatus();
	
	public String getServerUrl();
	public String getBuildUrl();
	
	public void updateStatus(String content) throws Exception;
}
