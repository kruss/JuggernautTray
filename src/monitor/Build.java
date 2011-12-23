package monitor;

public class Build implements IBuild {

	private String name;
	private BuildStatus status;
	private String serverUrl;

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public BuildStatus getStatus() {
		return status;
	}	
	
	@Override
	public String getServerUrl() {
		return serverUrl;
	}
	
	@Override
	public String getBuildUrl() {
		if(serverUrl.startsWith("http://")){
			return serverUrl+"/index["+name.hashCode()+"].htm";
		}else{
			return "http://"+serverUrl+"/index["+name.hashCode()+"].htm";
		}
	}

	public Build(String identifier) throws Exception {
		parseIdentifier(identifier);
		status = BuildStatus.UNKNOWN;
	}

	@Override
	public String getIdentifier(){
		return name+"@"+serverUrl;
	}
	
	private void parseIdentifier(String identifier) throws Exception {
		int index = identifier.indexOf("@");
		if(index != -1){
			name = identifier.substring(0, index);
			serverUrl = identifier.substring(index+1, identifier.length());
			if(name.isEmpty() || serverUrl.isEmpty()){
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

	@Override
	public void updateStatus(String content) throws Exception {
		status = BuildStatus.UNKNOWN;
		for(String line : content.split("\n")){
			if(
					line.contains("UNDEFINED") || 
					line.contains("PROCESSING") || 
					line.contains("CANCEL")
			){
				return;
			}else if(
					line.contains("ERROR") || 
					line.contains("FAILURE")
			){
				status = BuildStatus.ERROR;
				return;
			}else if(
					line.contains("SUCCEED")
			){
				status = BuildStatus.OK;
				return;
			}
		}
	}
}
