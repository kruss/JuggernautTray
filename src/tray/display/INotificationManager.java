package tray.display;

import monitor.MonitorInfo;

public interface INotificationManager {

	public void updateStatus(MonitorInfo data);
	public void forceNotification();
}