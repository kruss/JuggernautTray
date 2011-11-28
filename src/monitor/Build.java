package monitor;

import util.FileTools;
import util.logger.ILogger;

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
	public void updateBuild(ILogger logger) {
		try{
			status = BuildStatus.UNKNOWN;
			String content = FileTools.readUrl(getBuildUrl());
			for(String line : content.split("\n")){
				if(
						line.contains("UNDEFINED") || 
						line.contains("PROCESSING") || 
						line.contains("CANCEL")
				){
					break;
				}else if(
						line.contains("ERROR") || 
						line.contains("FAILURE")
				){
					status = BuildStatus.ERROR;
					break;
				}else if(
						line.contains("SUCCEED")
				){
					status = BuildStatus.OK;
					break;
				}
			}
		}catch(Exception e){
			logger.warn("Could not update ["+getIdentifier()+"] => "+e.getClass().getSimpleName()+" \""+e.getMessage()+"\"");
		}
	}
}
