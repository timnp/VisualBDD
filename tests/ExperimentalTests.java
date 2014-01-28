package tests;

import java.util.LinkedList;
import java.util.regex.*;

public class ExperimentalTests {
	
	private Pattern constantPattern = Pattern.compile("[01]");
	private Pattern variablePattern = Pattern.compile("[Xx]\\d+");
	private Pattern digitPattern = Pattern.compile("\\d");
	private Matcher matcher;
	
	private String toFormulaString(String inputString) {
		// list of the String's binary operations (in order)
		LinkedList<String> binOps = new LinkedList<String>();
		// list for the successors of the String's binary operations (in order)
		LinkedList<String> successors = new LinkedList<String>();
		// If the String starts with a left parenthesis and ends with a right 
		// one, the part in them has to be converted.
		if (inputString.charAt(0) == '(' && 
				inputString.charAt(inputString.length() - 1) == ')') {
			return "(" + toFormulaString
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
		//TODO DO THINGS WITH OPS AND SUCS
		return null;
	}
	
	private String positiveSuccessor(String inputString) {
		if (inputString.startsWith("0") || 
				inputString.startsWith("1")) {
			return inputString.substring(0, 1);
		}
		String varSuc = variableSuccessor(inputString);
		matcher = variablePattern.matcher(varSuc);
		if (matcher.matches()) {
			return varSuc;
		}
		String parSuc = parenthesizedSuccessor(inputString);
		if (!parSuc.equals("")) {
			return parSuc;
		}
		// TODO
		else return "";
	}
	
	private String variableSuccessor(String inputString) {
		String outputString = "";
		matcher = variablePattern.matcher(inputString.substring(0, 2));
		if (matcher.matches()) {
			outputString.concat(inputString.substring(0, 1));
			inputString = inputString.substring(1);
			while (matcher.matches()) {
				outputString.concat(inputString.substring(0, 1));
				matcher = digitPattern.matcher(inputString.substring(0, 1));
			}
		}
		return outputString;
	}
	
	private String parenthesizedSuccessor(String inputString) {
		String movedChar;
		int innerPars = 0;
		String outputString = "";
		if (inputString.startsWith("(")) {
			outputString.concat(inputString.substring(0 ,1));
			inputString = inputString.substring(1);
			while (!inputString.isEmpty()) {
				movedChar = inputString.substring(0, 1);
				inputString = inputString.substring(1);
				outputString.concat(movedChar);
				if (movedChar.equals("(")) {
					innerPars++;
				}
				else if (movedChar.equals(")")) {
					if (innerPars > 0) {
						innerPars--;
					}
					else return outputString;
				}
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		
	}

}
