package monitor;

import java.io.File;
import java.util.ArrayList;

import java.util.Collections;

import monitor.build.BambooBuild;
import monitor.build.JuggernautBuild;
import monitor.build.BuildMeta;
import monitor.build.IBuild;
import monitor.build.IBuild.BuildStatus;
import monitor.build.IBuild.BuildType;

import util.FileTools;
import util.SystemTools;
import util.logger.ILogger;
import util.notify.ChangeNotifier;
import util.notify.IChangeNotifier;

public class BuildMonitor implements IBuildMonitor {
	
	private ILogger logger;
	private File file;
	private int cycle;
	
	private ArrayList<IBuild> builds;
	private BuildMonitorThread thread;
	private IChangeNotifier notifier;
	
	public BuildMonitor(ILogger logger, File file, int cycle){
		this.logger = logger;
		this.file = file;
		this.cycle = cycle;
		
		builds = new ArrayList<IBuild>();
		notifier = new ChangeNotifier(logger, this);
	}

	@Override
	public IChangeNotifier getChangeNotifier() {
		return notifier;
	}
	
	@Override
	public void init() throws Exception {
		load();
		if(thread == null){
			logger.debug("start monitor");
			thread = new BuildMonitorThread();
			thread.start();
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(thread != null){
			logger.debug("stop monitor");
			thread.active = false;
			thread.interrupt();
			while(thread.isAlive()){
				SystemTools.sleep(100);
			}
			thread = null;
		}
	}
	
	@Override
	public synchronized MonitorInfo getMonitorInfo(){
		return new MonitorInfo(builds);
	}

	@Override
	public synchronized void addBuild(String identifier) throws Exception {
		if(getBuild(identifier) == null){
			logger.info("add build: "+identifier);
			IBuild build = null;
			BuildMeta meta = new BuildMeta(identifier);
			if(meta.type.equalsIgnoreCase(BuildType.JUGGERNAUT.toString())){
				build = new JuggernautBuild(meta);
			}else if(meta.type.equalsIgnoreCase(BuildType.BAMBOO.toString())){
				build = new BambooBuild(meta);
			}else{
				throw new Exception("invalid type: "+meta.type);
			}
			builds.add(build);
			Collections.sort(builds);
			save();
		}
	}
	
	@Override
	public synchronized void removeBuild(String identifier) throws Exception {
		IBuild build = getBuild(identifier);
		if(build != null){
			logger.info("remove build: "+build.getMeta().identifier);
			builds.remove(build);
			save();
		}
	}
	
	private IBuild getBuild(String identifier) {
		for(IBuild build : builds){
			if(build.getMeta().identifier.equals(identifier)){
				return build;
			}
		}
		return null;
	}

	@Override
	public synchronized void updateMonitor() throws Exception {
		for(IBuild build : builds){
			logger.info("update build: "+build.getMeta().identifier);
			try{
				String content = FileTools.readUrl(build.getBuildUrl());
				logger.debug(content);
				build.parseStatus(content);
			}catch(Exception e){
				logger.warn("Could not update ["+build.getMeta().identifier+"] => "+e.getClass().getSimpleName()+" \""+e.getMessage()+"\"");
				build.setStatus(BuildStatus.UNKNOWN);
			}
			logger.info("=> "+build.getMeta().identifier+" ("+build.getStatus()+")");
		}
		notifier.notifyListeners();
	}

	class BuildMonitorThread extends Thread {
		public boolean active;
		public void run() {
			active = true;
			while(active){
				try{
					updateMonitor();
				}catch (Exception e){
					logger.error(e);
				}		
				SystemTools.sleep(cycle);
			}
		}
	}
	
	private void load() throws Exception {
		if(file.exists()){
			logger.debug("load: "+file.getAbsolutePath());
			String content = FileTools.readFile(file);
			for(String line : content.split("\n")){
				String identifier = line.trim();
				if(!identifier.isEmpty()){
					try{
						addBuild(identifier);
					}catch(Exception e){
						logger.error(e);
					}
				}
			}
		}
	}
	
	private void save() throws Exception {
		logger.debug("save: "+file.getAbsolutePath());
		StringBuilder content = new StringBuilder();
		for(IBuild build : builds){
			content.append(build.getMeta().identifier+"\n");
		}
		FileTools.writeFile(file, content.toString());
	}
}
