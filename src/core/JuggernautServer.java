package core;

import java.util.ArrayList;

import core.BuildMonitor.Build;

public class JuggernautServer {

	private Logger logger;
	
	public JuggernautServer(Logger logger) {
		this.logger = logger;
	}

	public ArrayList<Build> getBuilds(String url, ArrayList<String> filter) {
		logger.log("download: "+url);
		ArrayList<Build> builds = new ArrayList<Build>();
		
		// TODO Auto-generated method stub
		return builds;
	}
}
