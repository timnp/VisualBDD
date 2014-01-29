package tests;

import java.util.LinkedList;
import java.util.regex.*;

import model.Formula;

public class ExperimentalTests {
	
	private static Pattern constantPattern = Pattern.compile("[01]");
	private static Pattern variablePattern = Pattern.compile("[Xx]\\d+");
	private static Pattern digitPattern = Pattern.compile("\\d");
	private static Matcher matcher;
	
	/**
	 * 
	 * @param inputString
	 * @return
	 */
	public static String toFormulaString(String inputString) {
		// removing all spaces from the String
		inputString = inputString.replaceAll("\\s", "");
		// calling the actual recursive method
		return toFormulaStringRec(inputString);
	}
	
	
	/**
	 * converts a given String into a "Formula String" (if possible)
	 * A "Formula String" is a String that a Formula can be created from.
	 * @param inputString
	 * @return a Formula String which is logically equivalent to the input 
	 * String
	 */
	private static String toFormulaStringRec(String inputString) {
		// list of the String's binary operations (in order)
		LinkedList<String> binOps = new LinkedList<String>();
		// list for the successors of the String's binary operations (in order)
		LinkedList<String> successors = new LinkedList<String>();
		// If the String starts with a left parenthesis and ends with a right 
		// one, the part in them has to be converted.
		if (inputString.charAt(0) == '(' && 
				inputString.charAt(inputString.length() - 1) == ')') {
			return "(" + toFormulaStringRec
					(inputString.substring(1, inputString.length() - 1)) + ")";
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
		// Otherwise the String is divided into binary operations and 
		// successors.
		while (!inputString.isEmpty()) {
			// If the first character of the String represents a binary 
			// operation, it is added to the binary operation list.
			if (inputString.startsWith("*") || inputString.startsWith("+")) {
				binOps.add(inputString.substring(0, 1));
				inputString = inputString.substring(1);
			}
			// If the String starts with a positive successor, the successor is
			// added to the successor list.
			else {
				String posSuc = positiveSuccessor(inputString);
				if (!posSuc.equals("")) {
					successors.add(posSuc);
					inputString = inputString.substring(posSuc.length());
				}
				// If the String starts with a negated successor, the successor
				// is added to the successor list.
				else if (inputString.startsWith("-")) {
					String subSuc = 
							positiveSuccessor(inputString.substring(1));
					if (!subSuc.equals("")) {
						successors.add("-".concat(subSuc));
						inputString = 
								inputString.substring(subSuc.length() + 1);
					}
					// Otherwise the String can't be converted into a Formula 
					// String.
					// TODO
					else return null;
				}
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
				// turning the Strings representing the logical conjunction's 
				// successors into Formula Strings
				String firstSuc = toFormulaStringRec(successors.get(firstAnd));
				// first part of replacing the logical conjunction's successors
				// in the successor list by itself 
				successors.remove(firstAnd);
				String secondSuc = toFormulaStringRec(successors.get(firstAnd));
				// creating the Formula String for the logical conjunction
				String and = firstSuc + "*" + secondSuc;
				// second part of replacing the logical conjunction's 
				// successors in the successor list by itself
				successors.set(firstAnd, and);
			}
			// If there is no logical conjunction, the logical disjunctions are
			// merged.
			else {
				// removing the first logical disjunction from the binary 
				// operation list
				binOps.remove();
				// turning the Strings representing the logical disjunction's 
				// successors into Formula Strings
				String firstSuc = toFormulaStringRec(successors.getFirst());
				// first part of replacing the logical disjunction's successors
				// in the successor list by itself
				successors.remove();
				String secondSuc = toFormulaStringRec(successors.getFirst());
				// creating the Formula String for the logical disjunction
				String or = firstSuc + "+" + secondSuc;
				// second part of replacing the logical disjunction's 
				// successors in the successor list by itself
				successors.set(0, or);
			}
		}
		// If all successors have been merged into one Formula String, it can 
		// be returned.
		return successors.getFirst();
	}
	
	/**
	 * auxiliary method that retrieves the first positive successor from a 
	 * String if the String starts with it; returns the empty String otherwise
	 * @param inputString
	 * @return
	 */
	private static String positiveSuccessor(String inputString) {
		// If the String starts with a 0 or a 1 (thus a constant), that 
		// constant is returned.
		if (inputString.startsWith("0") || 
				inputString.startsWith("1")) {
			return inputString.substring(0, 1);
		}
		// If the String starts with a variable, that variable is returned.
		String varSuc = variableSuccessor(inputString);
		if (!varSuc.equals("")) {
			return varSuc;
		}
		// If the String starts with a parenthesized successor, that successor 
		// is returned.
		String parSuc = parenthesizedSuccessor(inputString);
		if (!parSuc.equals("")) {
			return parSuc;
		}
		// Otherwise the String doesn't start with a successor and the empty 
		// String is returned.
		else return "";
	}
	
	/**
	 * auxiliary method that retrieves the first variable successor from a 
	 * String if the String starts with it; returns the empty String otherwise
	 * @param inputString
	 * @return
	 */
	private static String variableSuccessor(String inputString) {
		String outputString = "";
		// If the String starts with an X and a digit, it starts with a 
		// variable.
		matcher = variablePattern.matcher(inputString.substring(0, 2));
		if (matcher.matches()) {
			// moving the X and the found digit to the output String
			outputString.concat(inputString.substring(0, 2));
			inputString = inputString.substring(2);
			// As long as the following characters of the String are digits, 
			// they're part of the variable.
			matcher = digitPattern.matcher(inputString.substring(0, 1));
			while (matcher.matches()) {
				outputString.concat(inputString.substring(0, 1));
				matcher = digitPattern.matcher(inputString.substring(0, 1));
			}
		}
		return outputString;
	}
	
	/**
	 * auxiliary method that retrieves the first parenthesized successor from a
	 * String if the String starts with it; returns the empty String otherwise 
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
		// Only if the String starts with a left parenthesis, it may start with
		// a parenthesized successor.
		if (inputString.startsWith("(")) {
			// moving the left parenthesis to the output String
			movedChar = inputString.substring(0 ,1);
			inputString = inputString.substring(1);
			outputString.concat(movedChar);
			// As long as the input String isn't empty, the first character of 
			// it is moved to the output String.
			while (!inputString.isEmpty()) {
				// moving the first character
				movedChar = inputString.substring(0, 1);
				inputString = inputString.substring(1);
				outputString.concat(movedChar);
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
	
	public static void main(String[] args) {
		String f_string = "x2 + x1 *  -(X2 * x1) * -x3";
		String f_fstring = toFormulaString(f_string);
		Formula f = new Formula(f_fstring);
		String f_fstring2 = f.toString();
		System.out.println(f_fstring2);
	}

}
