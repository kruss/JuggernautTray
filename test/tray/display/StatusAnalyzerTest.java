package tray.display;

import static org.junit.Assert.*;

import java.util.ArrayList;

import monitor.IBuild;
import monitor.MonitorInfo;
import monitor.IBuild.BuildStatus;

import org.junit.Test;

import tray.display.StatusAnalyzer.StatusChange;
import util.logger.ILogger;

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
		
		ArrayList<IBuild> newBuilds = new ArrayList<IBuild>();
		newBuilds.add(new BuildMock("build1", BuildStatus.OK));
		newBuilds.add(new BuildMock("build2", BuildStatus.ERROR));
		newBuilds.add(new BuildMock("build3", BuildStatus.UNKNOWN));
		
		StatusAnalyzer analyzer = new StatusAnalyzer(
				new MonitorInfo(), 
				new MonitorInfo(newBuilds)
		);
		
		ArrayList<StatusChange> changes = analyzer.getChanges();
		assertEquals(3, changes.size());
		
		assertEquals("build1", changes.get(0).identifier);
		assertEquals(null, changes.get(0).oldStatus);
		assertEquals(BuildStatus.OK, changes.get(0).newStatus);
		
		assertEquals("build2", changes.get(1).identifier);
		assertEquals(null, changes.get(1).oldStatus);
		assertEquals(BuildStatus.ERROR, changes.get(1).newStatus);
		
		assertEquals("build3", changes.get(2).identifier);
		assertEquals(null, changes.get(2).oldStatus);
		assertEquals(BuildStatus.UNKNOWN, changes.get(2).newStatus);
	}
	
	@Test public void testNewAndOldBuilds(){
		
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
		
		assertEquals("build2", changes.get(0).identifier);
		assertEquals(BuildStatus.OK, changes.get(0).oldStatus);
		assertEquals(BuildStatus.ERROR, changes.get(0).newStatus);
		
		assertEquals("build4", changes.get(1).identifier);
		assertEquals(null, changes.get(1).oldStatus);
		assertEquals(BuildStatus.OK, changes.get(1).newStatus);
	}
	
	private class BuildMock implements IBuild {

		private String identifier;
		private BuildStatus status;
		
		public BuildMock(String identifier, BuildStatus status) {
			this.identifier = identifier;
			this.status = status;
		}
		
		@Override
		public String getIdentifier(){ return identifier; }
		@Override
		public String getName(){ return identifier+"-build-name"; }
		@Override
		public String getBuildUrl(){ return identifier+"-build-url"; }
		@Override
		public String getServerUrl(){ return identifier+"-server-url"; }
		@Override
		public BuildStatus getStatus(){ return status; }
		@Override
		public void updateBuild(ILogger logger) throws Exception {}
		@Override
		public int compareTo(IBuild arg0) { return 0; }
	}
}
