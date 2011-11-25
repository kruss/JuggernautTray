package core;

import http.WebClient;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import util.FileTools;
import util.SystemTools;

public class JuggernautTray {

	public static final String APP_NAME = "Juggernaut-Tray (0.1.0)";
	public static final String DEFAULT_ID = "Juggernaut";
	
	public static void main(String[] args) {
		try {
			if(args.length == 0){				
				new JuggernautTray(DEFAULT_ID);
			}else if(args.length == 1){
				new JuggernautTray(DEFAULT_ID+"-"+args[0]);
			}else{
				throw new Exception("Invalid arguments");
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private Logger logger;
	private Configuration config;
	private SetupDialog dialog;
	private BuildMonitor monitor;
	private TrayIcon icon;
	
	public JuggernautTray(String id) throws Exception {
		logger = new Logger();
		logger.log(JuggernautTray.APP_NAME);
		
		config = new Configuration(logger, new File(SystemTools.getWorkingDir()+File.separator+id+".xml"));
		dialog = new SetupDialog(logger, config);
		monitor = new BuildMonitor(logger, config);

		if(SystemTray.isSupported()){
			init();
		}else{
			throw new Exception("Could not access system-tray");
	    }
	}

	private void init() throws Exception {
		
        PopupMenu popupMenu = new PopupMenu();
        MenuItem setupMenu = new MenuItem("Setup");
        setupMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	openDialog();
            }
        });
        popupMenu.add(setupMenu);
        MenuItem quitMenu = new MenuItem("Quit");
        quitMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	monitor.stop();
                System.exit(0);
            }
        });
        popupMenu.add(quitMenu);
        
        Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");
        icon = new TrayIcon(image, JuggernautTray.APP_NAME, popupMenu);
		icon.setImageAutoSize(true);
		icon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try{
            		if(config.isValid()){
            			SystemTools.openBrowser(config.serverUrl);
            		}else{
            			openDialog();
            		}
            	}catch(Exception ex){
            		ex.printStackTrace();
            		displayMessage(ex.getMessage(), TrayIcon.MessageType.ERROR);
            	}
            
            }
        });
		
		SystemTray tray = SystemTray.getSystemTray();
		tray.add(icon);
		
		monitor.start();
		if(!config.isValid()){
			displayMessage("Double-Click to open Setup", TrayIcon.MessageType.INFO);
		}
	}

	private void displayMessage(String message, MessageType type) {
		icon.displayMessage(JuggernautTray.APP_NAME, message, type);
	}

	private void openDialog() {
		dialog.setVisible(true);
	}
}
