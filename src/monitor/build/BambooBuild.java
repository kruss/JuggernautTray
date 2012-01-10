package monitor.build;


public class BambooBuild extends AbstractBuild {

	public BambooBuild(BuildMeta meta) {
		super(meta);
	}

	@Override
	public String getServerUrl() {
		return super.getServerUrl()+"/allPlans.action";
	}
	
	@Override
	public String getBuildUrl() {
		return super.getServerUrl()+"/browse/"+meta.name;
	}

	@Override
	public void parseStatus(String content) throws Exception {
		status = BuildStatus.UNKNOWN;
		
		boolean parse = false;
		for(String line : content.split("\n")){
			if(line.contains("Recent History")){
				parse = true;
				continue;
				
			}else if(line.contains("Plan Statistics")){
				break;
				
			}else if(parse){
				if(
						line.contains("Successful")
				){
					status = BuildStatus.OK;
					break;
					
				}else if(
						line.contains("Failed")
				){
					status = BuildStatus.ERROR;
					break;
					
				}
				
			}
		}
	}
}
