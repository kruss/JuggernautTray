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
