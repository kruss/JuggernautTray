package tray;

import monitor.MonitorData;

public interface INotificationManager {

	public void updateStatus(MonitorData data);
	public void forceNotification();
}
