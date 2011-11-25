package core;

import java.util.ArrayList;

import util.ChangeNotifier;
import util.IChangeListener;
import util.SystemTools;

public class BuildMonitor implements IChangeListener {

	private Logger logger;
	private Configuration config;
	private BuildMonitorThread thread;
	public ChangeNotifier notifier;
	
	private JuggernautServer server;
	private ArrayList<Build> builds;
	
	public synchronized BuildInfo getBuildInfo(){
		return new BuildInfo(builds);
	}
	
	public BuildMonitor(Logger logger, Configuration config){
		this.logger = logger;
		this.config = config;
		notifier = new ChangeNotifier(logger, this);
		
		server = new JuggernautServer(logger);
		reset();
		config.notifier.addListener(this);
	}
	
	private void reset() {
		builds = new ArrayList<Build>();
	}

	public void start(){
		if(thread == null){
			logger.log("start monitor");
			thread = new BuildMonitorThread();
			thread.start();
		}
	}
	
	public void stop(){
		if(thread != null){
			logger.log("stop monitor");
			thread.active = false;
			thread.interrupt();
			while(thread.isAlive()){
				SystemTools.sleep(100);
			}
			thread = null;
		}
	}

	public synchronized void update() throws Exception {
		int hash = getHash();
		if(config.isValid()){
			builds = server.getBuilds(config.serverUrl, config.buildFilter);
		}else{
			reset();
		}
		if(hash != getHash()){
			notifier.notifyListeners();
		}
	}
	
	private synchronized int getHash() {
		int hash = 0;
		for(Build build : builds){
			hash += build.getHash();
		}
		return hash;
	}

	@Override
	public void changed(Object object) {
		if(object instanceof Configuration){
			try{
				update();
			}catch(Exception e){
				logger.error(e);
			}
		}	
	}
	
	class BuildMonitorThread extends Thread {
		public boolean active;
		public void run() {
			active = true;
			while(active){
				try{
					update();
				}catch (Exception e){
					logger.error(e);
				}		
				SystemTools.sleep(config.updateCycle * 60 * 1000);
			}
		}
	}
	
	public static class Build {
		public enum Status {
			SUCCEED, ERROR, FAILURE
		}
		public String name;
		public Status status;
		
		public Build(String name, Status result){
			this.name = name;
			this.status = result;
		}
		
		public int getHash() {
			return name.hashCode() + status.toString().hashCode();
		}
	}
	
	public static class BuildInfo {
		public enum Status {
			UNDEFINED, OK, NOT_OK
		}
		public ArrayList<String> okBuilds;
		public ArrayList<String> nokBuilds;
		public Status status;
		
		public BuildInfo(ArrayList<Build> builds){
			okBuilds = new ArrayList<String>();
			nokBuilds = new ArrayList<String>();
			status = Status.UNDEFINED;
			if(!builds.isEmpty()){
				status = Status.OK;
				for(Build build : builds){
					if(build.status == Build.Status.SUCCEED){
						okBuilds.add(build.name);
					}else{
						nokBuilds.add(build.name);
						status = Status.NOT_OK;
					}
				}
			}
		}
	}
}
