package datatypes;

import java.util.Collections;
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
	 * provides TODO
	 * @return
	 */
	public OBDD toOBDD() {
		// a switch for the possible constructors
		switch (constructor) {
		// Zeroth case: The Formula represents a constant.
		case 0:
			// If this constant Formula is tautological, the OBDD has to be too.
			if (this.value) {
				// Because the Formula has no successors and doesn't represent
				// a variable itself, the set of its variables is empty.
				// The node to begin with is the 1-terminal itself.
				OBDD currentNode = OBDD.ONE;
				// initializing a list for the OBDD nodes of the layer below 
				// the current one
				LinkedList<OBDD> layerBelow = new LinkedList<OBDD>();
				// If the VariableOrdering isn't empty, more nodes are 
				// constructed.
				if (varOrdSize > 0) {
					// First the lowest (decision node) layer consisting of 
					// redundant nodes leading to the 1-terminal is constructed.
					for (int j = 0; j < Math.pow(2, varOrdSize - 1); j++) {
						// Since the "cons" method is constructive, the current 
						// node may be overwritten without losing any node 
						// constructed this way.
						currentNode = currentNode.cons(this.varOrd.getLast(), currentNode, this.varOrd);
						// adding the nodes to the list of the layer below for 
						// the layer above
						layerBelow.add(currentNode);
					}
					// The rest of the layers is constructed the same way: Each 
					// node gets two different nodes of the layer below as 
					// children.
					for (int i = this.varOrd.size() - 2; i >= 0; i--) {
						// The number of nodes decreases each layer from bottom 
						// to top by half.
						for (int j = 0; j < Math.pow(2, i); j++) {
							currentNode = 
									layerBelow.poll().cons(this.varOrd.get(i), layerBelow.poll(), this.varOrd);
							// adding the new nodes to the layer below list
							layerBelow.add(currentNode);
						}
					}
				}
				// The constructed OBDD's root is returned.
				return currentNode;
			}
			// Otherwise the Formula is contradictory and so the OBDD has to be.
			else {
				// In this case the node to begin with is the 0-terminal.
				OBDD currentNode = OBDD.ZERO;
				// initializing a list for the OBDD nodes of the layer below 
				// the current one
				LinkedList<OBDD> layerBelow = new LinkedList<OBDD>();
				// If the VariableOrdering isn't empty, more nodes are 
				// constructed.
				if (varOrdSize > 0) {
					// First the lowest (decision node) layer consisting of 
					// redundant nodes leading to the 0-terminal is constructed.
					for (int j = 0; j < Math.pow(2, varOrdSize - 1); j++) {
						currentNode = currentNode.cons(this.varOrd.getLast(), currentNode, this.varOrd);
						// adding the nodes to the list of the layer below for 
						// the layer above
						layerBelow.add(currentNode);
					}
					// The rest of the layers is constructed the same way: Each 
					// node gets two different nodes of the layer below as 
					// children.
					for (int i = this.varOrd.size() - 2; i >= 0; i--) {
						// The number of nodes decreases each layer from bottom 
						// to top by half.
						for (int j = 0; j < Math.pow(2, i); j++) {
							currentNode = 
									layerBelow.poll().cons(this.varOrd.get(i), layerBelow.poll(), this.varOrd);
							// adding the new nodes to the layer below list
							layerBelow.add(currentNode);
						}
					}
				}
				// The constructed OBDD's root is returned.
				return currentNode;
			}
		// First case: The Formula represents a variable.
		case 1:
			// getting the index of the specified variable in the 
			// VariableOrdering
			int varIndex = this.varOrd.get(this.varNr);
			// If the specified variable is part of the VariableOrdering, an
			// OBDD with all of the VariableOrdering's variables can be 
			// constructed.
			if (varIndex >= 0) {
				// initializing a variable for the nodes that are being 
				// constructed
				OBDD currentNode = OBDD.ONE;
				// initializing a list for the OBDD nodes of the layer below 
				// the current one
				LinkedList<OBDD> layerBelow = new LinkedList<OBDD>();
				if (varIndex == varOrdSize - 1) {
					// If the specified variable is the last in the 
					// VariableOrdering, the lowest layer consists of OBDD 
					// nodes that decide, whether the value is 1 or 0. 
					for (int j = 0; j < Math.pow(2, varIndex); j++) {
						currentNode = 
								OBDD.ONE.cons(this.varNr, OBDD.ZERO, this.varOrd);
						layerBelow.add(currentNode);
					}
				} else {
					// Otherwise there are two groups of nodes in the layers 
					// below the layer of the specified variable: the "zero" 
					// nodes, which lead only to the 0-terminal, and the 
					// 1-nodes, which lead only to the 1-terminal.
					// The lowest non-terminal layer has to be constructed 
					// separately because of the "different" number of nodes in 
					// the layer below.
					// initializing two lists for the "zero" nodes and the 
					// "one" nodes of the layer below
					LinkedList<OBDD> layerBelowZero = new LinkedList<OBDD>();
					LinkedList<OBDD> layerBelowOne = new LinkedList<OBDD>();
					for (int j = 0; j < Math.pow(2, varOrdSize - 2); j++) {
						// constructing the lowest "zero" nodes
						currentNode = 
								OBDD.ZERO.cons(this.varOrd.getLast(), OBDD.ZERO, this.varOrd);
						// adding the "zero" nodes to their list
						layerBelowZero.add(currentNode);
						// constructing the lowest "one" nodes
						currentNode = 
								OBDD.ONE.cons(this.varOrd.getLast(), OBDD.ONE, this.varOrd);
						// adding the "one" nodes to their list
						layerBelowOne.add(currentNode);
					}
					// constructing the other layers below the layer of the 
					// specified variable
					for (int i = this.varOrd.size() - 2; i > varIndex; i--) {
						for (int j = 0; j < Math.pow(2, i - 1); j++) {
							// constructing the "zero" nodes
							currentNode = 
									layerBelowZero.poll().cons(this.varOrd.get(i), layerBelowZero.poll(), this.varOrd);
							// adding the "zero" nodes to their list
							layerBelowZero.add(currentNode);
							// constructing the "one" nodes
							currentNode = 
									layerBelowOne.poll().cons(this.varOrd.get(i), layerBelowOne.poll(), this.varOrd);
							// adding the "one" nodes to their list
							layerBelowOne.add(currentNode);
						}
					}
					// constructing the layer of the specified variable
					for (int j = 0; j < Math.pow(2, varIndex); j++) {
						currentNode = 
								layerBelowOne.poll().cons(this.varNr, layerBelowZero.poll(), this.varOrd);
						// adding the nodes to the list for the layers above
						layerBelow.add(currentNode);
					}
				}
				// Above the specified variable's layer each node has any two 
				// nodes from the layer below as its children.
				for (int i = varIndex - 1; i >= 0; i--) {
					for (int j = 0; j < Math.pow(2, i); j++) {
						currentNode = 
								layerBelow.poll().cons(this.varOrd.get(i), layerBelow.poll(), this.varOrd);
						// adding the nodes to the layer below list
						layerBelow.add(currentNode);
					}
				}
				// The constructed OBDD's root is returned.
				return currentNode;
			}
			// If the specified variable isn't part of the VariableOrdering 
			// (possibly because it's empty), just one node is constructed.
			else return OBDD.ONE.cons(this.varNr, OBDD.ZERO, this.varOrd);
		// Second case: The Formula represents a logical negation.
		case 2:
			// First the OBDD for the successor is constructed.
			OBDD successorOBDD = this.firstSuccessor.toOBDD();
			// returning the negated successor OBDD
			return successorOBDD.negate();
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// First the OBDDs for the two successors are constructed.
			OBDD firstSuccessorOBDD = this.firstSuccessor.toOBDD();
			OBDD secondSuccessorOBDD = this.secondSuccessor.toOBDD();
			// applying the logical conjunction "and" on the two successor 
			// OBDDs and returning the result;
			return firstSuccessorOBDD.apply(secondSuccessorOBDD, OBDD.AND, this.varOrd);
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// First the OBDDs for the two successors are constructed.
			firstSuccessorOBDD = this.firstSuccessor.toOBDD();
			secondSuccessorOBDD = this.secondSuccessor.toOBDD();
			// applying the logical disjunction "or" on the two successor OBDDs 
			// and returning the result;
			return firstSuccessorOBDD.apply(secondSuccessorOBDD, OBDD.OR, this.varOrd);
		// Default case: None of the given constructors was used.
		default:
			// tentative value: null
			// TODO user message
			return null;
		}
	}
	
	
	/**
	 * Evaluates the Formula relating to a given assignment.
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the Formula as a boolean
	 */
	public boolean assign(LinkedList<Integer> assignedOne) {
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
			firstSucVal = firstSuccessor.assign(assignedOne);
			// returning the negated value of the successor
			return !firstSucVal;
		// Third case: The Formula represents a logical conjunction.
		case 3:
			// calculating the value of the first successor
			firstSucVal = firstSuccessor.assign(assignedOne);
			// calculating the value of the second successor
			secondSucVal = secondSuccessor.assign(assignedOne);
			// returning the conjunction of the two successor values
			return firstSucVal && secondSucVal;
		// Fourth case: The Formula represents a logical disjunction.
		case 4:
			// calculating the value of the first successor
			firstSucVal = firstSuccessor.assign(assignedOne);
			// calculating the value of the second successor
			secondSucVal = secondSuccessor.assign(assignedOne);
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
			vars.addAll(firstSuccessor.vars());
			// getting all variable numbers of the second successor
			secondSucVars = secondSuccessor.vars();
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
	 * @param varOrd - the VariableOrdering
	 * @return the Formula's entire truth table
	 */
	public JTable entireTruthTable(VariableOrdering varOrd) {
		// returning the truth table for "all" variables
		return truthTable(varOrd.getOrdList());
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
			boolean funVal = assign(assignedOne);
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
