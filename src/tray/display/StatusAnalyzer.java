package tray.display;

import java.util.ArrayList;

import monitor.MonitorInfo;
import monitor.MonitorInfo.BuildInfo;
import monitor.build.IBuild.BuildStatus;

public class StatusAnalyzer {

	public static class StatusChange {

		public String identifier;
		public BuildStatus newStatus;
		public BuildStatus oldStatus;
		
		public StatusChange(BuildInfo build) {
			identifier = build.identifier;
			newStatus = build.status;
			oldStatus = null;
		}
	}
	
	private MonitorInfo oldData; 
	private MonitorInfo newData;
	
	public StatusAnalyzer(MonitorInfo oldData, MonitorInfo newData){
		this.oldData = oldData;
		this.newData = newData;
	}
	
	public ArrayList<StatusChange> getChanges() {
		ArrayList<StatusChange> changes = new ArrayList<StatusChange>();
		for(BuildInfo newBuild : newData.getBuilds()){
			StatusChange change = new StatusChange(newBuild);
			BuildInfo oldBuild = oldData.getBuild(newBuild.identifier);
			if(oldBuild != null){
				change.oldStatus = oldBuild.status;
				if(change.newStatus != change.oldStatus){
					changes.add(change);
				}
			}else{
				changes.add(change);
			}
		}
		return changes;
	}
}
