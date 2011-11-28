package util;

import javax.swing.JOptionPane;

public class UiTools {

	public static String inputDialog(String message){ 
		String input = JOptionPane.showInputDialog(message);
		if(input != null){
			return input.trim();
		}else{
			return null;
		}
	}
	
	public static String inputDialog(String message, String value){ 
		String input = JOptionPane.showInputDialog(message, value);
		if(input != null){
			return input.trim();
		}else{
			return null;
		}
	}
	
	public static boolean confirmDialog(String message){
		int input = JOptionPane.showConfirmDialog(null, message);
		if(input == JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
}
