package monitor.build;

import static org.junit.Assert.*;

import java.io.File;

import monitor.build.IBuild.BuildStatus;

import org.junit.Test;

import util.FileTools;

public class JuggernautBuildTest {

	@Test public void testBuildUrl(){
		try{
			String type = "type";
			String name = "name";
			String server = "server";
			
			BuildMeta meta = new BuildMeta(type, name, server);
			JuggernautBuild build = new JuggernautBuild(meta);
			
			assertEquals("http://"+server, build.getServerUrl());
			assertEquals("http://"+server+"/index["+name.hashCode()+"].htm", build.getBuildUrl());
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	@Test public void testBuildStatus(){
		try{
			JuggernautBuild build = new JuggernautBuild(
					new BuildMeta("type", "name", "server")
			);
			assertEquals(BuildStatus.UNKNOWN, build.status);
			
			for(BuildStatus status : BuildStatus.values()){
				build.parseStatus(
						FileTools.readFile(
								new File("test/monitor/build/juggernaut/"+status.toString().toLowerCase()+".htm")
						)
				);
				assertEquals(status, build.status);
			}
			
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
}
