package datatypes;

import java.util.LinkedList;
import java.util.regex.*;

/**
 * 
 * @author TimNP
 *
 */
public class Formula {
	/**
	 * first successor (for operations)
	 */
	private Formula firstSuccessor;
	/**
	 * second successor (for binary operations)
	 */
	private Formula secondSuccessor;
	/**
	 * number for used constructor
	 */
	private int constructor;
	/**
	 * variable number
	 */
	private int varNr;
	/**
	 * value (for constants)
	 */
	private boolean value;
	
	/**
	 * constant numbers for all possible constructors
	 */
	public static int CONSTANT = 0;
	public static int VARIABLE = 1;
	public static int NOT = 2;
	public static int AND = 3;
	public static int OR = 4;
	
	
	/**
	 * private default constructor for smart "constructors"
	 */
	private Formula () {}
	
	
	/**
	 * constructor for constants
	 * @param val
	 */
	public Formula(boolean val) {
		value = val;
		constructor = CONSTANT;
	}
	
	
	/**
	 * constructor for variables
	 * @param varNr
	 */
	public Formula (int varNr) {
		this.varNr = varNr;
		constructor = VARIABLE;
	}
	
	
	/**
	 * smart "constructor" for logical negation
	 * @return Formula representing a logical negation of this node
	 */
	public Formula not() {
		Formula not = new Formula();
		not.firstSuccessor = this;
		not.constructor = NOT;
		return not;
	}
	
	
	/**
	 * smart "constructor" for logical conjunction
	 * @param secondSuccessor
	 * @return Formula representing a logical conjunction 
	 * 			between this node and the given one
	 */
	public Formula and(Formula secondSuccessor) {
		Formula and = new Formula();
		and.firstSuccessor = this;
		and.secondSuccessor = secondSuccessor;
		and.constructor = AND;
		return and;
	}
	
	
	/**
	 * smart "constructor" for logical disjunction
	 * @param secondSuccessor
	 * @return Formula representing a logical disjunction 
	 * 			between this node and the given one
	 */
	public Formula or(Formula secondSuccessor) {
		Formula or = new Formula();
		or.firstSuccessor = this;
		or.secondSuccessor = secondSuccessor;
		or.constructor = OR;
		return or;
	}
	
	
	/**
	 * String for spaces in Patterns
	 */
	private String spaces = "\\s*";
	
	/**
	 * Pattern String for constants
	 */
	private String constantPatternString = 
									// A constant is either 0 or 1;
									"[01]";
	/**
	 * Pattern for constants
	 */
	private Pattern constantPattern = Pattern.compile(constantPatternString);
	
	/**
	 * Pattern String for variables
	 */
	private String variablePatternString =
									// A variable starts with an X.
									"[Xx]"
									// group: the variable's number
									+ "(\\d+)";
	/**
	 * Pattern for variables
	 */
	private Pattern variablePattern = Pattern.compile(variablePatternString);
	
	/**
	 * Pattern String for logical negation
	 */
	private String notPatternString =
									// left parenthesis
									"[(]" 
									+ spaces 
									// negation symbol
									+ "[-]" 
									+ spaces 
									// successor:
									// either put in parentheses or a variable
									+ "([(].+[)]|[Xx]\\d+)" 
									+ spaces
									// right parenthesis
									+ "[)]";
	/**
	 * Pattern for logical negation
	 */
	private Pattern notPattern = Pattern.compile(notPatternString);
	
	/**
	 * Pattern String for binary operations
	 */
	private String binOpPatternString = 
									// left parenthesis
									"[(]" 
									+ spaces 
									// first successor:
									// either put in parentheses or a variable
									+ "([(].+[)]|[Xx]\\d+)" 
									+ spaces 
									// the operation:
									// logical conjunction or disjunction
									+ "([*+])" 
									+ spaces 
									// second successor:
									// either put in parentheses or a variable
									+ "([(].+[)]|[Xx]\\d+)" 
									+ spaces 
									// right parenthesis
									+ "[)]";
	/**
	 * Pattern for binary operations
	 */
	private Pattern binOpPattern = Pattern.compile(binOpPatternString);
	
	/**
	 * Matcher for the Patterns
	 */
	private Matcher matcher;
	
	
	/**
	 * constructor for Formulas from a given String that meets the criteria
	 * defined by the Patterns 
	 * @param formulaString
	 */
	public Formula (String formulaString) {
		// matching the input String to the constant Pattern
		this.matcher = constantPattern.matcher(formulaString);
		if (matcher.matches()) {
			// If the constant is 1, its value is "true".
			// Otherwise it is 0, and its value is "false".
			value = (formulaString == "1");
			// setting the constructor
			constructor = CONSTANT;
		}
		// matching the input String to the variable Pattern
		matcher = variablePattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the variable number from the String
			varNr = Integer.parseInt(matcher.group(1));
			// setting the constructor
			constructor = VARIABLE;
		}
		// matching the input String to the logical negation Pattern
		matcher = notPattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the String that represents the successor
			String successorString = matcher.group(1);
			// creating the successor
			firstSuccessor = new Formula(successorString);
			// setting the constructor
			constructor = NOT;
		}
		// matching the input String to the binary operation Pattern
		matcher = binOpPattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the String that represents the first successor
			String firstSuccessorString = matcher.group(1);
			// creating the first successor
			firstSuccessor = new Formula(firstSuccessorString);
			// retrieving the String that represents the operation
			String opString = matcher.group(2);
			// retrieving the String that represents the first successor
			String secondSuccessorString = matcher.group(3);
			// creating the first successor
			secondSuccessor = new Formula(secondSuccessorString);
			// switch for the possible binary operations
			switch (opString) {
			// "*" stands for a logical conjunction
			case "*":
				constructor = AND;
				break;
			// "+" stands for a logical disjunction
			case "+":
				constructor = OR;
				break;
			default:
				//TODO user message
			}
		}
	}
	
	
	/**
	 * provides the complete OBDD representing this Formula
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public OBDD toOBDD(VariableOrdering varOrd) {
		// constructing the entire TruthTable
		TruthTable entireTruthTable = entireTruthTable(varOrd);
		// creating the complete OBDD from the TruthTable
		return entireTruthTable.toOBDD(varOrd);
	}
	
	
	/**
	 * evaluates the Formula relating to a given assignment
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the Formula as a boolean
	 */
	public boolean evaluate(LinkedList<Integer> assignedOne) {
		// initializing variables for the values of the successors
		boolean firstSucVal;
		boolean secondSucVal;
		// a switch for the possible constructors for this Formula
		switch (constructor) {
		// Zeroth case: The Formula represents a constant.
		case 0:
			// simply returning the constant's value
			return value;
		// First case: The Formula represents a variable.
		case 1:
			// If the variable is assigned the logical value one,
			// true is returned.
			// Otherwise the variable is assigned the logical value zero
			// or it isn't assigned anything. Therefore false is returned.
			return (assignedOne.contains(varNr));
		// Second case: The Formula represents a logical negation.
		case 2:
			// calculating the value of the successor
			firstSucVal = firstSuccessor.evaluate(assignedOne);
			// returning the negated value of the successor
			return !firstSucVal;
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// calculating the value of the first successor
			firstSucVal = firstSuccessor.evaluate(assignedOne);
			// calculating the value of the second successor
			secondSucVal = secondSuccessor.evaluate(assignedOne);
			// returning the conjunction of the two successor values
			return firstSucVal && secondSucVal;
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// calculating the value of the first successor
			firstSucVal = firstSuccessor.evaluate(assignedOne);
			// calculating the value of the second successor
			secondSucVal = secondSuccessor.evaluate(assignedOne);
			// returning the disjunction of the two successor values
			return firstSucVal || secondSucVal;
		// Default case: None of the given constructors was used.
		default:
			// tentative default value: false
			// TODO user message
			return false;
		}
	}
	
	
	/**
	 * method that provides the numbers of the variables used in the Formula 
	 * and its sub-Formulas
	 * @return
	 */
	private LinkedList<Integer> vars() {
		// initializing the LinkedList for the return
		LinkedList<Integer> vars = new LinkedList<Integer>();
		// a switch for the possible constructors for this Formula
		switch (constructor) {
		// Zeroth case: The Formula represents a constant.
		case 0:
			// clearing the list
			vars.clear();
			// returning the list
			return vars;
		// First case: The Formula represents a variable.
		case 1:
			// clearing the list
			vars.clear();
			// adding the variable's number to the list
			vars.add(varNr);
			// returning the list
			return vars;
		// Second case: The Formula represents a logical negation.
		case 2:
			// clearing the list
			vars.clear();
			// adding all variable numbers of the successor to the list
			vars.addAll(firstSuccessor.vars());
			// returning the list
			return vars;
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// clearing the list
			vars.clear();
			// adding all variable numbers of the first successor to the list
			vars.addAll(firstSuccessor.vars());
			// getting all variable numbers of the second successor
			LinkedList<Integer> secondSucVars = secondSuccessor.vars();
			// merging the list with the second successor's variable numbers
			for(Integer i : secondSucVars) {
				// adding the variable number to the list 
				// if it isn't already in
				if(!vars.contains(i)) {
					vars.add(i);
				}
			}
			// returning the list
			return vars;
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// clearing the list
			vars.clear();
			// adding all variable numbers of the first successor to the list
			vars.addAll(firstSuccessor.vars());
			// getting all variable numbers of the second successor
			secondSucVars = secondSuccessor.vars();
			// merging the list with the second successor's variable numbers
			for(Integer i : secondSucVars) {
				// adding the variable number to the list 
				// if it isn't already in
				if(!vars.contains(i)) {
					vars.add(i);
				}
			}
			// returning the list
			return vars;
		// Default case: None of the given constructors was used.
		default:
			// tentative default value: the empty list
			// TODO user message
			vars.clear();
			return vars;
		}
	}
	
	
	/**
	 * @param varOrd - the VariableOrdering
	 * @return the Formula's entire truth table
	 */
	public TruthTable entireTruthTable(VariableOrdering varOrd) {
		// returning the truth table for "all" variables
		return new TruthTable(this, varOrd.getOrdList());
	}
	
	
	/**
	 * function that turns the Formula into a String
	 * @Override
	 */
	public String toString() {
		// switch for the possible constructors
		switch (constructor) {
		// zeroth case: returning either the tautological or the
		// contradictory Formula respectively
		case 0:
			if (value) {
				return "0";
			} else return "1";
		// first case: returning the variable
		case 1:
			return "X" + varNr;
		// second case: returning the negated successor in parentheses
		case 2:
			return "(-" + firstSuccessor.toString() + ")";
		// third case: returning the two successors combined by a symbol for
		// logical conjunction (*) in parentheses
		case 3:
			return "(" + firstSuccessor.toString() 
					+ " * " + secondSuccessor.toString() + ")";
		// fourth case: returning the two successors combined by a symbol for
		// logical disjunction (+) in parentheses
		case 4:
			return "(" + firstSuccessor.toString() 
					+ " + " + secondSuccessor.toString() + ")";
		default:
			return "";
		}
	}
	
	
	/**
	 * TODO WHY DOESN'T THIS HAVE JAVADOC COMMENTS?
	 * @return
	 */
	public Formula reduceConstants() {
		// switch for the possible constructors
		switch (constructor) {
		// Zeroth case: The Formula represents a constant and can't be reduced.
		case 0:
			return this;
		// First case: The Formula represents a variable and can't be reduced.
		case 1:
			return this;
		// Second case: The Formula represents a logical negation.
		case 2:
			// First the successor is reduced.
			Formula rcSuccessor = firstSuccessor.reduceConstants();
			// If the reduced successor is a constant,
			// the opposite constant is returned.
			if (rcSuccessor.constructor == CONSTANT) {
				return new Formula(!rcSuccessor.value);
			}
			// Otherwise the negation of the reduced successor is returned.
			else {
				 return rcSuccessor.not();
			}
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// First the two successors are reduced.
			Formula rcFirstSuccessor = firstSuccessor.reduceConstants();
			Formula rcSecondSuccessor = secondSuccessor.reduceConstants();
			if (rcFirstSuccessor.constructor == CONSTANT) {
				// If the first successor is the tautological Formula,
				// the second successor is returned.
				if (rcFirstSuccessor.value) {
					return rcSecondSuccessor;
				}
				// If the first successor is the contradictory Formula,
				// it is returned.
				else return rcFirstSuccessor;
			} else if (rcSecondSuccessor.constructor == CONSTANT) {
				// If the second successor is the tautological Formula,
				// the first successor is returned.
				if (rcSecondSuccessor.value) {
					return rcFirstSuccessor;
				}
				// If the second successor is the contradictory Formula,
				// it is returned.
				else return rcSecondSuccessor;
			// If none of the two successors is a constant, 
			// their conjunction is returned.
			} else return rcFirstSuccessor.and(rcSecondSuccessor);
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// First the two successors are reduced.
			rcFirstSuccessor = firstSuccessor.reduceConstants();
			rcSecondSuccessor = secondSuccessor.reduceConstants();
			if (rcFirstSuccessor.constructor == CONSTANT) {
				// If the first successor is the tautological Formula,
				// it is returned.
				if (rcFirstSuccessor.value) {
					return rcFirstSuccessor;
				}
				// If the first successor is the contradictory Formula,
				// the second successor is returned.
				else return rcSecondSuccessor;
			} else if (rcSecondSuccessor.constructor == CONSTANT) {
				// If the second successor is the tautological Formula,
				// it is returned.
				if (rcSecondSuccessor.value) {
					return rcSecondSuccessor;
				}
				// If the second successor is the contradictory Formula,
				// the first successor is returned.
				else return rcFirstSuccessor;
			// If none of the two successors is a constant, 
			// their conjunction is returned.
			} else return rcFirstSuccessor.or(rcSecondSuccessor);
		// Default case: None of the given constructors was used.
		default:
			// tentative value: the Formula itself
			// TODO user message
			return this;
		}
	}
}
