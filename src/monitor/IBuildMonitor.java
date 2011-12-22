package monitor;

import util.IComponent;
import util.notify.IChangeNotifier;

public interface IBuildMonitor extends IComponent {
	
	public void addBuild(String identifier) throws Exception;
	public void removeBuild(String identifier) throws Exception;
	
	public void updateMonitor() throws Exception;
	public MonitorInfo getMonitorInfo();

	public IChangeNotifier getChangeNotifier();
}
