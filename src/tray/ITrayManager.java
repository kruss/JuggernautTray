package tray;

import java.awt.TrayIcon.MessageType;

import util.IComponent;

public interface ITrayManager extends IComponent {

	public void displayMessage(String message, MessageType type);
}
