package datatypes;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author TimNP
 *
 */
public class OBDD {
	/**
	 * identifier for OBDD nodes (usage in computed tables)
	 */
	private int id;
	/**
	 * counter for the identifiers (0 and 1 are the terminals (constants)
	 */
	private static int idCount = 2;
	/**
	 * boolean which determines if the node is a terminal
	 */
	private boolean terminal;
	/**
	 * logical value for terminal nodes
	 */
	private boolean value;
	/**
	 * variable number for decision nodes
	 */
	private int var;
	/**
	 * list of variable numbers representing the OBDD'a variable ordering
	 */
	private VariableOrdering varOrd;
	/**
	 * HashMap for all of the OBDD's layers (except the terminal layer)
	 * Each layer is identified by its nodes' variable.
	 * Each layer is represented by a list of its nodes.
	 * For reasons of performance this HashMap is only updated when it's used.
	 */
	private HashMap<Integer,LinkedList<OBDD>> layers;
	/**
	 * list of all parents of the node
	 */
	private LinkedList<OBDD> parents;
	/**
	 * the (decision) node's high child
	 */
	private OBDD highChild;
	/**
	 * the (decision) node's low child
	 */
	private OBDD lowChild;
	
	
	/**
	 * the constant 1-terminal
	 */
	public static final OBDD ONE = one();
	
	/**
	 * the constant 0-terminal
	 */
	public static final OBDD ZERO = zero();
	
	/**
	 * all sixteen boolean functions as constants for Apply
	 */
	public static final int CONTRADICTION = 0;
	public static final int AND = 1;
	public static final int A_GREATER_THAN_B = 2;
	public static final int IDENTITY_OF_A = 3;
	public static final int B_GREATER_THAN_A = 4;
	public static final int IDENTITY_OF_B = 5;
	public static final int XOR = 6;
	public static final int OR = 7;
	public static final int NOR = 8;
	public static final int EQUIVALENCE = 9;
	public static final int NOT_B = 10;
	public static final int B_IMPLIES_A = 11;
	public static final int NOT_A = 12;
	public static final int A_IMPLIES_B = 13;
	public static final int NAND = 14;
	public static final int TAUTOLOGY = 15;
	
	/**
	 * computed table for the apply algorithm
	 */
	private static HashMap<Pair<Integer>, OBDD> applyCT = 
			new HashMap<Pair<Integer>, OBDD>();
	
	/**
	 * computed table for the negation algorithm
	 */
	private static HashMap<Integer,OBDD> negCT = 
			new HashMap<Integer,OBDD>();
	
	/**
	 * computed table for the toFormula method
	 */
	private static HashMap<Integer, Formula> formulaCT = 
			new HashMap<Integer, Formula>();
	
	/**
	 * computed table for the number algorithm
	 */
	private static HashMap<Integer, Integer> numberCT = 
			new HashMap<Integer, Integer>();
	
	/**
	 * computed table for the equivalence test
	 */
	private static HashMap<Pair<Integer>,Boolean> equivCT = 
			new HashMap<Pair<Integer>,Boolean>();
	
	/**
	 * list of variables assigned one for the satisfaction algorithm
	 */
	private static LinkedList<Integer> satAO = new LinkedList<Integer>();
	
	/**
	 * boolean value for the satisfaction algorithm
	 */
	private static boolean satVal;
	
	
	
	/**
	 * smart "constructor" for the 1-terminal
	 * @return the 1-terminal
	 */
	private static OBDD one() {
		OBDD one = new OBDD();
		one.id = 1;
		one.terminal = true;
		one.value = true;
		// defining the variable number for comparability
		one.var = -1;
		// A terminal has an empty variable ordering.
		one.varOrd = new VariableOrdering(new LinkedList<Integer>());
		return one;
	}
	
	
	/**
	 * smart "constructor" for the 0-terminal
	 * @return the 0-terminal
	 */
	private static OBDD zero() {
		OBDD zero = new OBDD();
		zero.id = 0;
		zero.terminal = true;
		zero.value = false;
		// defining the variable number for comparability
		zero.var = -1;
		// A terminal has an empty variable ordering.
		zero.varOrd = new VariableOrdering(new LinkedList<Integer>());
		return zero;
	}
	
	
	/**
	 * updates the node's layer HashMap
	 */
	private void updateLayers() {
		// initializing the new layer HashMap
		HashMap<Integer,LinkedList<OBDD>> layers =
				new HashMap<Integer,LinkedList<OBDD>>();
		// retrieving the OBDD's root node to begin with
		OBDD root = this.getRoot();
		// For each variable in the complete VariableOrdering a layer 
		// represented by a LinkedList is initialized.
		for (int var : root.varOrd.getOrdList()) {
			layers.put(var, new LinkedList<OBDD>());
		}
		// adding all of the OBBD's non-terminal nodes to the layer HashMap
		layers = root.addToLayerHashMap(layers);
		// saving the updated layer HashMap for this node
		this.layers = layers;
	}
	
	
	/**
	 * adds the node to its respective layer in a layer HashMap and
	 * lets it's children do the same if the node isn't a terminal 
	 * @param layers
	 * @return the updated layer HashMap
	 */
	private HashMap<Integer,LinkedList<OBDD>> addToLayerHashMap(HashMap<Integer,LinkedList<OBDD>> layers) {
		// if the node is a terminal, nothing is done
		if (!this.terminal) {
			// retrieving this node's layer list
			LinkedList<OBDD> layerList = layers.get(this.var);
			// adding the node to its layer list if it isn't already in
			if(!layerList.contains(this)) {
				layerList.add(this);
			}
			// putting the updated layer list into the layer HashMap
			layers.put(this.var, layerList);
			// recursively adding the high child
			layers = this.highChild.addToLayerHashMap(layers);
			// recursively adding the low child
			layers = this.lowChild.addToLayerHashMap(layers);
		}
		return layers;
	}	

	
	/**
	 * creates a new OBDD with this node as high child, a given variable,
	 * and a given node as low child
	 * @param variable
	 * @param lowChild
	 * @param varOrd - the complete VariableOrdering
	 * @return the new node
	 */
	public OBDD cons(int variable, OBDD lowChild, VariableOrdering varOrd) {
		// creating a VariableOrderingComparator for the complete 
		// VariableOrdering
		VariableOrderingComparator complVarOrdComp = 
				new VariableOrderingComparator(varOrd);
		// If the given variable is higher than the variables of both this and 
		// the given node, the new node can be created.
		if ((complVarOrdComp.compare(variable, this.var) > 0) && 
				(complVarOrdComp.compare(variable, lowChild.var) > 0)) {
			// initializing the new node
			OBDD newNode = new OBDD();
			// setting the new node's ID to the counter's current value and
			// increasing it by one
			newNode.id = idCount++;
			// The new node isn't a terminal since it's created with children.
			newNode.terminal = false;
			// The new node's variable ordering is the same as this node's.
			newNode.varOrd = this.varOrd;
			// The given variable becomes the new node's one.
			newNode.var = variable;
			// The new node's VariableOrdering is "cut off" 
			// before the node's variable.
			newNode.varOrd = varOrd.removeBefore(variable);
			// This node becomes the new node's high child.
			newNode.highChild = this;
			// The given node becomes the new node's low child.
			newNode.lowChild = lowChild;
			// The new node's parent list is initialized.
			newNode.parents = new LinkedList<OBDD>();
			// The new node becomes a parent of this node
			// if this node isn't a terminal.
			if (!this.terminal) {
				this.parents.add(newNode);			
			}
			// The new node becomes a parent of the given node.
			// if this node isn't a terminal.
			if (!lowChild.terminal) {
				lowChild.parents.add(newNode);			
			}
			return newNode;
		} else {
			// Otherwise the node can't be created.
			// TODO user message
			return null;
		}
		
	}
	
	
	/**
	 * function that provides the apply algorithm
	 * and clears the computed table before
	 * @param b
	 * @param op
	 * @param varOrd
	 * @return
	 */
	public OBDD apply(OBDD b, int op, VariableOrdering varOrd) {
		// clearing the computed table
		applyCT.clear();
		// calling the actual (recursive) apply algorithm
		OBDD result = applyRec(b, op, varOrd);
		// returning the result
		return result;
	}
	
	
	/**
	 * function that applies a boolean operation on two OBDDs
	 * (3.5.4)
	 * @param b - the second OBDD to apply the operation on
	 * @param op - the boolean operation
	 * @param varOrd - the complete VariableOrdering
	 * @return the resulting OBDD
	 */
	private OBDD applyRec(OBDD b, int op, VariableOrdering varOrd) {
		// If both OBDD nodes are terminals, the resulting terminal can be
		// calculated by means of the boolean operation.
		if (this.terminal && b.terminal) {
			// switch for the sixteen possible boolean operations
			switch (op) {
			// case 0
			case CONTRADICTION:
				// "applying" the contradiction: returning the 0-terminal
				return ZERO;
			// case 1
			case AND:
				// applying "and":
				// returning the 1-terminal if both nodes are the 1-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(this.value && b.value);
			// case 2
			case A_GREATER_THAN_B:
				// applying "a greater than b":
				// returning the 1-terminal if this node is the 1-terminal and
				// the other one is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(this.value && !b.value);
			// case 3
			case IDENTITY_OF_A:
				// "applying" the "identity of a": returning this node
				return this;
			// case 4
			case B_GREATER_THAN_A:
				// applying "b greater than a":
				// returning the 1-terminal if this node is the 0-terminal and
				// the other one is the 1-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(!this.value && b.value);
			// case 5
			case IDENTITY_OF_B:
				// "applying" the "identity of b": returning the other node
				return b;
			// case 6
			case XOR:
				// applying "xor":
				// returning the 1-terminal if one of the two nodes is the
				// 1-terminal and the other one is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(this.value ^ b.value);
			// case 7
			case OR:
				// applying "or":
				// returning the 1-terminal if at least one of the two nodes
				// is the 1-terminal, and otherwise the 0-terminal
				return booleanToOBDD(this.value || b.value);
			// case 8
			case NOR:
				// applying "nor":
				// returning the 1-terminal if none of the two nodes is the
				// 1-terminal, and otherwise the 0-terminal
				return booleanToOBDD(!(this.value || b.value));
			// case 9
			case EQUIVALENCE:
				// applying "equivalence"
				// return the 1-terminal if the two nodes are the same,
				// and otherwise the 0-terminal
				return booleanToOBDD(this.value == b.value);
			// case 10
			case NOT_B:
				// applying "not b":
				// returning the 1-terminal if b is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(!b.value);
			// case 11
			case B_IMPLIES_A:
				// applying "b implies a":
				// returning the 1-terminal if this node is the 1-terminal or
				// the other one is the 0-terminal (or both),
				// and otherwise the 0-terminal
				return booleanToOBDD(this.value || !b.value);
			// case 12
			case NOT_A:
				// applying "not a":
				// returning the 1-terminal if this node is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToOBDD(!this.value);
			// case 13
			case A_IMPLIES_B:
				// applying "a implies b":
				// returning the 1-terminal if this node is the 0-terminal
				// or the other one is the 1-terminal (or both),
				// and otherwise the 0-terminal
				return booleanToOBDD(!this.value || b.value);
			// case 14
			case NAND:
				// applying "nand":
				// returning the 1-terminal if at least one of the two
				// nodes is the 0-terminal, and otherwise the 0-terminal
				return booleanToOBDD(!(this.value && b.value));
			// case 15
			case TAUTOLOGY:
				 // "applying" the tautology: returning the 1-terminal
				return ONE;
			// Default case: None of the sixteen boolean operations was given.
			default:
				// tentative default value: the 0-terminal
				//TODO user message
				return ZERO;
			}
		}
		// If neither OBDD node is a terminal, 
		// first the computed table is checked.
		else {
			// initializing a pair of the two OBDD nodes
			Pair<Integer> applyPair = new Pair<Integer>(this.id, b.id);
			// Return the OBDD stated for the two nodes in the computed table
			// if there is one.
			if (applyCT.containsKey(applyPair)) {
				return applyCT.get(applyPair);
			} else {
				// If both nodes have the same variable,
				// both nodes' children can be called recursively.
				if (!this.terminal && !b.terminal && this.var == b.var) {
					// applying the operation on the both nodes' high children
					OBDD applyHighChildren = 
							this.highChild.applyRec(b.highChild, op, varOrd);
					// applying the operation on the both nodes' low children
					OBDD applyLowChildren = 
							this.lowChild.applyRec(b.lowChild, op, varOrd);
					// combining the two resulting nodes
					OBDD newNode = 
							applyHighChildren.cons(this.var, applyLowChildren, varOrd);
					// putting the resulting node for the two nodes
					// into the computed table
					applyCT.put(applyPair, newNode);
					// returning the node
					return newNode;
				}
				else {
					// creating a VariableOrderingComparator for the complete 
					// VariableOrdering
					VariableOrderingComparator complVarOrdComp = 
							new VariableOrderingComparator(varOrd);
					// If this OBDD node isn't a terminal and it's variable has 
					// a higher position in the variable ordering than the 
					// other node's one, only this node's children are called 
					// recursively (here).
					if (!this.terminal && (complVarOrdComp.compare(this.var, b.var) > 0)) {
					// applying the operation on this node's high child
					// and the other node
					OBDD applyHighChild = 
							this.highChild.applyRec(b, op, varOrd);
					// applying the operation on this node's low child
					// and the other node
					OBDD applyLowChild = this.lowChild.applyRec(b, op, varOrd);
					// combining the two resulting nodes
					OBDD newNode = 
							applyHighChild.cons(this.var, applyLowChild, varOrd);
					// putting the resulting node for the two nodes
					// into the computed table
					applyCT.put(applyPair, newNode);
					// returning the node
					return newNode;
					}
					// Otherwise the other node isn't a terminal and it's variable 
					// has a higher position in the variable ordering than this 
					// node's one. So only the other node's children are called 
					// recursively (here).
					else {
						// applying the operation on this node
						// and the other node's high child
						OBDD applyHighChild = 
								this.applyRec(b.highChild, op, varOrd);
						// applying the operation on this node
						// and the other node's low child
						OBDD applyLowChild = this.applyRec(b.lowChild, op, varOrd);
						// combining the two resulting nodes
						OBDD newNode = 
								applyHighChild.cons(this.var, applyLowChild, varOrd);
						// putting the resulting node for the two nodes
						// into the computed table
						applyCT.put(applyPair, newNode);
						// returning the node
						return newNode;
					}
				}				
			}
		}
	}
	
	
	/**
	 * auxiliary function that "turns" a boolean into an OBDD
	 * @param bool
	 * @return the corresponding terminal
	 */
	private OBDD booleanToOBDD(boolean bool) {
		if (bool) {
			return ONE;
		} else return ZERO;
	}
	
	
	/**
	 * @return this entire OBDD's root
	 */
	private OBDD getRoot() {
		// initializing the "root" as this node
		OBDD root = this;
		// As long as the "root" has any parents, it isn't the real root
		// and is replaced by one of its parents.
		while (!root.parents.isEmpty()) {
			root = root.parents.getFirst();
		}
		// returning this entire OBDD's node without parents 
		// and therefore its root
		return root;
	}
	
	
	/**
	 * method that provides the negation algorithm on OBDDs
	 * and clears the computed table before
	 * @return
	 */
	public OBDD negate() {
		// clearing the computed table
		negCT.clear();
		// calling the actual (recursive) negate algorithm
		OBDD result = negateRec();
		// returning the result
		return result;
	}
	
	
	/**
	 * method that negates an OBDD
	 * (3.4.5)
	 * @return the negated OBDD
	 */
	private OBDD negateRec() {
		// If the node is a terminal, it's negation is the opposite terminal.
		if (this.terminal) {
			OBDD oppositeTerminal = booleanToOBDD(!this.value);
			return oppositeTerminal;
		}
		else {
			// Return the OBDD stated for this node in the computed table 
			// (if there is one).
			if (negCT.containsKey(this.id)) {
				return negCT.get(this.id);
			} else {
				// negating the high child
				OBDD negHC = this.highChild.negate();
				// negating the low child
				OBDD negLC = this.lowChild.negate();
				// combining the two negated children
				OBDD neg = negHC.cons(this.var, negLC, this.varOrd);
				// putting the negated node into the computed table
				negCT.put(this.id, neg);
				// returning the negated node
				return neg;
			}
		}
	}
	
	
	/**
	 * method that provides the satisfaction algorithm 
	 * and clears the assignment and the value before
	 * @return
	 */
	public LinkedList<Integer> satisfy() {
		// clearing the assignment and the value
		satAO.clear();
		satVal = false;
		// calling the actual (recursive) satisfy algorithm
		satisfyRec();
		// If the value is one, the assignment currently stored in satAO
		// is a satisfying one and therefore returned.
		if (satVal) {
			return satAO;
		} else {
			// tentative value: null
			// TODO user message
			return null;
		}
	}
	
	
	/**
	 * method that provides a satisfying assignment for this OBDD (if possible)
	 * (3.2.3)
	 */
	private void satisfyRec() {
		// If the node is a terminal, it's value is returned.
		if (this.terminal) {
			satVal = this.value;
		} else {
			// First the node's variable is assigned one
			satAO.add(this.var);
			// The search continues from the high child on.
			this.highChild.satisfyRec();
			if (!satVal) {
				// If the current assignment isn't a satisfying one,
				// zero is tested for this node's variable.
				satAO.removeFirstOccurrence(this.var);
				// The search then continues from the low child on.
				this.lowChild.satisfyRec();
			}
		}
	}
	
	
	/**
	 * function that provides the toFormula method 
	 * and clears the computed table before
	 * @return
	 */
	public Formula toFormula() {
		// clearing the computed table
		formulaCT.clear();
		// calling the actual (recursive) toFormula method
		Formula result = toFormulaRec(this.varOrd);
		// reducing constants
		Formula resultRC = result.reduceConstants();
		// returning the result
		return resultRC;
	}
	
	
	/**
	 * @param completeVarOrd - the complete VariableOrdering
	 * @return the Formula represented by the OBDD 
	 */
	private Formula toFormulaRec(VariableOrdering completeVarOrd) {
		// In the case of a terminal, a constant Formula is constructed.
		if (this.terminal) {
			if (this.value) {
				// returning the tautological Formula if the node is the
				// 1-terminal
				return new Formula(true, completeVarOrd);
			}
			else {
				// returning the contradictory Formula if the node is the
				// 0-terminal
				return new Formula(false, completeVarOrd);
			}
		}
		else {
			// Return the Formula stated in the computed table for this node
			// (if there is one).
			if (formulaCT.containsKey(this.id)) {
				return formulaCT.get(this.id);
			} else {
				// Formula representing the node's variable
				Formula xn = new Formula(this.var, completeVarOrd);
				// Formula represented by the OBDD induced by the node's 
				// high child
				Formula hcFormula = 
						this.highChild.toFormulaRec(completeVarOrd);
				// Formula represented by the OBDD induced by the node's 
				// low child
				Formula lcFormula = this.lowChild.toFormulaRec(completeVarOrd);
				// the left half of the Shannon expansion
				Formula shannonLeft = xn.and(hcFormula, completeVarOrd);
				// the right half of the Shannon expansion
				Formula shannonRight = 
						xn.not(completeVarOrd).and(lcFormula, completeVarOrd);
				// Shannon expansion
				Formula shannon = shannonLeft.or(shannonRight);
				// putting the Formula into the computed table
				formulaCT.put(this.id, shannon);
				// returning the Formula
				return shannon;
			}
		}
	}
	
	
	/**
	 * method that provides the evaluation of the Formula represented by the 
	 * OBDD relating to a given assignment but sorts the assignment list before
	 * @param assignedOne
	 * @return the value of the Formula
	 */
	public boolean valueByOBDD(LinkedList<Integer> assignedOne) {
		// creating a comparator for the complete VariableOrdering
		VariableOrderingComparator complVarOrdComp = 
				new VariableOrderingComparator(this.varOrd);
		// sorting the list of variables assigned one by means of the 
		// VariableOrdering
		Collections.sort(assignedOne, complVarOrdComp);
		// calling the actual (recursive) algorithm
		return valueByOBDDRec(assignedOne, complVarOrdComp);
	}
	
	
	/**
	 * Evaluates the formula represented by the OBDD relating to a given
	 * assignment.
	 * (3.2.1)
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the formula as a boolean
	 */
	public boolean valueByOBDDRec(LinkedList<Integer> assignedOne, 
			VariableOrderingComparator complVarOrdComp) {
		if (this.terminal) {
			// If the node is a terminal, its value is returned.
			return this.value;
		}
		// Otherwise the OBDD is run through as directed by the assignment.
		else {
			// As long as the first variable in the list of the variables 
			// assigned one is higher by means of the VariableOrdering than 
			// this node's one, it is removed from the list.
			while (complVarOrdComp.compare(assignedOne.getFirst(), this.var) > 0) {
				assignedOne.removeFirst();
			}
			if (assignedOne.getFirst() == this.var) {
				// If the node's variable is assigned one, first it is removed 
				// from the list.
				assignedOne.removeFirst();
				// Then the high child gets to continue the calculation.
				return this.highChild.valueByOBDDRec(assignedOne, complVarOrdComp);
			}
			else {
				// Otherwise the node's variable is assigned zero and therefore
				// the low child gets to continue the calculation.
				return this.lowChild.valueByOBDDRec(assignedOne, complVarOrdComp);
			}
		}
	}
	
	
	/**
	 * Evaluates the Formula represented by the OBDD relating to a given
	 * assignment.
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the Formula as a boolean
	 */
	public boolean valueByFormula(LinkedList<Integer> assignedOne) {
		// using the "assign" function of the Formula data type
		return this.toFormula().assign(assignedOne);
	}

	
	/**
	 * function that provides the number algorithm 
	 * and clears the computed table before
	 * @return
	 */
	public int number() {
		// clearing the computed table
		numberCT.clear();
		// calling the actual (recursive) number algorithm
		int result = numberRec();
		// returning the result
		return result;
	}
	
	
	/**
	 * function that provides the number of satisfying assignments for the OBDD
	 * @return the number of satisfying assignments
	 */
	private int numberRec() {
		// If the node is a terminal, the number of satisfying assignments can
		// easily be returned.
		if (this.terminal) {
			if (this.value) {
				// The 1-terminal has exactly one satisfying assignment: the
				// empty one.
				return 1;
			}
			else {
				// The 0-terminal doesn't have any satisfying assignments.
				return 0;
			}
		}
		else {
			// Return the number stated for this node in the computed table
			// (if there is one).
			if (numberCT.containsKey(this.id)) {
				return numberCT.get(this.id);
			}
			// Otherwise the number of satisfying assignments is calculated by 
			// summing the numbers of satisfying assignments for both children.
			else {
				// position of this node's variable in the variable ordering
				int varPos = this.varOrd.indexOf(this.var);
				// position of the high child's variable in the variable ordering
				// (The variable ordering has to be the same in the entire OBDD.)
				int hcVarPos = this.varOrd.indexOf(this.highChild.var);
				// position of the low child's variable in the variable ordering
				int lcVarPos = this.varOrd.indexOf(this.lowChild.var);
				// the high child's number of satisfying assignments 
				int hcNumber = this.highChild.numberRec();
				// the low child's number of satisfying assignments
				int lcNumber = this.lowChild.numberRec();
				// Satz 3.2.6
				int number = (int) ((hcNumber * Math.pow(2, hcVarPos - varPos) 
						+ lcNumber * Math.pow(2, lcVarPos - varPos)) / 2);
				// putting the number for this node into the computed table
				numberCT.put(this.id, number);
				// returning the number
				return number;
			}
		}
	}
	
	
	/**
	 * 
	 * @return a list with two equivalent nodes in the OBDD if possible
	 */
	public LinkedList<OBDD> findEquivalent() {
		// updating the layer HashMap
		this.updateLayers();
		// initializing the list for the equivalent nodes
		LinkedList<OBDD> candidates = new LinkedList<OBDD>();
		// initializing the list of the current layer's nodes
		// which are possibly equivalent to another one
		LinkedList<OBDD> residualLayerList;
		// trying to find equivalent nodes in each layer individually
		for (int var : this.varOrd.getOrdList()) {
			// getting the current layer list
			residualLayerList = this.layers.get(var);
			// While there are two or more nodes in the layer list,
			// they might be equivalent.
			while (residualLayerList.size()>=2) {
				// moving the first node from the residual layer list
				// to the candidate list
				candidates.add(residualLayerList.removeFirst());
				// checking for each node of the residual layer list
				// whether it's equivalent to the first candidate node
				for (OBDD secondCandidate : residualLayerList) {
					// If the two nodes are equivalent,
					// return the list with the two of them.
					if (candidates.getFirst().isEquivalent(secondCandidate)) {
						candidates.add(secondCandidate);
						return candidates;
					}
					// clearing the candidate list
					candidates.clear();
				}
				// If no equivalent node could be found for this one,
				// check for other ones.
			}
			// otherwise checking other layers
		}
		// At this point there are no equivalent nodes.
		//TODO user message
		return null;
	}
	
	
	/**
	 * function that provides the equivalence test 
	 * and clears the computed table before
	 * @param otherNode
	 * @return
	 */
	public boolean isEquivalent(OBDD otherNode) {
		// clearing the computed table
		equivCT.clear();
		// calling the actual (recursive) equivalence test
		boolean result = isEquivalentRec(otherNode);
		// returning the result
		return result;
	}
	
	
	/**
	 * @param otherNode
	 * @return whether another node is equivalent to this node
	 */
	private boolean isEquivalentRec(OBDD otherNode) {
		// If at least one of the two nodes is a terminal,
		// they aren't equivalent unless they're the same.
		if (this.terminal || otherNode.terminal) {
			return (this.id == otherNode.id);
		}
		else {
			// initializing a pair of the two OBDDs
			Pair<Integer> checkPair = new Pair<Integer>(this.id,otherNode.id);
			// Return the value stated for the two nodes in the computed table
			// if there is one.
			if (equivCT.containsKey(checkPair)) {
				return equivCT.get(checkPair);
			} else {
				// For equivalence the two high children have to be equivalent.
				boolean equivalentHC =
						this.highChild.isEquivalentRec(otherNode.highChild);
				// For equivalence the two low children have to be equivalent.
				boolean equivalentLC =
						this.lowChild.isEquivalentRec(otherNode.lowChild);
				// For equivalence the two variables have to be equivalent.
				boolean equivalentVar = this.var == otherNode.var;
				// combining all three criteria
				boolean equivalent =
						(equivalentHC && equivalentLC && equivalentVar);
				// putting the value for the two nodes into the computed table
				equivCT.put(checkPair, equivalent);
				// finally returning the value
				return equivalent;
			}
		}
	}
	
	
	/**
	 * method that provides the recursive findRedundantRec method for the 
	 * entire OBDD
	 * @return
	 */
	public OBDD findRedundant() {
		// starting the (recursive) search at the entire OBDD's root
		return this.getRoot().findRedundantRec();
	}
	
	
	/**
	 * method that provides a redundant node of this OBDD node's sub-OBDD
	 * (if possible)
	 * @return a redundant node (if possible)
	 */
	public OBDD findRedundantRec() {
		// If the node is a terminal, the search has failed.
		if (this.terminal) {
			// TODO user message
			// tentative value: null
			return null;
		}
		// Otherwise the node has children.
		else if (this.isRedundant()) {
			// If the node is redundant, it is returned.
			return this;
		}
		// If the node itself isn't redundant, the search is continued for its
		// children recursively.
		else {
			// trying to find a redundant node along the high child's paths
			OBDD redundantFind = this.highChild.findRedundantRec();
			// If the search along the high child's paths didn't provide a 
			// redundant node, the search is continued along the low child's
			// paths.
			if ((redundantFind == null)) {
				redundantFind = this.lowChild.findRedundantRec();
			}
			// returning the "find"
			return redundantFind;
		}
	}
	
	
	/**
	 * method that states whether the OBDD node is redundant
	 * @return
	 */
	public boolean isRedundant() {
		// A node is redundant, if it's children are the same (which can be 
		// indicated by their IDs).
		return (this.highChild.id == this.lowChild.id);
	}
	
	
	/**
	 * method that states, whether the entire OBDD is a QOBDD
	 * @return 
	 */
	public boolean isQOBDD() {
		if (!(this.findEquivalent() == null)) {
			// If there are any equivalent nodes, the OBDD isn't a QOBDD.
			return false;
		} else {
			// In a QOBDD each path from the root to a terminal has to include 
			// each variable of the complete VariableOrdering.
			// The checking is started at the root.
			OBDD root = this.getRoot();
			// an ordered list of "all" variables
			LinkedList<Integer> varOrdList = root.varOrd.getOrdList();
			// checking, whether all variables are on each path from the root 
			// to a terminal
			return root.noVarMissing(varOrdList);
		}
	}
	
	
	/**
	 * auxiliary function that states, whether in all paths from this node on 
	 * there is no variable of a given list missing
	 * @param varOrdList
	 * @return
	 */
	private boolean noVarMissing(LinkedList<Integer> varOrdList) {
		// If the node is a terminal, no variable is missing on this path.
		if (this.terminal) {
			return true;
		} else if (!(this.var == varOrdList.poll())) {
			// If the first variable in the ordered variable list isn't the one 
			// of this node, it is missing on this path.
			return false;
		}
		// Otherwise the node's children are checked recursively.
		else return (this.highChild.noVarMissing(varOrdList) && 
				this.lowChild.noVarMissing(varOrdList));
	}
	
	
	/**
	 * method that states, whether the entire OBDD is an ROBDD
	 * @return
	 */
	public boolean isROBDD() {
		if (!(this.findEquivalent() == null)) {
			// If there are any equivalent nodes, the OBDD isn't an ROBDD.
			return false;
		}
		// An OBDD is an ROBBD if there are no equivalent nodes and no 
		// redundant nodes.
		else return ((this.findRedundant() == null));
	}
}
