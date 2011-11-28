package util;

public class StringTools {

	private static final int MAX_EXCEPTION_TRACE = 25;
	
	public static String trace(Exception e){
		return trace(e, MAX_EXCEPTION_TRACE);
	}
	
	public static String trace(Exception e, int depth){

		StringBuilder trace = new StringBuilder();
		trace.append("["+e.getClass().getSimpleName()+"] "+e.getMessage());
		StackTraceElement[] stack = e.getStackTrace();
		int length = stack.length > depth ? depth : stack.length;
		for(int i=0; i<length; i++){
			if(stack[i].getLineNumber() > 0){
				trace.append("\n at "+stack[i].getClassName()+"::"+stack[i].getMethodName()+" ("+stack[i].getLineNumber()+")");
			}else{
				trace.append("\n at "+stack[i].getClassName()+"::"+stack[i].getMethodName());
			}
		}
		if(length < stack.length){ trace.append("\n ..."); }
		return trace.toString();
	}
}
