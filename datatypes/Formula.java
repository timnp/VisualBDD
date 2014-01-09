package datatypes;

import java.util.LinkedList;
import java.util.regex.*;

import javax.swing.JTable;

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
	 * the used variable ordering
	 */
	private VariableOrdering varOrd;
	
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
	 * @param varOrd
	 */
	public Formula(boolean val, VariableOrdering varOrd) {
		this.value = val;
		this.varOrd = varOrd;
		this.constructor = CONSTANT;
	}
	
	
	/**
	 * constructor for variables
	 * @param varNr
	 * @param varOrd
	 */
	public Formula (int varNr, VariableOrdering varOrd) {
		this.varNr = varNr;
		this.varOrd = varOrd;
		this.constructor = VARIABLE;
	}
	
	
	/**
	 * smart "constructor" for logical negation
	 * @param successor
	 * @param varOrd
	 * @return Formula representing a logical negation
	 * 			between this node and the given one
	 */
	public Formula not(VariableOrdering varOrd) {
		Formula not = new Formula();
		not.firstSuccessor = this;
		not.varOrd = varOrd;
		not.constructor = NOT;
		return not;
	}
	
	
	/**
	 * smart "constructor" for logical conjunction
	 * @param secondSuccessor
	 * @param varOrd
	 * @return Formula representing a logical conjunction
	 * 			between this node and the given one
	 */
	public Formula and(Formula secondSuccessor, VariableOrdering varOrd) {
		Formula and = new Formula();
		and.firstSuccessor = this;
		and.secondSuccessor = secondSuccessor;
		and.varOrd = varOrd;
		and.constructor = AND;
		return and;
	}
	
	
	/**
	 * smart "constructor" for logical disjunction
	 * @param secondSuccessor
	 * @param varOrd
	 * @return Formula representing a logical disjunction
	 */
	public Formula or(Formula secondSuccessor) {
		Formula or = new Formula();
		or.firstSuccessor = this;
		or.secondSuccessor = secondSuccessor;
		or.varOrd = varOrd;
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
			// If the constant is 0, its value is "false".
			if (formulaString == "0") {
				this.value = false;
			}
			// The only other way, the input String could match the constant 
			// Pattern, is that its a 1 and therefore its value is "true".
			else {
				this.value = true;
			}
			// setting the constructor
			this.constructor = CONSTANT;
		}
		// matching the input String to the variable Pattern
		this.matcher = variablePattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the variable number from the String
			this.varNr = Integer.parseInt(matcher.group(1));
			// setting the constructor
			this.constructor = VARIABLE;
		}
		// matching the input String to the logical negation Pattern
		this.matcher = notPattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the String that represents the successor
			String successorString = matcher.group(1);
			// creating the successor
			this.firstSuccessor = new Formula(successorString);
			// setting the constructor
			this.constructor = NOT;
		}
		// matching the input String to the binary operation Pattern
		this.matcher = binOpPattern.matcher(formulaString);
		if (matcher.matches()) {
			// retrieving the String that represents the first successor
			String firstSuccessorString = matcher.group(1);
			// creating the first successor
			this.firstSuccessor = new Formula(firstSuccessorString);
			// retrieving the String that represents the operation
			String opString = matcher.group(2);
			// retrieving the String that represents the first successor
			String secondSuccessorString = matcher.group(3);
			// creating the first successor
			this.secondSuccessor = new Formula(secondSuccessorString);
			// switch for the possible binary operations
			switch (opString) {
			// "*" stands for a logical conjunction
			case "*":
				this.constructor = AND;
				break;
			// "+" stands for a logical disjunction
			case "+":
				this.constructor = OR;
				break;
			default:
				//TODO user message
			}
		}
	}
	
	
//	public OBDD toOBDD() {
//		// a switch for the possible constructors
//		switch (this.constructor) {
//		// Zeroth case: The Formula represents a constant.
//		case 0:
//			if (this.value) {
//				return OBDD.ONE;
//			}
//			else {
//				return OBDD.ZERO;
//			}
//		}
//	}
	
	
	/**
	 * Evaluates the Formula relating to a given assignment.
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the Formula as a boolean
	 */
	public boolean assign(LinkedList<Integer> assignedOne) {
		// a switch for the possible constructors for this Formula
		switch (constructor) {
		// Zeroth case: The Formula represents a constant.
		case 0:
			// simply returning the constant's value
			return this.value;
		// First case: The Formula represents a variable.
		case 1:
			// If the variable is assigned the logical value one,
			// true is returned.
			if(assignedOne.contains(this.varNr)) {
				return true;
			}
			// Otherwise the variable is assigned the logical value zero
			// or it isn't assigned anything. Therefore false is returned.
			return false;
		// Second case: The Formula represents a logical negation.
		case 2:
			// calculating the value of the successor
			boolean firstSucVal = this.firstSuccessor.assign(assignedOne);
			// returning the negated value of the successor
			return !firstSucVal;
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// calculating the value of the first successor
			firstSucVal = this.firstSuccessor.assign(assignedOne);
			// calculating the value of the second successor
			boolean secondSucVal = this.secondSuccessor.assign(assignedOne);
			// returning the conjunction of the two successor values
			return firstSucVal && secondSucVal;
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// calculating the value of the first successor
			firstSucVal = this.firstSuccessor.assign(assignedOne);
			// calculating the value of the second successor
			secondSucVal = this.secondSuccessor.assign(assignedOne);
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
	 * @return the numbers of the variables used in
	 * the Formula and its sub-Formulas
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
			vars.add(this.varNr);
			// returning the list
			return vars;
		// Second case: The Formula represents a logical negation.
		case 2:
			// clearing the list
			vars.clear();
			// adding all variable numbers of the successor to the list
			vars.addAll(this.firstSuccessor.vars());
			// returning the list
			return vars;
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// clearing the list
			vars.clear();
			// adding all variable numbers of the first successor to the list
			vars.addAll(this.firstSuccessor.vars());
			// getting all variable numbers of the second successor
			LinkedList<Integer> secondSucVars = this.secondSuccessor.vars();
			// merging the list with the second successor's variable numbers
			for(Integer i : secondSucVars) {
				// adding the variable number to the list if it isn't already in
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
			vars.addAll(this.firstSuccessor.vars());
			// getting all variable numbers of the second successor
			secondSucVars = this.secondSuccessor.vars();
			// merging the list with the second successor's variable numbers
			for(Integer i : secondSucVars) {
				// adding the variable number to the list if it isn't already in
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
	 * 
	 * @return the Formula's entire truth table
	 */
	public JTable entireTruthTable() {
		// initializing the greatest and least numbers
		// among the Formula's variables
		int maxVarNo = -1;
		int minVarNo = -1;
		// checking for each of the Formula's variable numbers
		// whether it's the greatest or the least
		for (int i : this.vars()) {
			if (i > maxVarNo || maxVarNo == -1) {
				// if a number is greater than the current maximum or there is
				// no maximum, the number becomes the new maximum
				maxVarNo = i;
			}
			if (i < minVarNo || minVarNo == -1) {
				// if a number is less than the current minimum or there is
				// no minimum, the number becomes the new minimum
				minVarNo = i;
			}
		}
		// initializing a list for all variable numbers from minVarNo
		// to maxVarNo
		LinkedList<Integer> vars = new LinkedList<Integer>();
		// adding the variable numbers to the list
		for (int i = minVarNo ; i <= maxVarNo ; i++) {
			vars.add(i);
		}
		// returning the truth table for "all" variables
		return truthTable(vars);
	}
	
	
	/**
	 * 
	 * @param vars
	 * @return the Formula's truth table showing only the given variables
	 */
	private JTable truthTable(LinkedList<Integer> vars) {
		// initializing the column name array
		String[] columnNames = new String[vars.size() + 1];
		// making each of the Formula's variables a column name
		for (int i : vars) {
			columnNames[vars.indexOf(i)] = "X"+i;
		}
		// making the function value the last column name
		columnNames[vars.size()] =
				"f(X" + vars.getFirst() + ",...,X" + vars.getLast() + ")";
		// initializing the data array
		Integer[][] data = new Integer[(int) Math.pow(2, vars.size())]
						[vars.size() + 1];
		// writing zeros and ones for the variable values into the data array 
		// iterating over the columns
		for (int column = 0 ; column < vars.size() ; column++) {
			// A row of zeros and then ones before the next zero
			// is considered a "run".
			int maxRun = (int) Math.pow(2, column);
			// A zero or one after (below) another instance of
			// the same number is considered a "repeat".
			int maxRepeat = (int) Math.pow(2, vars.size() - column - 1);
			for (int run = 0 ; run < maxRun ; run++) {
				for (int repeat = 0 ; repeat < maxRepeat; repeat++) {
					// Each "run" first has the repeats of zeros. 
					data[2 * maxRepeat * run + repeat][column] = 0;
					// After (below) the zeros there are the repeats of ones.
					data[2 * maxRepeat * run + maxRepeat + repeat][column] = 1;
				}
			}
		}
		// initializing the list for the assignments to calculate the values
		// for the last column
		LinkedList<Integer> assignedOne = new LinkedList<Integer>();
		// calculating the values one row after another
		for (int row = 0 ; row < (int) Math.pow(2, vars.size()) ; row++) {
			// clearing the assignment list
			assignedOne.clear();
			// adding each variable in which's column is a one (in this row)
			// to the assignment list
			for (int column = 0 ; column < vars.size() ; column++) {
				if (data[row][column] == 1) {
					// If there is a one in the column, the respective variable
					// is added to the assignment list.
					assignedOne.add(vars.get(column));
				}
			}
			// function value of this Formula's function under the assignment
			// represented by the row
			boolean funVal = this.assign(assignedOne);
			// transferring the boolean function value into one/zero and
			// writing it into the data array
			if (funVal) {
				data[row][vars.size()] = 1;
			}
			else data[row][vars.size()] = 0;
		}
		// creating the truth table as a concrete JTable
		JTable truthTable = new JTable(data, columnNames);
		// returning the truth table
		return truthTable;
	}
	
	
	/**
	 * function that turns the Formula into a String
	 * @Override
	 */
	public String toString() {
		// switch for the possible constructors
		switch (this.constructor) {
		// zeroth case: returning either the tautological or the
		// contradictory Formula respectively
		case 0:
			if (this.value) {
				return "0";
			} else return "1";
		// first case: returning the variable
		case 1:
			return "X" + this.varNr;
		// second case: returning the negated successor in parentheses
		case 2:
			return "(-" + this.firstSuccessor.toString() + ")";
		// third case: returning the two successors combined by a symbol for
		// logical conjunction (*) in parentheses
		case 3:
			return "(" + this.firstSuccessor.toString() 
					+ " * " + this.secondSuccessor.toString() + ")";
		// fourth case: returning the two successors combined by a symbol for
		// logical disjunction (+) in parentheses
		case 4:
			return "(" + this.firstSuccessor.toString() 
					+ " + " + this.secondSuccessor.toString() + ")";
		default:
			return "";
		}
	}
	
	
	public Formula reduceConstants() {
		// switch for the possible constructors
		switch (this.constructor) {
		// Zeroth case: The Formula represents a constant and can't be reduced.
		case 0:
			return this;
		// First case: The Formula represents a variable and can't be reduced.
		case 1:
			return this;
		// Second case: The Formula represents a logical negation.
		case 2:
			// First the successor is reduced.
			Formula rcSuccessor = this.firstSuccessor.reduceConstants();
			// If the reduced successor is a constant,
			// the opposite constant is returned.
			if (rcSuccessor.constructor == CONSTANT) {
				return new Formula(!rcSuccessor.value, this.varOrd);
			}
			// Otherwise the negation of the reduced successor is returned.
			else {
				 return rcSuccessor.not(this.varOrd);
			}
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// First the two successors are reduced.
			Formula rcFirstSuccessor = this.firstSuccessor.reduceConstants();
			Formula rcSecondSuccessor = this.secondSuccessor.reduceConstants();
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
			} else return rcFirstSuccessor.and(rcSecondSuccessor, this.varOrd);
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// First the two successors are reduced.
			rcFirstSuccessor = this.firstSuccessor.reduceConstants();
			rcSecondSuccessor = this.secondSuccessor.reduceConstants();
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
