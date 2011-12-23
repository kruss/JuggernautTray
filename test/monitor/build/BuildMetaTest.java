package monitor.build;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuildMetaTest {

	@Test public void testMetaFromIdentifier(){
		
		try{
			String type = "type";
			String name = "name";
			String server = "server";
			String identifier = type+BuildMeta.SEP1+name+BuildMeta.SEP2+server;
			BuildMeta meta = new BuildMeta(identifier);
			assertEquals(identifier, meta.identifier);
			assertEquals(type, meta.type);
			assertEquals(name, meta.name);
			assertEquals(server, meta.server);
		}catch(Exception e){
			fail(e.getMessage());
		}

		String[] identifiers = {
				BuildMeta.SEP2+"name"+BuildMeta.SEP1+"server", 
				"type"+BuildMeta.SEP1+BuildMeta.SEP2+"server", 
				"type"+BuildMeta.SEP1+"name"+BuildMeta.SEP2, 
				"type name"+BuildMeta.SEP2+"server",
				"type"+BuildMeta.SEP1+"name server",
				"type name server",
				"",
				null
		};
		for(String identifier : identifiers){	
			try{
				new BuildMeta(identifier);
				fail("did'nt fail on: '"+identifier+"'");
			}catch(Exception e){ /* NOTHING */ }
		}
	}
	
	@Test public void testIdentifierFromMeta(){
		try{
			String type = "type";
			String name = "name";
			String server = "server";
			String identifier = type+BuildMeta.SEP1+name+BuildMeta.SEP2+server;
			BuildMeta meta = new BuildMeta(type, name, server);
			assertEquals(identifier, meta.identifier);
			assertEquals(type, meta.type);
			assertEquals(name, meta.name);
			assertEquals(server, meta.server);
		}catch(Exception e){
			fail(e.getMessage());
		}
		
		String[][] metas = {
			{ "", "name", "server" },
			{ "type", "", "server" },
			{ "type", "name", "" },
			{ "", "", "" },
			{ null, null, null }
		};
		for(String[] meta : metas){
			try{
				new BuildMeta(meta[0], meta[1], meta[2]);
				fail("did'nt fail on: '"+meta[0]+"' '"+meta[1]+"' '"+meta[2]+"'");
			}catch(Exception e){ /* NOTHING */ }
		}
	}
}
