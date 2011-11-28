package monitor;

public class Build implements IBuild {

	private String buildName;
	private String serverUrl;
	private BuildStatus buildStatus;

	@Override
	public String getName() {
		return buildName;
	}
	
	@Override
	public String getServerUrl() {
		return serverUrl;
	}
	
	@Override
	public String getBuildUrl() {
		if(serverUrl.startsWith("http://")){
			return serverUrl+"/index["+buildName.hashCode()+"].htm";
		}else{
			return "http://"+serverUrl+"/index["+buildName.hashCode()+"].htm";
		}
	}
	
	@Override
	public BuildStatus getStatus() {
		return buildStatus;
	}	
	
	@Override
	public void setStatus(BuildStatus status) {
		this.buildStatus = status;
	}

	public Build(String identifier) throws Exception {
		parseIdentifier(identifier);
		buildStatus = BuildStatus.UNKNOWN;
	}

	@Override
	public String getIdentifier(){
		return buildName+"@"+serverUrl;
	}
	
	private void parseIdentifier(String identifier) throws Exception {
		int index = identifier.indexOf("@");
		if(index != -1){
			buildName = identifier.substring(0, index);
			serverUrl = identifier.substring(index+1, identifier.length());
			if(buildName.isEmpty() || serverUrl.isEmpty()){
				throw new Exception("invalid data: "+identifier);
			}
		}else{
			throw new Exception("invalid format: "+identifier);
		}
	}

	@Override
	public int compareTo(IBuild build) {
		return this.getIdentifier().compareTo(build.getIdentifier());
	}
}
