package tray.display;

public interface IMessageDisplay {

	public enum MessageType {
		INFO,
		WARNING,
		ERROR
	}
	
	public void displayMessage(String message, MessageType type);
	public boolean supportNewline();
}
