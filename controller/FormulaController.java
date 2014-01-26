package controller;

import java.util.regex.*;

import datatypes.Formula;

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
	public Formula stringToFormula(String inputString) {
		String formulaString = toFormulaString(inputString, false);
		return new Formula(formulaString);
	}
	
	
	/**
	 * Pattern String for parentheses
	 */
	private String parenthesesPatternString =
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
	private Pattern parenthesesPattern = 
			Pattern.compile(parenthesesPatternString);
	
	/**
	 * Pattern String for constants
	 */
	private String constantPatternString =
			// possible spaces
			"\\s*"
			// group: the constant, either 1 or 0
			+ "([01])"
			// possible spaces
			+ "\\s*";
	/**
	 * Pattern for constants
	 */
	private Pattern constantPattern = 
			Pattern.compile(constantPatternString);
	
	/**
	 * Pattern String for variables
	 */
	private String variablePatternString = 
			// possible spaces
			"\\s*"
			// group: the variable consisting of an X and a number
			+ "([Xx]\\d+)"
			// possible spaces
			+ "\\s*";
	/**
	 * Pattern for variables
	 */
	private Pattern variablePattern = Pattern.compile(variablePatternString);
	
	/**
	 * Pattern String for a logical negation
	 */
	private String notPatternString =
			// possible spaces
			"\\s*"
			// the negation symbol
			+ "-"
			// group: the successor
			// A negation's successor is either completely parenthesized or a 
			// variable.
			+ "([(].*[)]|[Xx]\\d+)";
	/**
	 * Pattern for a logical negation
	 */
	private Pattern notPattern = Pattern.compile(notPatternString);
	
	/**
	 * Pattern String for a logical conjunction
	 */
	private String andPatternString =
			// group 1: the first successor
			"(.*)"
			// the conjunction symbol
			+ "*"
			// group 2: the second successor
			+ "(.*)";
	/**
	 * Pattern for a logical conjunction
	 */
	private Pattern andPattern = Pattern.compile(andPatternString);
	
	/**
	 * Pattern for a logical disjunction
	 */
	private String orPatternString = 
			// group 1: the first successor
			"(.*)"
			// the disjunction symbol
			+ "+"
			// group 2: the second successor
			+ "(.*)";
	/**
	 * Pattern for a logical disjunction
	 */
	private Pattern orPattern = Pattern.compile(orPatternString);
	
	private Matcher matcher;
	
	
	private String toFormulaString(String inputString, boolean isInPars) {
		// matching the input String to the parentheses Pattern
		matcher = parenthesesPattern.matcher(inputString);
		if (matcher.matches()) {
			// If the String starts with a left parenthesis and ends with a 
			// right one, the part between is in parentheses.
			return "(" + toFormulaString(matcher.group(1), true) + ")";
		}
		// matching the input String to the constant Pattern
		matcher = constantPattern.matcher(inputString);
		if (matcher.matches()) {
			// retrieving the constant itself
			String constant = matcher.group(1);
			// putting the constant in parentheses if it isn't already
			return inParentheses(constant, isInPars);
		}
		// matching the input String to the variable Pattern
		matcher = variablePattern.matcher(inputString);
		if (matcher.matches()) {
			// retrieving the variable itself
			String variable = matcher.group(1);
			// putting the variable in parentheses if it isn't already
			return inParentheses(variable, isInPars);
		}
		// matching the input String to the logical negation Pattern
		matcher = notPattern.matcher(inputString);
		if (matcher.matches()) {
			// the negation consisting of the negation symbol and the successor
			// (also turned into a formula String)
			String negation = "-" + toFormulaString(matcher.group(1), false);
			// putting the negation in parentheses if it isn't already
			return inParentheses(negation, isInPars);
		}
		// matching the input String to the logical conjunction Pattern
		matcher = andPattern.matcher(inputString);
		if (matcher.matches()) {
			// the conjunction consisting of its two successors (also turned 
			// into formula Strings) combined by the conjunction symbol
			String conjunction = toFormulaString(matcher.group(1), false) + "*"
					+ toFormulaString(matcher.group(2), false);
			// putting the conjunction in parentheses if it isn't already
			return inParentheses(conjunction, isInPars);
		}
		// matching the input String to the logical disjunction Pattern
		matcher = orPattern.matcher(inputString);
		if (matcher.matches()) {
			// the disjunction consisting of its two successors (also turned 
			// into formula Strings) combined by the disjunction symbol
			String disjunction = toFormulaString(matcher.group(1), false) + "+"
					+ toFormulaString(matcher.group(2), false);
			// putting the disjunction in parentheses if it isn't already
			return inParentheses(disjunction, isInPars);
		}
		// If none of the given Patterns matches the input String 
		// (possibly because it's empty), the empty String is returned.
		return "";
	}
	
	/**
	 * auxiliary function that puts a String in parentheses if it isn't already
	 * @param inputString - the string
	 * @param isInPars - states, whether the String is in parentheses
	 * @return the string in parentheses
	 */
	private String inParentheses(String inputString, boolean isInPars) {
		if (isInPars) {
			return inputString;
		}
		else return "(" + inputString + ")";
	}
}
