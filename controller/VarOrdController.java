package controller;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import view.MainGui;
import model.VariableOrdering;

/**
 * 
 * @author TimNP
 *
 */
public class VarOrdController {
	/**
	 * the connected MainGui
	 */
	private MainGui mainGui;
	
	
	
	/**
	 * constructor for a VarOrdController
	 * @param mainGui
	 */
	public VarOrdController(MainGui mainGui) {
		// setting the MainGui
		this.mainGui = mainGui;
	}
	
	
	/**
	 * method that provides a VariableOrdering that is represented by a given 
	 * String
	 * @param inputString - the String
	 * @return
	 */
	public VariableOrdering stringToVarOrd(String inputString) {
		// creating a new VariableOrdering from the ordering list the input 
		// String represents
		return new VariableOrdering(stringToOrdList(inputString));
	}
	
	
	/**
	 * method that provides the algorithm, that returns an ordering list for a 
	 * given String, after removing all spaces from the String
	 * @param inputString
	 * @return
	 */
	private LinkedList<Integer> stringToOrdList(String inputString) {
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
	private LinkedList<Integer> stringToOrdListRec(String inputString, 
			LinkedList<Integer> partialOrdList) {
		// If the input String is empty, the given ordering list gets returned.
		if (inputString.isEmpty()) return partialOrdList;
		// isolating the input String's first character
		char firstChar = inputString.charAt(0);
		// If the String's first character is an X, it's considered to be the 
		// beginning of a variable.
		if (firstChar == 'X' || firstChar == 'x') {
			// retrieving the starting variable's number
			String varNoString = startingVarNoString(inputString);
			// If there is no number after the X, the String isn't valid.
			// TODO user message
			if (varNoString.equals(null)) return null;
			// adding the variable number to the ordering list
			partialOrdList.add(Integer.parseInt(varNoString));
			// recursively adding the rest of the input String
			return stringToOrdListRec(inputString.
					substring(varNoString.length()), partialOrdList);
		}
		// If the String's first character is a ',' or a '<', it's considered 
		// to be a separator between two variables.
		else if (firstChar == ',' || firstChar == '<') {
			// recursively adding the rest of the input String
			return stringToOrdListRec(inputString.substring(1), 
					partialOrdList);
		}
		// Otherwise the String isn't valid
		// TODO user message
		else return null;
	}
	
	
	/**
	 * Pattern String for a digit
	 */
	private String digitPatternString = "\\d";
	
	
	/**
	 * Pattern for a digit
	 */
	private Pattern digitPattern = Pattern.compile(digitPatternString);
	
	
	/**
	 * Matcher for the Pattern
	 */
	private Matcher matcher;
	
	
	/**
	 * auxiliary method that retrieves the variable number of a given String's 
	 * starting variable
	 * @param inputString
	 * @return the variable number as a String; null if there is none
	 */
	private String startingVarNoString(String inputString) {
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
			// retrieving the next character
			firstChar = inputString.substring(0, 1);
			// matching to the new character
			matcher = digitPattern.matcher(firstChar);
		}
		// If the variable String is empty, null gets returned.
		if (varString.equals("")) return null;
		// Otherwise the variable number gets returned.
		else return varString;
	}
}
