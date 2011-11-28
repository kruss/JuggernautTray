package core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import util.IComponent;
import util.SystemTools;
import util.logger.ILogger;
import util.logger.Logger;
import util.options.IOptions;
import util.options.Options;
import tray.ITrayManager;
import tray.TrayManager;
import monitor.BuildMonitor;
import monitor.IBuildMonitor;

public class JuggernautTray implements IComponent {

	public static final String APP_NAME = "JuggernautTray";
	public static final String APP_VERSION = "0.1.0";
	
	private static final String OUTPUT_FILE = "Juggernaut.tray";
	private static final int UPDATE_CYCLE = 5 * 60 * 1000; // 5 minutes
	
	public static final int PROCESS_OK = 0;
	public static final int PROCESS_NOT_OK = -1;
	
	public static void main(String[] args) {
		try {
			JuggernautTray app = new JuggernautTray(args);
			app.init();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(PROCESS_NOT_OK);
		}
	}

	private IOptions options;
	private ILogger logger;
	private ArrayList<IComponent> components;
	
	public JuggernautTray(String[] args) throws Exception {
		options = new Options(args);
		logger = new Logger(options.isVerboseMode());
		components = new ArrayList<IComponent>();
		
		IBuildMonitor buildMonitor = new BuildMonitor(
				logger, 
				new File(SystemTools.getWorkingDir()+File.separator+OUTPUT_FILE), 
				UPDATE_CYCLE);
		components.add(buildMonitor);
		ITrayManager trayManager = new TrayManager(
				logger, 
				this, 
				buildMonitor);
		components.add(trayManager);
	}

	@Override
	public void init() throws Exception {
		logger.info(APP_NAME+" ("+APP_VERSION+")");
		for(IComponent component : components){
			component.init();
		}
	}

	@Override
	public void shutdown() throws Exception {
		logger.info("exit");
		Collections.reverse(components);
		for(IComponent component : components){
			try{ 
				component.shutdown();
			}catch(Exception e){
				logger.error(e);
			}
		}
		System.exit(PROCESS_OK);
	}
}
