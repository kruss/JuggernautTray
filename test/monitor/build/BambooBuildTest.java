package monitor.build;

import static org.junit.Assert.*;

import java.io.File;

import monitor.build.IBuild.BuildStatus;

import org.junit.Test;

import util.FileTools;

public class BambooBuildTest {

	@Test public void testBuildUrl(){
		try{
			String type = "type";
			String name = "name";
			String server = "server";
			
			BuildMeta meta = new BuildMeta(type, name, server);
			BambooBuild build = new BambooBuild(meta);
			
			assertEquals("http://"+server+"/allPlans.action", build.getServerUrl());
			assertEquals("http://"+server+"/browse/"+name, build.getBuildUrl());
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	@Test public void testBuildStatus(){
		try{
			BambooBuild build = new BambooBuild(
					new BuildMeta("type", "name", "server")
			);
			assertEquals(BuildStatus.UNKNOWN, build.status);
			
			for(BuildStatus status : BuildStatus.values()){
				build.parseStatus(
						FileTools.readFile(
								new File("test/monitor/build/bamboo/"+status.toString().toLowerCase()+".htm")
						)
				);
				assertEquals(status, build.status);
			}
			
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
}
