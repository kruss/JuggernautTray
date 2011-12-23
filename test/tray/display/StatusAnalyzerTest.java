package tray.display;

import static org.junit.Assert.*;

import java.util.ArrayList;

import monitor.MonitorInfo;
import monitor.build.AbstractBuild;
import monitor.build.BuildMeta;
import monitor.build.IBuild;
import monitor.build.IBuild.BuildStatus;

import org.junit.Test;

import tray.display.StatusAnalyzer.StatusChange;

public class StatusAnalyzerTest {

	@Test public void testNoBuilds(){
		
		StatusAnalyzer analyzer = new StatusAnalyzer(
				new MonitorInfo(), 
				new MonitorInfo()
		);
		
		ArrayList<StatusChange> changes = analyzer.getChanges();
		assertEquals(0, changes.size());
	}
	
	@Test public void testOnlyNewBuilds(){
		try{
			ArrayList<IBuild> builds = new ArrayList<IBuild>();
			builds.add(new BuildMock("build1", BuildStatus.OK));
			builds.add(new BuildMock("build2", BuildStatus.ERROR));
			builds.add(new BuildMock("build3", BuildStatus.UNKNOWN));
			
			StatusAnalyzer analyzer = new StatusAnalyzer(
					new MonitorInfo(), 
					new MonitorInfo(builds)
			);
			
			ArrayList<StatusChange> changes = analyzer.getChanges();
			assertEquals(3, changes.size());
			
			assertEquals(builds.get(0).getMeta().identifier, changes.get(0).identifier);
			assertEquals(null, changes.get(0).oldStatus);
			assertEquals(BuildStatus.OK, changes.get(0).newStatus);
			
			assertEquals(builds.get(1).getMeta().identifier, changes.get(1).identifier);
			assertEquals(null, changes.get(1).oldStatus);
			assertEquals(BuildStatus.ERROR, changes.get(1).newStatus);
			
			assertEquals(builds.get(2).getMeta().identifier, changes.get(2).identifier);
			assertEquals(null, changes.get(2).oldStatus);
			assertEquals(BuildStatus.UNKNOWN, changes.get(2).newStatus);
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	@Test public void testNewAndOldBuilds(){
		try{
			ArrayList<IBuild> oldBuilds = new ArrayList<IBuild>();
			oldBuilds.add(new BuildMock("build1", BuildStatus.OK));
			oldBuilds.add(new BuildMock("build2", BuildStatus.OK));
			oldBuilds.add(new BuildMock("build3", BuildStatus.UNKNOWN));
			
			ArrayList<IBuild> newBuilds = new ArrayList<IBuild>();
			newBuilds.add(new BuildMock("build2", BuildStatus.ERROR));
			newBuilds.add(new BuildMock("build3", BuildStatus.UNKNOWN));
			newBuilds.add(new BuildMock("build4", BuildStatus.OK));
			
			StatusAnalyzer analyzer = new StatusAnalyzer(
					new MonitorInfo(oldBuilds), 
					new MonitorInfo(newBuilds)
			);
			
			ArrayList<StatusChange> changes = analyzer.getChanges();
			assertEquals(2, changes.size());
			
			assertEquals(newBuilds.get(0).getMeta().identifier, changes.get(0).identifier);
			assertEquals(BuildStatus.OK, changes.get(0).oldStatus);
			assertEquals(BuildStatus.ERROR, changes.get(0).newStatus);
			
			assertEquals(newBuilds.get(2).getMeta().identifier, changes.get(1).identifier);
			assertEquals(null, changes.get(1).oldStatus);
			assertEquals(BuildStatus.OK, changes.get(1).newStatus);
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	private class BuildMock extends AbstractBuild {

		public BuildMock(String name, BuildStatus status) throws Exception {
			super(new BuildMeta(name+"-type", name+"-name", name+"-server"));
			this.status = status;
		}

		@Override
		public String getBuildUrl() {
			return getServerUrl()+"/"+meta.name;
		}

		@Override
		public void updateStatus(String content) throws Exception {}
	}
}
