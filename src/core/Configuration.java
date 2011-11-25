package core;

import java.io.File;
import java.util.ArrayList;

import util.ChangeNotifier;
import util.IChangeListener;

public class Configuration {
	
	private transient Logger logger;
	private transient File file;
	public transient ChangeNotifier notifier;
	
	public String serverUrl; 					// url of build-server index
	public ArrayList<String> buildFilter; 		// list of builds to monitor or all if empty
	public int updateCycle;						// update-cycle in minutes
	
	public boolean isValid(){
		return !serverUrl.isEmpty();
	}

	
	public Configuration(Logger logger, File file) throws Exception {
		this.logger = logger;
		this.file = file;
		notifier = new ChangeNotifier(logger, this);
		
		if(file.exists()){
			load();
		}else{
			reset();
		}
	}
	
	private void reset() {
		logger.log("reset config");
		
		serverUrl = "";
		buildFilter = new ArrayList<String>();
		updateCycle = 5;
	}
	
	public void load() throws Exception {
		logger.log("load config: "+file.getAbsolutePath());
		
		// TODO
	}
	
	public void save() throws Exception {
		logger.log("save config: "+file.getAbsolutePath());
		
		// TODO
	}
}
