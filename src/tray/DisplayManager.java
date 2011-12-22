package tray;

import java.util.ArrayList;

import tray.IMessageDisplay.MessageType;
import util.SystemTools;

import monitor.MonitorData;
import monitor.IBuild.BuildStatus;
import monitor.MonitorData.BuildInfo;

public class DisplayManager implements IDisplayManager {

	private IMessageDisplay tray;
	private MonitorData data;
	private boolean force;
	
	public DisplayManager(IMessageDisplay tray) {
		this.tray = tray;
		force = false;
	}
	
	@Override
	public void forceDisplay(){
		force = true;
	}

	@Override
	public void updateDisplay(MonitorData data) {
		
		if(this.data == null){ 
			// initial update
			if(data.getBuilds().size() > 0){
				displayStatusMessage(getStatusChanges(new MonitorData(), data), getMessageType(data));
			}else{
				displayEmptyMessage();
			}
		}else{ 
			// while running
			ArrayList<String> messages = getStatusChanges(this.data, data); 
			if(messages.size() > 0){
				displayStatusMessage(messages, getMessageType(data));
			}else if(force){
				if(data.getBuilds().size() > 0){
					displayStatusMessage(getStatusChanges(new MonitorData(), data), getMessageType(data));
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
	
	private void displayStatusMessage(ArrayList<String> changes, MessageType type) {
		StringBuilder message = new StringBuilder();
		for(int i=0; i< changes.size(); i++){
			message.append(changes.get(i));
			if(i < changes.size()-1){		
				message.append((SystemTools.isWindowsOS() ? "\n" : " "));
			}
		}
		tray.displayMessage(message.toString(), type);
	}

	private MessageType getMessageType(MonitorData data) {
		return data.getStatus() == BuildStatus.OK ? MessageType.INFO : MessageType.ERROR;
	}
	
	private ArrayList<String> getStatusChanges(MonitorData data1, MonitorData data2) {
		ArrayList<String> changes = new ArrayList<String>();
		for(BuildInfo build2 : data2.getBuilds()){
			BuildInfo build1 = data1.getBuild(build2.identifier);
			String identifier = (SystemTools.isWindowsOS() ? build2.identifier : build2.name);
			if(build1 == null){
				changes.add(identifier+" ("+build2.status+")");
			}else if(build1.status != build2.status){
				changes.add(identifier+" ("+build1.status+" >> "+build2.status+")");
			}
		}
		return changes;
	}
}
