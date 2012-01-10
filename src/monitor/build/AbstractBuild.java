package monitor.build;

public abstract class AbstractBuild implements IBuild {

	protected BuildMeta meta;
	protected BuildStatus status;
	
	public AbstractBuild(BuildMeta meta){
		this.meta = meta;
		status = BuildStatus.UNKNOWN;
	}
	
	@Override
	public BuildMeta getMeta(){
		return meta;
	}
	
	@Override
	public BuildStatus getStatus() {
		return status;
	}	
	
	@Override
	public void setStatus(BuildStatus status){
		this.status = status;
	}
	
	@Override
	public String getServerUrl() {
		if(meta.server.startsWith("http://")){
			return meta.server;
		}else{
			return "http://"+meta.server;
		}
	}

	@Override
	public int compareTo(IBuild build) {
		return this.getMeta().identifier.compareTo(build.getMeta().identifier);
	}
}
