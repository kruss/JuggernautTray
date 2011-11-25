package core;

import javax.swing.JFrame;

public class SetupDialog extends JFrame {

	private Logger logger;
	private Configuration config;
	
	public SetupDialog(Logger logger, Configuration config){
		this.logger = logger;
		this.config = config;
		
		setTitle(JuggernautTray.APP_NAME);
		setSize(320, 240);
		setLocation(100, 100);
		setVisible(false);
	}
}
