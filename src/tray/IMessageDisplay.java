package tray;

public interface IMessageDisplay {

	public enum MessageType {
		INFO,
		WARNING,
		ERROR
	}
	
	public void displayMessage(String message, MessageType type);
}
