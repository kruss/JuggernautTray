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
	public void updateStatus(String content) throws Exception {
		status = BuildStatus.UNKNOWN;
		boolean parse = false;
		for(String line : content.split("\n")){
			if(line.contains("viewBuild:"+meta.name)){
				if(!parse){
					parse = true;
					status = BuildStatus.ERROR;
				}else{
					return;
				}
			}
			if(parse){
				if(line.contains("planKeySection Successful")){
					status = BuildStatus.OK;
				}
			}
		}
	}
}
