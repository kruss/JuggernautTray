package monitor;

import java.io.File;
import java.util.ArrayList;

import java.util.Collections;

import monitor.IBuild.BuildStatus;

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
			logger.log("start monitor");
			thread = new BuildMonitorThread();
			thread.start();
		}
	}

	@Override
	public void shutdown() throws Exception {
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
	
	@Override
	public synchronized BuildStatus getStatus(){
		if(builds.size() > 0){
			for(IBuild build : builds){
				if(build.getStatus() == BuildStatus.ERROR){
					return BuildStatus.ERROR;
				}else if(build.getStatus() == BuildStatus.UNKNOWN){
					return BuildStatus.UNKNOWN;
				}
			}
			return BuildStatus.OK;
		}else{			
			return BuildStatus.UNKNOWN;
		}
	}
	
	@Override
	public synchronized ArrayList<BuildInfo> getBuildInfo(){
		ArrayList<BuildInfo> infos = new ArrayList<BuildInfo>();
		for(IBuild build : builds){
			infos.add(new BuildInfo(build));
		}
		return infos;
	}

	@Override
	public synchronized void addBuild(String identifier) throws Exception {
		if(getBuild(identifier) == null){
			logger.log("add build: "+identifier);
			IBuild build = new Build(identifier);
			builds.add(build);
			Collections.sort(builds);
			save();
			notifier.notifyListeners();
		}
	}
	
	@Override
	public synchronized void removeBuild(String identifier) throws Exception {
		IBuild build = getBuild(identifier);
		if(build != null){
			logger.log("remove build: "+build.getIdentifier());
			builds.remove(build);
			save();
			notifier.notifyListeners();
		}
	}
	
	private IBuild getBuild(String identifier) {
		for(IBuild build : builds){
			if(build.getIdentifier().equals(identifier)){
				return build;
			}
		}
		return null;
	}

	@Override
	public synchronized void updateStatus() throws Exception {
		for(IBuild build : builds){
			logger.log("update: "+build.getIdentifier());
			try{
				build.setStatus(getStatus(build));
			}catch(Exception e){
				build.setStatus(BuildStatus.UNKNOWN);
				logger.warn(
						"Could update ["+build.getIdentifier()+"] => "+
						e.getClass().getSimpleName()+" \""+e.getMessage()+"\""
				);
			}
		}
		notifier.notifyListeners();
	}
	
	private BuildStatus getStatus(IBuild build) throws Exception {
		String content = FileTools.readUrl(build.getBuildUrl());
		logger.debug(content);
		for(String line : content.split("\n")){
			if(
					line.contains("UNDEFINED") || 
					line.contains("PROCESSING") || 
					line.contains("CANCEL")
			){
				return BuildStatus.UNKNOWN;
			}else if(
					line.contains("ERROR") || 
					line.contains("FAILURE")
			){
				return BuildStatus.ERROR;
			}else if(
					line.contains("SUCCEED")
			){
				return BuildStatus.OK;
			}
		}
		return BuildStatus.UNKNOWN;
	}

	class BuildMonitorThread extends Thread {
		public boolean active;
		public void run() {
			active = true;
			while(active){
				try{
					updateStatus();
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
		}else{
			logger.debug("create: "+file.getAbsolutePath());
			FileTools.writeFile(file, "");
		}
	}
	
	private void save() throws Exception {
		logger.debug("save: "+file.getAbsolutePath());
		StringBuilder content = new StringBuilder();
		for(IBuild build : builds){
			content.append(build.getIdentifier()+"\n");
		}
		FileTools.writeFile(file, content.toString());
	}
}
