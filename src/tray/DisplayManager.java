package tray;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;

import util.SystemTools;

import monitor.MonitorData;
import monitor.IBuild.BuildStatus;
import monitor.MonitorData.BuildInfo;

public class DisplayManager {

	private ITrayManager tray;
	private MonitorData data;
	private boolean force;
	
	public DisplayManager(ITrayManager tray) {
		this.tray = tray;
		force = false;
	}
	
	public void forceDisplay(){
		force = true;
	}

	public void updateDisplay(MonitorData data) {
		
		if(this.data == null){ 
			// update after startup
			if(data.getBuilds().size() > 0){
				displayStatusMessage(getMessages(new MonitorData(), data), getType(data));
			}else{
				displayEmptyMessage();
			}
		}else{ 
			// update while running
			ArrayList<String> messages = getMessages(this.data, data); 
			if(messages.size() > 0){
				displayStatusMessage(messages, getType(data));
			}else if(force){
				if(data.getBuilds().size() > 0){
					displayStatusMessage(getMessages(new MonitorData(), data), getType(data));
				}else{
					displayEmptyMessage();
				}
				force = false;
			}
		}
		this.data = data;
	}

	private void displayEmptyMessage() {
		tray.displayMessage("Add some builds !", MessageType.WARNING);
	}
	
	private void displayStatusMessage(ArrayList<String> messages, MessageType type) {
		tray.displayMessage(getInfo("Status", messages), type);
	}

	private MessageType getType(MonitorData data) {
		return data.getStatus() == BuildStatus.OK ? MessageType.INFO : MessageType.ERROR;
	}
	
	private ArrayList<String> getMessages(MonitorData data1, MonitorData data2) {
		ArrayList<String> messages = new ArrayList<String>();
		for(BuildInfo build2 : data2.getBuilds()){
			BuildInfo build1 = data1.getBuild(build2.identifier);
			String name = (SystemTools.isWindowsOS() ? build2.identifier : build2.name);
			if(build1 == null){
				messages.add(name+" ("+build2.status+")");
			}else if(build1.status != build2.status){
				messages.add(name+" ("+build1.status+" >> "+build2.status+")");
			}
		}
		return messages;
	}

	private String getInfo(String title, ArrayList<String> messages) {
		StringBuilder info = new StringBuilder();
		if(SystemTools.isWindowsOS()){
			info.append(title+"\n\n");
		}
		for(String message : messages){
			info.append(message);
			if(SystemTools.isWindowsOS()){
				info.append("\n");
			}else{
				info.append(" ");
			}
		}
		return info.toString();
	}
}
