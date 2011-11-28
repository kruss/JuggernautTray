package util;

import java.util.ArrayList;

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
		int input = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
		if(input == JOptionPane.YES_OPTION){
			return true;
		}else{
			return false;
		}
	}
	
	public static String optionDialog(String message, ArrayList<String> values) {
		String input = (String)JOptionPane.showInputDialog(
				null, message, "Select",
				JOptionPane.QUESTION_MESSAGE, null, 
				values.toArray(new String[0]), null
		);
		return input;
	}
}
