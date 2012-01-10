package monitor.build;


public class JuggernautBuild extends AbstractBuild {

	public JuggernautBuild(BuildMeta meta) {
		super(meta);
	}

	@Override
	public String getBuildUrl() {
		return getServerUrl()+"/index["+meta.name.hashCode()+"].htm";
	}

	@Override
	public void parseStatus(String content) throws Exception {
		status = BuildStatus.UNKNOWN;
		
		boolean parse = false;
		for(String line : content.split("\n")){
			if(line.contains("Info")){
				parse = true;
				continue;
				
			}else if(line.contains("Operations")){
				break;
				
			}else if(parse){
				if(
						line.contains("SUCCEED")
				){
					status = BuildStatus.OK;
					break;
					
				}else if(
						line.contains("ERROR") || 
						line.contains("FAILURE")
				){
					status = BuildStatus.ERROR;
					break;
					
				}
				
			}
		}
	}
}
