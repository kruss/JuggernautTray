package monitor.build;

public class BuildMeta {

	public static final String SEP1 = "::";
	public static final String SEP2 = "@";
	
	public String identifier;
	public String type;
	public String name; 
	public String server; 
	
	public BuildMeta(String type, String name, String server) throws Exception {
		identifier = type+SEP1+name+SEP2+server;
		if(!name.isEmpty() && !server.isEmpty() && !type.isEmpty()){
			this.name = name;
			this.server = server;
			this.type = type;
			return;
		}
		throw new Exception("invalid format: "+identifier);
	}
	
	public BuildMeta(String identifier) throws Exception {
		this.identifier = identifier;
		String[] split = identifier.split(SEP1);
		if(split.length == 2 && !split[0].isEmpty() && !split[1].isEmpty()){
			type = split[0];
			split = split[1].split(SEP2);
			if(split.length == 2 && !split[0].isEmpty() && !split[1].isEmpty()){
				name = split[0];
				server = split[1];
				return;
			}
		}
		throw new Exception("invalid format: "+identifier);
	}
}
