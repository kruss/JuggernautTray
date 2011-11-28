package monitor;

import java.util.ArrayList;

import monitor.IBuild.BuildStatus;
import util.IComponent;
import util.notify.IChangeNotifier;

public interface IBuildMonitor extends IComponent {
	
	public void addBuild(String identifier) throws Exception;
	public void removeBuild(String identifier) throws Exception;
	
	public void updateStatus() throws Exception;
	public BuildStatus getStatus();
	public ArrayList<BuildInfo> getBuildInfo();
	
	public static class BuildInfo {
		public String identifier;
		public String url;
		public BuildStatus status;
		
		public BuildInfo(IBuild build){
			identifier = build.getIdentifier();
			url = build.getBuildUrl();
			status = build.getStatus();
		}
	}

	public IChangeNotifier getChangeNotifier();
}
