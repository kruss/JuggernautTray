package tray;

import monitor.MonitorData;

public interface IDisplayManager {

	public void updateDisplay(MonitorData data);
	public void forceDisplay();
}
