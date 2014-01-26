package controller;

import java.util.regex.*;

import model.Formula;

/**
 * 
 * @author TimNP
 *
 */
public class FormulaController {
	/**
	 * 
	 * @param inputString
	 * @return
	 */
	public static Formula stringToFormula(String inputString) {
		String formulaString = toFormulaString(inputString, false);
		return new Formula(formulaString);
	}
	
	
	/**
	 * Pattern String for parentheses
	 */
	private static String parenthesesPatternString =
			// possible spaces
			"\\s*"
			// the left parenthesis
			+ "[(]"
			// group: the String in the parentheses
			+ "(.*)"
			// the right parenthesis
			+ "[)]"
			// possible spaces
			+ "\\s*";
	/**
	 * Pattern for parentheses
	 */
	private static Pattern parenthesesPattern = 
			Pattern.compile(parenthesesPatternString);
	
	/**
	 * Pattern String for constants
	 */
	private static String constantPatternString =
			// possible spaces
			"\\s*"
			// group: the constant, either 1 or 0
			+ "([01])"
			// possible spaces
			+ "\\s*";
	/**
	 * Pattern for constants
	 */
	private static Pattern constantPattern = 
			Pattern.compile(constantPatternString);
	
	/**
	 * Pattern String for variables
	 */
	private static String variablePatternString = 
			// possible spaces
			"\\s*"
			// group: the variable consisting of an X and a number
			+ "([Xx]\\d+)"
			// possible spaces
			+ "\\s*";
	/**
	 * Pattern for variables
	 */
	private static Pattern variablePattern = Pattern.compile(variablePatternString);

	/**
	 * String for simple successors in Patterns
	 * A simple successor is either completely parenthesized, or a variable, 
	 * or a constant. In each case it may be negated.
	 */
	private static String simpleSuccessor =
			// possible spaces
			"\\s*"
			// possible negation
			+ "[-]?"
			// possible spaces
			+ "[" 	
					// possibility: completely parenthesized
					+ "[[(].*[)]]"
					// possibility: variable
					+ "|[[Xx]\\d+]" 
					// possibility: constant
					+ "|[01]"+
			"]"
			// possible spaces
			+ "\\s*";
	
	/**
	 * Pattern String for a logical negation
	 */
	private static String notPatternString =
			// possible spaces
			"\\s*"
			// the negation symbol
			+ "-"
			// group: the successor
			// A negation's successor is either completely parenthesized or a  
			// variable or a constant.
			+ "(" + simpleSuccessor + ")";
	/**
	 * Pattern for a logical negation
	 */
	private static Pattern notPattern = Pattern.compile(notPatternString);
	
	/**
	 * Pattern String for a logical conjunction
	 */
	private static String andPatternString =
			// group 1: the first successor
			"(" + simpleSuccessor + ")"
			// the conjunction symbol
			+ "*"
			// group 2: the second successor
			// The second successor may not be a simple one but a sequence of 
			// simple ones combined by conjunction symbols.
			+ "(" + simpleSuccessor + "[[*]" + simpleSuccessor + "]*)";
	/**
	 * Pattern for a logical conjunction
	 */
	private static Pattern andPattern = Pattern.compile(andPatternString);
	
	/**
	 * Pattern for a logical disjunction
	 */
	private static String orPatternString = 
			// group 1: the first successor
			// The second successor may not be a simple one but a sequence of 
			// simple ones combined by conjunction symbols.
			"(" + simpleSuccessor + "[[*]" + simpleSuccessor + "]*)"
			// the disjunction symbol
			+ "+"
			// group 2: the second successor
			// The second successor may not be a simple one but a sequence of 
			// simple ones combined by conjunction or disjunction symbols.
			+ "(" + simpleSuccessor + "[[*+]" + simpleSuccessor + "]*)";
	/**
	 * Pattern for a logical disjunction
	 */
	private static Pattern orPattern = Pattern.compile(orPatternString);
	
	private static Matcher matcher;
	
	
	private static String toFormulaString(String inputString, boolean isInPars) {
		// matching the input String to the parentheses Pattern
		matcher = parenthesesPattern.matcher(inputString);
		System.out.println("matched to parentheses: " + inputString);
		if (matcher.matches()) {
			System.out.println("has parentheses");
			// If the String starts with a left parenthesis and ends with a 
			// right one, the part between is in parentheses.
			System.out.println("string in parentheses: " + matcher.group(1));
			return "(" + toFormulaString(matcher.group(1), true) + ")";
		}
		// matching the input String to the constant Pattern
		matcher = constantPattern.matcher(inputString);
		System.out.println("matched to constant: " + inputString);
		if (matcher.matches()) {
			System.out.println("is constant");
			// retrieving the constant itself
			String constant = matcher.group(1);
			System.out.println("constant string: " + constant);
			// putting the constant in parentheses if it isn't already
			return inParentheses(constant, isInPars);
		}
		// matching the input String to the variable Pattern
		matcher = variablePattern.matcher(inputString);
		System.out.println("matched to variable: " + inputString);
		if (matcher.matches()) {
			System.out.println("os variable");
			// retrieving the variable itself
			String variable = matcher.group(1);
			System.out.println("variable string: " + variable);
			// putting the variable in parentheses if it isn't already
			return inParentheses(variable, isInPars);
		}
		// matching the input String to the logical negation Pattern
		matcher = notPattern.matcher(inputString);
		System.out.println("matched to negation: " + inputString);
		if (matcher.matches()) {
			System.out.println("is negation");
			// the negation consisting of the negation symbol and the successor
			// (also turned into a formula String)
			String negation = "-" + toFormulaString(matcher.group(1), false);
			System.out.println("negation string: " + negation);
			// putting the negation in parentheses if it isn't already
			return inParentheses(negation, isInPars);
		}
		// matching the input String to the logical disjunction Pattern
		matcher = orPattern.matcher(inputString);
		System.out.println("matched to disjunction: " + inputString);
		if (matcher.matches()) {
			System.out.println("is disjunction");
			// the disjunction consisting of its two successors (also turned 
			// into formula Strings) combined by the disjunction symbol
			String disjunction = toFormulaString(matcher.group(1), false) + "+"
					+ toFormulaString(matcher.group(2), false);
			System.out.println("disjunction string: " + disjunction);
			// putting the disjunction in parentheses if it isn't already
			return inParentheses(disjunction, isInPars);
		}
		// matching the input String to the logical conjunction Pattern
		matcher = andPattern.matcher(inputString);
		System.out.println("matched to conjunction: " + inputString);
		if (matcher.matches()) {
			System.out.println("is conjunction");
			// the conjunction consisting of its two successors (also turned 
			// into formula Strings) combined by the conjunction symbol
			String conjunction = toFormulaString(matcher.group(1), false) + "*"
					+ toFormulaString(matcher.group(2), false);
			System.out.println("conjunction string: " + conjunction);
			// putting the conjunction in parentheses if it isn't already
			return inParentheses(conjunction, isInPars);
		}
		// If none of the given Patterns matches the input String 
		// (possibly because it's empty), the empty String is returned.
		System.out.println("mismatched '" + inputString + "' everywhere, empty String returned");
		return "";
	}
	
	/**
	 * auxiliary function that puts a String in parentheses if it isn't already
	 * @param inputString - the string
	 * @param isInPars - states, whether the String is in parentheses
	 * @return the string in parentheses
	 */
	private static String inParentheses(String inputString, boolean isInPars) {
		if (isInPars) {
			return inputString;
		}
		else return "(" + inputString + ")";
	}
}
