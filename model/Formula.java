package model;

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
	private int var;
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
		this.var = varNr;
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
	 * Pattern String for constants
	 */
	private String constantPatternString = 
			// A constant is either 0 or 1.
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
			"[Xx](\\d+)";
	/**
	 * Pattern for variables
	 */
	private Pattern variablePattern = Pattern.compile(variablePatternString);
	
//	/**
//	 * Pattern String for logical negation
//	 */
//	private String notPatternString =
//			// negation symbol
//			"[-](.+)";
//	/**
//	 * Pattern for logical negation
//	 */
//	private Pattern notPattern = Pattern.compile(notPatternString);
//	
//	/**
//	 * Pattern String for binary operations
//	 */
//	private String binOpPatternString =
//			// group 1: the first successor
//			"[(](.+)" 
//			// group 2: the operation:
//			// logical conjunction or disjunction
//			+ "([*+])" 
//			// group 3: the second successor
//			+ "(.+)[)]";
//	/**
//	 * Pattern for binary operations
//	 */
//	private Pattern binOpPattern = Pattern.compile(binOpPatternString);
	
	/**
	 * Matcher for the Patterns
	 */
	private Matcher matcher;
	
	
//	/**
//	 * constructor for Formulas from a given String that meets the criteria
//	 * defined by the Patterns 
//	 * @param formulaString
//	 */
//	public Formula (String formulaString) {
//		// matching the input String to the constant Pattern
//		matcher = constantPattern.matcher(formulaString);
//		if (matcher.matches()) {
//			// If the constant is 1, its value is "true".
//			// Otherwise it is 0, and its value is "false".
//			value = (matcher.group(1) == "1");
//			// setting the constructor
//			constructor = CONSTANT;
//		}
//		// matching the input String to the variable Pattern
//		matcher = variablePattern.matcher(formulaString);
//		if (matcher.matches()) {
//			// retrieving the variable number from the String
//			var = Integer.parseInt(matcher.group(1));
//			// setting the constructor
//			constructor = VARIABLE;
//		}
//		// matching the input String to the logical negation Pattern
//		matcher = notPattern.matcher(formulaString);
//		if (matcher.matches()) {
//			// retrieving the String that represents the successor
//			String successorString = matcher.group(1);
//			// creating the successor
//			firstSuccessor = new Formula(successorString);
//			// setting the constructor
//			constructor = NOT;
//		}
//		// matching the input String to the binary operation Pattern
//		matcher = binOpPattern.matcher(formulaString);
//		if (matcher.matches()) {
//			// retrieving the String that represents the first successor
//			String firstSuccessorString = matcher.group(1);
//			// creating the first successor
//			firstSuccessor = new Formula(firstSuccessorString);
//			// retrieving the String that represents the operation
//			String opString = matcher.group(2);
//			// retrieving the String that represents the first successor
//			String secondSuccessorString = matcher.group(3);
//			// creating the first successor
//			secondSuccessor = new Formula(secondSuccessorString);
//			// switch for the possible binary operations
//			switch (opString) {
//			// "*" stands for a logical conjunction
//			case "*":
//				constructor = AND;
//				break;
//			// "+" stands for a logical disjunction
//			case "+":
//				constructor = OR;
//				break;
//			default:
//				//TODO user message
//			}
//		}
//	}
	
	
	public Formula (String formulaString) {
		// matching the input String to the constant Pattern
		matcher = constantPattern.matcher(formulaString);
		if (matcher.matches()) {
			// If the constant is 1, its value is "true".
			// Otherwise it is 0, and its value is "false".
			value = (matcher.group() == "1");
			// setting the constructor
			constructor = CONSTANT;			
		}
		// matching the input String to the variable Pattern
		matcher = variablePattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the variable number from the String
			var = Integer.parseInt(matcher.group(1));
			// setting the constructor
			constructor = VARIABLE;
		}
		// If a Formula String starts with a logical negation symbol, it 
		// represents a logical negation.
		if (formulaString.startsWith("-")) {
			// creating the successor of the logical negation
			firstSuccessor = new Formula(formulaString.substring(1));
			// setting the constructor
			constructor = NOT;
		}
		// If a Formula String starts with a left parenthesis and ends with a 
		// right one, it is considered to represent a binary operation.
		if (formulaString.startsWith("(") && formulaString.endsWith(")")) {
			// retrieving the three parts of the Formula String:
			// the Formula String representing the first successor
			// the String representing the given Formula String's binary 
			// 		operation symbol
			// the Formula String representing the second successor
			String[] formulaParts = binOpParts(formulaString);
			// creating the first successor of the binary operation
			firstSuccessor = new Formula(formulaParts[0]);
			// creating the second successor of the binary operation
			secondSuccessor = new Formula(formulaParts[2]);
			// a switch for the possible binary operations
			switch (formulaParts[1]) {
			// First case: "*" stands for a logical conjunction.
			case "*":
				constructor = AND;
				break;
			// Second case: "+" stands for a logical disjunction.
			case "+":
				constructor = OR;
				break;
			// tentative default value: -1
			default:
				constructor = -1;
			}
		}
	}
	
	
	/**
	 * auxiliary method that splits a given Formula String representing a 
	 * binary operation into the operation symbol and the two Formula Strings 
	 * representing the successors
	 * @param formulaString - a Formula String representing a binary operation
	 * @return an Array with the first successor, the operation symbol, and the
	 * second successor
	 */
	private String[] binOpParts(String formulaString) {
		// initializing the Array for the three parts of the binary operation 
		// Formula String
		String[] result = new String[3];
		// initializing a variable for the number of unclosed left parentheses 
		// found in the String
		int leftPars = 0;
		// looking for the binary operation symbol within the outer parentheses
		// (The last character within the outer parentheses can't represent the
		//  binary operation in a Formula String.)
		for (int i = 1; i < formulaString.length() - 2; i++) {
			// retrieving the current character
			char currentChar = formulaString.charAt(i);
			// If the current character represents a binary operation and there
			// are no left parentheses left to be closed, it is the given 
			// Formula String's operation.
			if ((currentChar == '*' || currentChar == '+') && 
					leftPars == 0) {
				// retrieving the Formula String representing the Formula's 
				// first successor
				result[0] = formulaString.substring(1, i);
				// retrieving the String representing the Formula's binary 
				// operation.
				result[1] = formulaString.substring(i, i+1);
				// retrieving the Formula String representing the Formula's 
				// second successor.
				result[2] = formulaString.substring(i+1, 
						formulaString.length() - 1);
				// breaking the loop since the binary operation has been found
				break;
			}
			// If the current character is a left parenthesis, their number is 
			// increased by one.
			else if (currentChar == '(') {
				leftPars++;
			} 
			// If the current character is a right parenthesis, the number of 
			// left ones is decreased by one since one of them has been closed.
			else if (currentChar == ')') {
				leftPars--;
			}
			// Otherwise nothing is done.
		}
		// returning the result Array
		return result;
	}
	
	
	/**
	 * provides the complete OBDD representing this Formula
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public OBDD toObdd(VariableOrdering varOrd) {
		// constructing the entire TruthTable
		TruthTable entireTruthTable = entireTruthTable(varOrd);
		// creating the complete OBDD from the TruthTable
		return entireTruthTable.toObdd(varOrd);
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
			return (assignedOne.contains(var));
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
			return false;
		}
	}
	
	
	/**
	 * method that states whether this Formula is logical equivalent to another
	 * one
	 * @param f - the other Formula
	 * @return
	 */
	public boolean logicalEquivalent(Formula f) {
		// initializing the list of variables of the two Formulas combined
		LinkedList<Integer> combinedVars = vars();
		// iterator for the other Formula's list of variables
		java.util.Iterator<Integer> iter = f.vars().iterator();
		// iterating over the list to complete the combined variable list
		while (iter.hasNext()) {
			// the next variable
			int nextVar = iter.next();
			// adding the variable to the combined variable list if it isn't 
			// already in
			if (!combinedVars.contains(nextVar)) {
				combinedVars.add(nextVar);
			}
		}
		// creating a TruthTable for each of the two Formulas
		TruthTable tt1 = new TruthTable(this, combinedVars);
		TruthTable tt2 = new TruthTable(f, combinedVars);
		// retrieving the actual data from the TruthTables
		Boolean[][] data1 = tt1.getData();
		Boolean[][] data2 = tt2.getData();
		// the amount of the combined variables
		int numberOfVars = combinedVars.size();
		// checking for each assignment, whether the two Formulas provide the 
		// same result
		for (int i = 0; i < Math.pow(2, numberOfVars); i++) {
			if (!data1[i][numberOfVars].equals(data2[i][numberOfVars])) {
				// If the two Formulas provide different results under at least
				// one assignment, they arent's logical equivalent.
				return false;
			}
		}
		// If no check failed, the two Formulas are logical equivalent.
		return true;
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
			vars.add(var);
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
			return "X" + var;
		// second case: returning the negated successor in parentheses
		case 2:
			return "-" + firstSuccessor.toString();
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
	 * provides a Formula logically equivalent to this one reduced in relation 
	 * to constants, double negation, idempotency and tertium non datur
	 * @return
	 */
	public Formula reduce() {
		// variables for reduced successors
		Formula rFirstSuc;
		Formula rSecondSuc;
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
			rFirstSuc = firstSuccessor.reduce();
			// If the reduced successor is a constant,
			// the opposite constant is returned.
			if (rFirstSuc.constructor == CONSTANT) {
				return new Formula(!rFirstSuc.value);
			}
			// If the reduced successor is a logical negation, 
			// its successor is returned.
			else if (rFirstSuc.constructor == NOT) {
				return rFirstSuc.firstSuccessor;
			}
			// Otherwise the negation of the reduced successor is returned.
			else return rFirstSuc.not();
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// First the two successors are reduced.
			rFirstSuc = firstSuccessor.reduce();
			rSecondSuc = secondSuccessor.reduce();
			if (rFirstSuc.constructor == CONSTANT) {
				// If the first successor is the tautological Formula,
				// the second successor is returned.
				if (rFirstSuc.value) {
					return rSecondSuc;
				}
				// If the first successor is the contradictory Formula,
				// it is returned.
				else return rFirstSuc;
			} else if (rSecondSuc.constructor == CONSTANT) {
				// If the second successor is the tautological Formula,
				// the first successor is returned.
				if (rSecondSuc.value) {
					return rFirstSuc;
				}
				// If the second successor is the contradictory Formula,
				// it is returned.
				else return rSecondSuc;
			}
			// If the two successors are equal, the first one is returned.
			else if (rFirstSuc.isEqual(rSecondSuc)) {
				return rFirstSuc;
			}
			// If the first successor is the negation of the second one, 
			// the contradictory Formula is returned.
			else if (rFirstSuc.constructor == NOT && 
					rFirstSuc.firstSuccessor.isEqual(rSecondSuc)) {
				return new Formula(false);
			}
			// If the second successor is the negation of the first one, 
			// the contradictory Formula is returned.
			else if (rSecondSuc.constructor == NOT && 
					rFirstSuc.isEqual(rSecondSuc.firstSuccessor)) {
				return new Formula(false);
			}
			// Otherwise the conjunction of the two reduced successors is 
			// returned.
			else return rFirstSuc.and(rSecondSuc);
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// First the two successors are reduced.
			rFirstSuc = firstSuccessor.reduce();
			rSecondSuc = secondSuccessor.reduce();
			if (rFirstSuc.constructor == CONSTANT) {
				// If the first successor is the tautological Formula,
				// it is returned.
				if (rFirstSuc.value) {
					return rFirstSuc;
				}
				// If the first successor is the contradictory Formula,
				// the second one is returned.
				else return rSecondSuc;
			} else if (rSecondSuc.constructor == CONSTANT) {
				// If the second successor is the tautological Formula,
				// it is returned.
				if (rSecondSuc.value) {
					return rSecondSuc;
				}
				// If the second successor is the contradictory Formula,
				// the second one is returned.
				else return rFirstSuc;
			}
			// If the two successors are equal, the first one is returned.
			else if (rFirstSuc.isEqual(rSecondSuc)) {
				return rFirstSuc;
			}
			// If the first successor is the negation of the second one, 
			// the tautological Formula is returned.
			else if (rFirstSuc.constructor == NOT && 
					rFirstSuc.firstSuccessor.isEqual(rSecondSuc)) {
				return new Formula(true);
			}
			// If the second successor is the negation of the first one, 
			// the tautological Formula is returned.
			else if (rSecondSuc.constructor == NOT && 
					rFirstSuc.isEqual(rSecondSuc.firstSuccessor)) {
				return new Formula(true);
			}
			// Otherwise the disjunction of the two reduced successors is 
			// returned.
			else return rFirstSuc.or(rSecondSuc);
		// Default case: None of the given constructors was used.
		default:
			// tentative value: the Formula itself
			return this;
		}
	}

	
	/**
	 * states whether this Formula is equal to a given Formula
	 * @param g - the other Formula
	 * @return
	 */
	private boolean isEqual(Formula g) {
		// If the two Formulas' constructors are the same, they may be equal.
		if (constructor == g.constructor) {
			// variables for the equality of the successors
			boolean firstSucEqual;
			boolean secondSucEqual;
			// a switch for the possible constructors
			switch (constructor) {
			// Zeroth case: The Formula represents a constant.
			case 0:
				// If the other Formula also represents a constant, the equality of
				// their values is checked.
				return (value == g.value);
			// First case: The Formula represents a variable.
			case 1:
				// If the other Formula also represents a variable, the equality of
				// the variables is checked.
				return (var == g.var);
			// Second case: The Formula represents a logical negation.
			case 2:
				// If the other Formula also represents a logical negation, the 
				// equality of their successors is checked. 
				return firstSuccessor.isEqual(g.firstSuccessor);
			// Third case: The Formula represents a logical conjunction.
			case 3:
				// If the other Formula also represents a logical conjunction, the 
				// equality of their successors is checked.
				firstSucEqual = firstSuccessor.isEqual(g.firstSuccessor);
				secondSucEqual = secondSuccessor.isEqual(g.secondSuccessor);
				return (firstSucEqual && secondSucEqual);
			// Fourth case: The Formula represents a logical disjunction.
			case 4:
				// If the other Formula also represents a logical disjunction, the 
				// equality of their successors is checked.
				firstSucEqual = firstSuccessor.isEqual(g.firstSuccessor);
				secondSucEqual = secondSuccessor.isEqual(g.secondSuccessor);
				return (firstSucEqual && secondSucEqual);
			// Default case: None of the given constructors was used.
			default:
				// Equality can't be checked and false is returned.
				return false;
			}
		}
		// If the two Formulas' constructors aren't the same, they aren't equal.
		else return false;
	}
}
