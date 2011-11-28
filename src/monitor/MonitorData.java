package monitor;

import java.util.ArrayList;

import monitor.IBuild.BuildStatus;

public class MonitorData {
	
	public static class BuildInfo {
		public String identifier;
		public String name;
		public String url;
		public BuildStatus status;
		
		public BuildInfo(IBuild build){
			identifier = build.getIdentifier();
			name = build.getName();
			url = build.getBuildUrl();
			status = build.getStatus();
		}
	}

	private ArrayList<BuildInfo> builds;
	
	public MonitorData(){
		this.builds = new ArrayList<BuildInfo>();
	}
	
	public MonitorData(ArrayList<IBuild> builds){
		this.builds = new ArrayList<BuildInfo>();
		for(IBuild build : builds){
			this.builds.add(new BuildInfo(build));
		}
	}
	
	public ArrayList<BuildInfo> getBuilds(){
		return builds;
	}
	
	public ArrayList<BuildInfo> getBuilds(BuildStatus status) {
		ArrayList<BuildInfo> builds = new ArrayList<BuildInfo>();
		for(BuildInfo build : this.builds){
			if(build.status == status){
				builds.add(build);
			}
		}
		return builds;
	}
	
	public ArrayList<String> getIdentifiers() {
		ArrayList<String> names = new ArrayList<String>();
		for(BuildInfo build : builds){
			names.add(build.identifier);
		}
		return names;
	}
	
	public BuildInfo getBuild(String identifier) {
		for(BuildInfo build : builds){
			if(build.identifier.equals(identifier)){
				return build;
			}
		}
		return null;
	}
	
	public BuildStatus getStatus(){
		if(builds.size() > 0){
			for(BuildInfo build : builds){
				if(build.status == BuildStatus.ERROR){
					return BuildStatus.ERROR;
				}else if(build.status == BuildStatus.UNKNOWN){
					return BuildStatus.UNKNOWN;
				}
			}
			return BuildStatus.OK;
		}else{			
			return BuildStatus.UNKNOWN;
		}
	}
}
