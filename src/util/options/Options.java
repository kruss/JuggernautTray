package util.options;

public class Options implements IOptions {

	public static final String OPTION_PREFIX = "-";
	
	private String[] args;
	
	public Options(String[] args) {
		this.args = args;
	}

	@Override
	public boolean isVerboseMode() {
		return hasOption("v");
	}

	private boolean hasOption(String option) {
		for(String arg : args){
			if(arg.equals(OPTION_PREFIX+option)){
				return true;
			}
		}
		return false;
	}
}
