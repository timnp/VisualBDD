package controller;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.VariableOrdering;

/**
 * 
 * @author TimNP
 *
 */
public class VarOrdController {
	/**
	 * constructor for a VarOrdController
	 */
	public VarOrdController() {
		//TODO?
	}
	
	
	/**
	 * method that provides a VariableOrdering that is represented by a given 
	 * String
	 * @param inputString - the String
	 * @return
	 */
	public static VariableOrdering stringToVarOrd(String inputString) {
		// creating the ordering list the input string represents
		LinkedList<Integer> ordList = stringToOrdList(inputString);
		// If the ordering list couldn't be created properly, null is returned.
		if (ordList == null) return null;
		// Otherwise the variable ordering is created.
		else return new VariableOrdering(ordList);
	}
	
	
	/**
	 * provides the algorithm that returns an ordering list for a given String 
	 * after removing all spaces from the String
	 * @param inputString
	 * @return
	 */
	public static LinkedList<Integer> stringToOrdList(String inputString) {
		// removing all spaces from the String
		inputString = inputString.replaceAll("\\s", "");
		// calling the actual (recursive) method
		return stringToOrdListRec(inputString, new LinkedList<Integer>());
	}
	
	
	/**
	 * method that adds all variable numbers from a given String representing a
	 * VariableOrdering to an ordering (integer) list
	 * @param inputString
	 * @param partialOrdList
	 * @return
	 */
	private static LinkedList<Integer> stringToOrdListRec(String inputString, 
			LinkedList<Integer> partialOrdList) {
		if (inputString.isEmpty()) {
			// If the input String is empty and the partial ordering list isn't, 
			// the given ordering list gets returned.
			if (!partialOrdList.isEmpty()) return partialOrdList;
			// If both are empty, null is returned.
			else return null;
		}
		// isolating the input String's first character
		char firstChar = inputString.charAt(0);
		// If the String's first character is an X, it's considered to be the 
		// beginning of a variable.
		if (firstChar == 'X' || firstChar == 'x') {
			// retrieving the starting variable's number
			String varNoString = startingVarNoString(inputString);
			// If there is no number after the X, the String isn't valid.
			if (varNoString.equals("")) return null;
			// adding the variable number to the ordering list
			partialOrdList.add(Integer.parseInt(varNoString));
			// removing the variable number from the input string
			inputString = inputString.substring(varNoString.length());
			// recursively adding the rest of the input String
			return stringToOrdListRec(inputString.
					substring(varNoString.length()), partialOrdList);
		}
		// If the String's first character is a ',' or a '>', it's considered 
		// to be a separator between two variables.
		else if (firstChar == ',' || firstChar == '>') {
			// recursively adding the rest of the input String
			return stringToOrdListRec(inputString.substring(1), 
					partialOrdList);
		}
		// Otherwise the String isn't valid
		else return null;
	}
	
	
	/**
	 * Pattern String for a digit
	 */
	private static String digitPatternString = "\\d";
	
	
	/**
	 * Pattern for a digit
	 */
	private static Pattern digitPattern = Pattern.compile(digitPatternString);
	
	
	/**
	 * Matcher for the Pattern
	 */
	private static Matcher matcher;
	
	
	/**
	 * auxiliary method that retrieves the variable number of a given String's 
	 * starting variable
	 * @param inputString
	 * @return the variable number as a String; empty string if there is none
	 */
	private static String startingVarNoString(String inputString) {
		// initializing the variable String
		String varString = "";
		// cutting the starting X from the start of the String
		inputString = inputString.substring(1);
		// retrieving the input String's first character as a String
		String firstChar = inputString.substring(0,1);
		// matching the character to the digit pattern
		matcher = digitPattern.matcher(firstChar);
		// As long as there are digits at the  String's beginning, they're part
		// of the variable number.
		while (!inputString.isEmpty() && matcher.matches()) {
			// adding the digit to the variable String
			varString = varString.concat(firstChar);
			// cutting the digit off the input String
			inputString = inputString.substring(1);
			// If the remaining input string is empty, the return happens.
			if (inputString.isEmpty()) return varString;
			// retrieving the next character
			firstChar = inputString.substring(0, 1);
			// matching to the new character
			matcher = digitPattern.matcher(firstChar);
		}
		// returning the variable number string
		return varString;
	}
}
