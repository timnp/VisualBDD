package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.*;

import view.MainGui;
import model.Formula;

/**
 * 
 * @author TimNP
 *
 */
public class FormulaController {
	/**
	 * the connected MainGui
	 */
	private MainGui mainGui;
	/**
	 * computed table for the toFormulaString method
	 */
	private static HashMap<String, String> formulaStringCT = new HashMap<String, String>();
	
	
	
	/**
	 * constructor for a FormulaController
	 */
	public FormulaController(MainGui mainGui) {
		// setting the MainGui
		this.mainGui = mainGui;
	}
	
	
	/**
	 * method that provides a Formula that is represented by a given String
	 * @param inputString - the String
	 * @return
	 */
	public static Formula stringToFormula(String inputString) {
		// converting the String into a "Formula String"
		String formulaString = toFormulaString(inputString);
		// creating a Formula from the Formula String
		return new Formula(formulaString);
	}
	
	
	/**
	 * method that provides the algorithm, that returns a Formula String for a 
	 * given String, after removing all spaces from the String and clearing the
	 * computed table
	 * @param inputString
	 * @return
	 */
	public static String toFormulaString(String inputString) {
		// removing all spaces from the String
		inputString = inputString.replaceAll("\\s", "");
		// clearing the computed table
		formulaStringCT.clear();
		// calling the actual (recursive) method
		return toFormulaStringRec(inputString);
	}
	
	
	/**
	 * Pattern for constants
	 */
	private static Pattern constantPattern = Pattern.compile("[-]?[01]");
	/**
	 * Pattern for variables
	 */
	private static Pattern variablePattern = Pattern.compile("[-]?[Xx]\\d+");
	/**
	 * Pattern for digits
	 */
	private static Pattern digitPattern = Pattern.compile("\\d");
	/**
	 * Matcher for the Patterns
	 */
	private static Matcher matcher;
	
	
	/**
	 * converts a given String into a "Formula String" (if possible)
	 * A "Formula String" is a String that a Formula can be created from.
	 * @param inputString
	 * @return a Formula String which is logically equivalent to the input 
	 * String
	 */
	private static String toFormulaStringRec(String inputString) {
		// If the String is parenthesized, the part in between has to be 
		// converted.
		if (isParenthesized(inputString)) {
			String inPars = 
					inputString.substring(1, inputString.length() - 1);
			// checking, whether the Formula String has been computed before
			if (formulaStringCT.containsKey(inPars)) {
				return formulaStringCT.get(inPars);
			} else {
				// computing the Formula String and putting it into the 
				// computed table
				String inParsFS = toFormulaStringRec(inPars);
				formulaStringCT.put(inPars, inParsFS);
				return inParsFS;
			}
		}
		// If the String represents a constant, it remains unchanged.
		matcher = constantPattern.matcher(inputString);
		if (matcher.matches()) {
			return inputString;
		}
		// If the String represents a variable, it remains unchanged.
		matcher = variablePattern.matcher(inputString);
		if (matcher.matches()) {
			return inputString;
		}
		// If the String represents a logical negation, its successor has to be
		// converted into a Formula String recursively.
		if (inputString.startsWith("-")) {
			// retrieving the negation's successor
			String suc = firstSuccessor(inputString.substring(1));
			// checking whether the negation is represented by the entire 
			// String
			if (suc.length() + 1 == inputString.length()) {
				// converting the negation's successor into a Formula String
				// (if it hasn't been before)
				String not = "-" + suc;
				if (formulaStringCT.containsKey(not)) {
					return formulaStringCT.get(not);
				} else {
					// computing the Formula String and putting it into the 
					// computed table
					String notFS = "-" + toFormulaStringRec(suc);
					formulaStringCT.put(not, notFS);
					return notFS;
				}
			}
		}
		// list of the String's binary operations (in order)
		LinkedList<String> binOps = new LinkedList<String>();
		// list for pairs of the successors of the String's binary operations 
		// and booleans that state, whether the successor already is an Formula
		// String (in order)
		LinkedList<String> successors = new LinkedList<String>();
		// The String is divided into binary operations and successors.
		while (!inputString.isEmpty()) {
			// If the first character of the String represents a binary 
			// operation, it is added to the binary operation list.
			if (inputString.startsWith("*") || inputString.startsWith("+")) {
				binOps.add(inputString.substring(0, 1));
				inputString = inputString.substring(1);
			}
			// If the String starts with a successor, the successor is added to
			// the successor list.
			else {
				String successor = firstSuccessor(inputString);
				if (!successor.equals("")) {
					successors.add(successor);
					inputString = inputString.substring(successor.length());
					// TODO user message
				} else return null;
			}
		}
		// variable for the first appearance of a logical conjunction in the 
		// binary operation list
		int firstAnd;
		// While there are binary operations, partial Formula Strings can be 
		// merged.
		while (!binOps.isEmpty()) {
			firstAnd = binOps.indexOf("*");
			// checking whether there is a logical conjunction among the binary
			// operations, because of its precedence over the logical 
			// disjunction
			if (firstAnd >= 0) {
				// removing the found logical conjunction from the binary 
				// operation list
				binOps.remove(firstAnd);
				// retrieving the first successor
				String firstSuc = successors.get(firstAnd);
				// first part of replacing the logical conjunction's successors
				// in the successor list by itself 
				successors.remove(firstAnd);
				// retrieving the second successor
				String secondSuc = successors.get(firstAnd);
				// second part of replacing the logical conjunction's 
				// successors in the successor list by itself
				successors.set(firstAnd, binOpFormulaString(firstSuc, "*", secondSuc));
			}
			// If there is no logical conjunction, the logical disjunctions are
			// merged.
			else {
				// removing the first logical disjunction from the binary 
				// operation list
				binOps.remove();
				// retrieving the first successor
				String firstSuc = successors.getFirst();
				// first part of replacing the logical disjunction's successors
				// in the successor list by itself 
				successors.remove();
				// retrieving the second successor
				String secondSuc = successors.getFirst();
				// second part of replacing the logical disjunction's 
				// successors in the successor list by itself
				successors.set(0, binOpFormulaString(firstSuc, "+", secondSuc));
			}
		}
		// If all successors have been merged into one Formula String, it can 
		// be returned.
		return successors.getFirst();
	}
	
	
	/**
	 * auxiliary method that states whether a given String is parenthesized
	 * @param inputString
	 * @return
	 */
	private static boolean isParenthesized(String inputString) {
		// index of the input String's last character
		int lastIndex = inputString.length() - 1;
		// The String may only be parenthesized, if it starts with a left 
		// parenthesis and ends with a right one.
		if (inputString.charAt(0) == '(' && 
				inputString.charAt(lastIndex) == ')') {
			// removing the first and the last parentheses from the String
			inputString = inputString.substring(1, lastIndex);
			// number that states, how many left parentheses that have to get a
			// right one have been found
			int leftPars = 1;
			// variable for the current character
			char currentChar;
			// going through the String between the two outer parentheses
			while (!inputString.isEmpty()) {
				currentChar = inputString.charAt(0);
				// increasing the number of left parentheses by one in case of 
				// a left parenthesis
				if (currentChar == '(') leftPars++;
				// decreasing the number of left parentheses by one in case of 
				// right parenthesis
				if (currentChar == ')') {
					leftPars--;
					// If the number of left parentheses is less than one, the 
					// first one has been closed and therefore the entire 
					// String isn't parenthesized.
					if (leftPars < 1) return false;
				}
				// removing the first character from the input String
				inputString = inputString.substring(1);
			}
			// If there is exactly one left parenthesis left to be closed, its 
			// the first one and the String is parenthesized. Otherwise the 
			// String isn't even a valid String to represent a Formula.
			return leftPars == 1;
		}
		// If the String doesn't start with a left parenthesis and end with a 
		// right one, it isn't parenthesized.
		else return false;
	}
	
	
	/**
	 * auxiliary method that retrieves the first successor from a String if the
	 * String starts with it; returns the empty String otherwise
	 * @param inputString
	 * @return
	 */
	private static String firstSuccessor(String inputString) {
		// If the String starts with a constant, that constant is returned.
		if (inputString.startsWith("0") || 
				inputString.startsWith("1")) {
			return inputString.substring(0, 1);
		}
		// If the String starts with a negated constant, the negated constant 
		// is returned.
		if (inputString.startsWith("-0") || 
				inputString.startsWith("-1")) {
			return inputString.substring(0, 2);
		}
		// If the String starts with a (possibly negated) variable, that 
		// variable is returned.
		String varSuc = variableSuccessor(inputString);
		if (!varSuc.equals("")) {
			return varSuc;
		}
		// If the String starts with a (possibly negated) parenthesized 
		// successor, that successor is returned.
		String parSuc = parenthesizedSuccessor(inputString);
		if (!parSuc.equals("")) {
			return parSuc;
		}
		// Otherwise the String doesn't start with a successor and the empty 
		// String is returned.
		else return "";
	}
	
	
	/**
	 * auxiliary method that retrieves the first (possibly negated) variable 
	 * successor from a String if the String starts with it; returns the empty 
	 * String otherwise
	 * @param inputString
	 * @return
	 */
	private static String variableSuccessor(String inputString) {
		String outputString = "";
		// If the String starts (possibly with a negation symbol and then) with
		// an X and a digit, it starts with a variable.
		if (inputString.startsWith("-")) {
			matcher = variablePattern.matcher(inputString.substring(0, 3));
		}
		else {
			matcher = variablePattern.matcher(inputString.substring(0, 2));
		}
		if (matcher.matches()) {
			// moving the first two characters of the input (the X and either 
			// the negation symbol or the first digit) String to the output 
			// String
			outputString = outputString.concat(inputString.substring(0, 2));
			inputString = inputString.substring(2);
			// As long as the following characters of the String are digits, 
			// they're part of the variable.
			if (!inputString.isEmpty()) {
				matcher = digitPattern.matcher(inputString.substring(0, 1));
				while (!inputString.isEmpty() && matcher.matches()) {
					matcher = 
							digitPattern.matcher(inputString.substring(0, 1));
					outputString = 
							outputString.concat(inputString.substring(0, 1));
					inputString = inputString.substring(1);
				}
			}
		}
		return outputString;
	}
	
	
	/**
	 * auxiliary method that retrieves the first (possibly negated) 
	 * parenthesized successor from a String if the String starts with it; 
	 * returns the empty String otherwise 
	 * @param inputString
	 * @return
	 */
	private static String parenthesizedSuccessor(String inputString) {
		// variable for the currently moved character
		String movedChar;
		// variable for inner parentheses
		// If it is greater than zero, a found right parenthesis doesn't belong
		// to the first one.
		int innerPars = 0;
		String outputString = "";
		// Only if the String starts with (possibly a negation symbol and) a 
		// left parenthesis, it may start with a parenthesized successor.
		if (inputString.startsWith("(") || inputString.startsWith("-(")) {
			if (inputString.startsWith("(")) {
				// moving the left parenthesis to the output String
				movedChar = inputString.substring(0 ,1);
				inputString = inputString.substring(1);
			} else {
				// moving the negation symbol and the left parenthesis to the 
				// output String
				movedChar = inputString.substring(0 ,2);
				inputString = inputString.substring(2);
			}
			outputString = outputString.concat(movedChar);
			// As long as the input String isn't empty, the first character of 
			// it is moved to the output String.
			while (!inputString.isEmpty()) {
				// moving the first character
				movedChar = inputString.substring(0, 1);
				inputString = inputString.substring(1);
				outputString = outputString.concat(movedChar);
				// If the moved character is a left parenthesis, the number of 
				// inner parentheses is increased by one.
				if (movedChar.equals("(")) {
					innerPars++;
				}
				else if (movedChar.equals(")")) {
					// If the moved character is a right parenthesis and the 
					// number of inner parentheses is greater than zero, it is 
					// decreased by one.
					if (innerPars > 0) {
						innerPars--;
					}
					// If the moved character is a right parenthesis and the 
					// number of inner parentheses isn't greater than zero (it 
					// is zero then), the output String is returned.
					else return outputString;
				}
			}
		}
		// returning the empty String if the String doesn't start with a 
		// parenthesized successor
		return "";
	}
	
	
	/**
	 * auxiliary method that provides the Formula String for a String that 
	 * represents a binary operation ("and" * or "or" +) and two Strings that 
	 * represent its successors
	 * @param firstSuc - the first successor
	 * @param binOp - the binary operation
	 * @param secondSuc - the second successor
	 * @return the Formula String
	 */
	private static String binOpFormulaString(String firstSuc, String binOp, String secondSuc) {
		// checking whether the Formula String for the entire binary operation 
		// String has been computed before
		String binOpF = firstSuc + binOp + secondSuc;
		String binOpFS;
		if (formulaStringCT.containsKey(binOpF)) binOpFS = formulaStringCT.get(binOpF);
		else {
			// checking whether the Formula String for the first 
			// successor has been computed before
			String firstSucFS;
			if (formulaStringCT.containsKey(firstSuc)) {
				firstSucFS = formulaStringCT.get(firstSuc);
			} else {
				// computing the Formula String and putting it into the
				// computed table
				firstSucFS  = toFormulaStringRec(firstSuc);
				formulaStringCT.put(firstSuc, firstSucFS);
			}
			// checking whether the Formula String for the second 
			// successor has been computed before
			String secondSucFS;
			if (formulaStringCT.containsKey(secondSuc)) {
				secondSucFS = formulaStringCT.get(secondSuc);
			} else {
				// computing the Formula String and putting it into the
				// computed table
				secondSucFS  = toFormulaStringRec(secondSuc);
				formulaStringCT.put(secondSuc, secondSucFS);
			}
			// creating the Formula String for the entire binary operation 
			// String and putting it into the computed table
			binOpFS = "(" + firstSucFS + binOp + secondSucFS + ")";
			formulaStringCT.put(binOpF, binOpFS);
		}
		// returning the Formula String for the binary operation String
		return binOpFS;
	}
	
}
