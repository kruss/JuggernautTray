package tray.display;

import java.util.ArrayList;

import tray.display.IMessageDisplay.MessageType;

import monitor.MonitorInfo;
import monitor.IBuild.BuildStatus;
import monitor.MonitorInfo.BuildInfo;

public class NotificationManager implements INotificationManager {

	private IMessageDisplay display;
	private MonitorInfo data;
	
	public NotificationManager(IMessageDisplay display) {
		this.display = display;
	}

	@Override
	public void updateStatus(MonitorInfo data, boolean force) {
		ArrayList<String> rawMessages = getStatusMessages(new MonitorInfo(), data);
		if(this.data == null){ 
			// initial update
			if(data.getBuilds().size() > 0){
				displayStatusMessage(rawMessages, getMessageType(data));
			}else{
				displayEmptyMessage();
			}
		}else{ 
			// while running
			ArrayList<String> messages = getStatusMessages(this.data, data); 
			if(messages.size() > 0){
				displayStatusMessage(messages, getMessageType(data));
			}else if(force){
				if(data.getBuilds().size() > 0){
					displayStatusMessage(rawMessages, getMessageType(data));
				}else{
					displayEmptyMessage();
				}
				force = false;
			}
		}
		this.data = data;
	}
	
	private void displayStatusMessage(ArrayList<String> messages, MessageType type) {
		StringBuilder message = new StringBuilder();
		for(int i=0; i< messages.size(); i++){
			message.append(messages.get(i));
			if(i < messages.size()-1){		
				message.append((display.supportNewline() ? "\n" : ", "));
			}
		}
		display.displayMessage(message.toString(), type);
	}
	
	private void displayEmptyMessage() {
		display.displayMessage("Add some builds !", MessageType.WARNING);
	}

	private MessageType getMessageType(MonitorInfo data) {
		return data.getStatus() == BuildStatus.OK ? MessageType.INFO : MessageType.ERROR;
	}
	
	private ArrayList<String> getStatusMessages(MonitorInfo oldData, MonitorInfo newData) {
		ArrayList<String> messages = new ArrayList<String>();
		StatusAnalyzer analyzer = new StatusAnalyzer(oldData, newData);
		for(StatusAnalyzer.StatusChange change : analyzer.getChanges()){
			BuildInfo build = newData.getBuild(change.identifier);
			if(build != null){
				if(display.supportNewline()){
					if(change.oldStatus != null){
						messages.add(build.identifier+" ("+change.oldStatus+" >> "+change.newStatus+")");
					}else{
						messages.add(build.identifier+" ("+change.newStatus+")");
					}
				}else{
					messages.add(build.name+"::"+change.newStatus);
				}
			}
		}
		return messages;
	}
}
