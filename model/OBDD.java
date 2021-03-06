package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
	 * counter for the identifiers (0 and 1 are the terminals (constants))
	 */
	private static int idCount = 2;
	/**
	 * boolean which determines whether the node is a terminal
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
	 * HashMap for all of the OBDD's layers (except the terminal layer)
	 * Each layer is identified by its nodes' variable.
	 * Each layer is represented by a list of its nodes.
	 * For reasons of performance this HashMap is only updated when it's used.
	 */
	private HashMap<Integer,LinkedList<OBDD>> layers;
	/**
	 * the (decision) node's high child
	 */
	private OBDD highChild;
	/**
	 * the (decision) node's low child
	 */
	private OBDD lowChild;
	/**
	 * the node's (optional) name
	 */
	private String name;
	
	
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
	 * computed table for the getNode method
	 */
	private static HashMap<Integer,OBDD> getNodeCT = 
			new HashMap<Integer,OBDD>();
	
	/**
	 * computed table for the apply algorithm
	 */
	private static HashMap<Pair<Integer, Integer>, OBDD> applyCT = 
			new HashMap<Pair<Integer, Integer>, OBDD>();
	
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
	private static HashMap<Pair<Integer, Integer>,Boolean> equivCT = 
			new HashMap<Pair<Integer, Integer>,Boolean>();
	
	/**
	 * list of variables assigned one for the satisfaction algorithm
	 */
	private static LinkedList<Integer> satAO = new LinkedList<Integer>();
	
	/**
	 * boolean value for the satisfaction algorithm
	 */
	private static boolean satVal;
	
	/**
	 * computed table for the merge method
	 */
	private static HashMap<Integer, OBDD> removeAndMergeCT = 
			new HashMap<Integer, OBDD>();
	
	/**
	 * computed table for the "allSat" algorithm
	 */
	private static HashMap<Integer, LinkedList<LinkedList<Integer>>> allSatCT =
			new HashMap<Integer, LinkedList<LinkedList<Integer>>>();
	
	
	
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
		// defining the name for outputs
		one.name = "1";
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
		// defining the name for outputs
		zero.name = "0";
		return zero;
	}
	
	
	/**
	 * updates the node's layer HashMap and returns it
	 * @param root - the OBDD's root
	 * @return 
	 */
	public HashMap<Integer, LinkedList<OBDD>> updateLayers(OBDD root) {
		// initializing the new layer HashMap
		HashMap<Integer,LinkedList<OBDD>> layers =
				new HashMap<Integer,LinkedList<OBDD>>();
		// adding all of the OBBD's non-terminal nodes to the layer HashMap
		layers = root.addToLayerHashMap(layers);
		// saving the updated layer HashMap for this node
		this.layers = layers;
		return layers;
	}
	
	
	/**
	 * adds the node to its respective layer in a layer HashMap and
	 * lets it's children do the same if the node isn't a terminal 
	 * @param layerMap
	 * @return the updated layer HashMap
	 */
	private HashMap<Integer,LinkedList<OBDD>> 
	addToLayerHashMap(HashMap<Integer,LinkedList<OBDD>> layerMap) {
		// if the node is a terminal, nothing is done
		if (!terminal) {
			// retrieving this node's layer list
			LinkedList<OBDD> layerList = layerMap.get(var);
			// initializing an empty layer list, if there is none for this 
			// node's layer
			if (layerList == null) {
				layerList = new LinkedList<OBDD>();
			}
			// adding the node to its layer list if it isn't already in
			if(!layerList.contains(this)) {
				layerList.add(this);
			}
			// putting the updated layer list into the layer HashMap
			layerMap.put(var, layerList);
			// recursively adding the high child
			layerMap = highChild.addToLayerHashMap(layerMap);
			// recursively adding the low child
			layerMap = lowChild.addToLayerHashMap(layerMap);
		}
		return layerMap;
	}
	
	
	/**
	 * getter for the OBDD's ID
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * getter that states whether the node is a terminal
	 * @return
	 */
	public boolean isTerminal() {
		return terminal;
	}
	
	
	/**
	 * getter for the OBDD's variable
	 * @return
	 */
	public int getVar() {
		return var;
	}
	
	
	/**
	 * getter for the OBDD's layer HashMap
	 * @return
	 */
	public HashMap<Integer, LinkedList<OBDD>> getLayers() {
		// updating the layer HashMap first
		updateLayers(this);
		return layers;
	}
	
	
	/**
	 * getter for the node's high child
	 * @return
	 */
	public OBDD getHighChild() {
		return highChild;
	}
	
	
	/**
	 * getter for the node's low child
	 * @return
	 */
	public OBDD getLowChild() {
		return lowChild;
	}
	
	
	/**
	 * getter for the node's (optional) name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * method that finds a specific node in this entire OBDD (if possible)
	 * @param id - the node's ID
	 * @return the node, if it's found; else null
	 */
	public OBDD getNode(int id) {
		if (getNodeCT.containsKey(this.id)) {
			// If there is already a result for this node, it gets returned.
			return getNodeCT.get(this.id);
		}
		// Otherwise a result for this node has to be found.
		else {
			// If the specified ID is a terminal's one, the terminal is 
			// returned.
			if (id == 0) return ZERO;
			if (id == 1) return ONE;
			// If this node has the specified ID, it is returned.
			if (this.id == id) return this;
			// Initializing a variable for the found node with null.
			OBDD find = null;
			if (!this.terminal) {
				// If the node isn't the specified one and isn't a terminal, 
				// first the high child gets called recursively.
				find = highChild.getNode(id);
				// If the high child's call didn't return a node, the low child
				// gets called recursively.
				if (find == null) find = lowChild.getNode(id);
			}
			// returning the find
			return find;
		}
	}
	
	
	/**
	 * provides the cons algorithm without the possibility of choosing an ID
	 * @param variable
	 * @param lowChild
	 * @param varOrd - the VariableOrdering
	 * @return the new node
	 */
	public OBDD cons(int variable, OBDD lowChild, VariableOrdering varOrd) {
		return cons(-1, variable, lowChild, varOrd);
	}

	
	/**
	 * creates a new OBDD with this node as high child, a given ID, 
	 * a given variable, and a given node as low child (if the given variable 
	 * is greater than the variables of the two nodes by means of the 
	 * VariableOrdering)
	 * @param id
	 * @param variable
	 * @param lowChild
	 * @param varOrd - the VariableOrdering
	 * @return the new node
	 */
	private OBDD cons(int id, int variable, OBDD lowChild, VariableOrdering varOrd) {
		// creating a VariableOrderingComparator for the VariableOrdering
		VarOrdComparator complVarOrdComp = 
				new VarOrdComparator(varOrd);
		// If the given variable is higher than the variables of both this and 
		// the given node, the new node can be created.
		if ((complVarOrdComp.compare(variable, var) > 0) && 
				(complVarOrdComp.compare(variable, lowChild.var) > 0)) {
			// initializing the new node
			OBDD newNode = new OBDD();
			// setting the new node's ID to the counter's current value and
			// increasing it by one, if there is no adequate ID given
			if (id < 2) newNode.id = idCount++;
			// otherwise setting the new node's ID to the given one
			else newNode.id = id;
			// The new node isn't a terminal since it's created with children.
			newNode.terminal = false;
			// The given variable becomes the new node's one.
			newNode.var = variable;
			// This node becomes the new node's high child.
			newNode.highChild = this;
			// The given node becomes the new node's low child.
			newNode.lowChild = lowChild;
			return newNode;
		} else {
			// Otherwise the node can't be created.
			return null;
		}
	}
	
	
	/**
	 * provides the apply algorithm and clears the computed table before
	 * @param otherNode - the second OBDD to apply the operation on
	 * @param op - the boolean operation
	 * @param varOrd - the VariableOrdering (used for the cons operation)
	 * @return
	 */
	public OBDD apply(OBDD otherNode, int op, VariableOrdering varOrd) {
		// clearing the computed table
		applyCT.clear();
		// calling the actual (recursive) apply algorithm
		return applyRec(otherNode, op, varOrd);
	}
	
	
	/**
	 * applies a boolean operation on two OBDDs
	 * (3.5.4)
	 * @param otherNode - the second OBDD to apply the operation on
	 * @param op - the boolean operation
	 * @param varOrd - the VariableOrdering (used for the cons operation and 
	 * 					for comparing the two nodes' variables)
	 * @return the resulting OBDD
	 */
	private OBDD applyRec(OBDD otherNode, int op, VariableOrdering varOrd) {
		// If both OBDD nodes are terminals, the resulting terminal can be
		// calculated by means of the boolean operation.
		if (terminal && otherNode.terminal) {
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
				return booleanToObdd(value && otherNode.value);
			// case 2
			case A_GREATER_THAN_B:
				// applying "a greater than b":
				// returning the 1-terminal if this node is the 1-terminal and
				// the other one is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToObdd(value && !otherNode.value);
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
				return booleanToObdd(!value && otherNode.value);
			// case 5
			case IDENTITY_OF_B:
				// "applying" the "identity of b": returning the other node
				return otherNode;
			// case 6
			case XOR:
				// applying "xor":
				// returning the 1-terminal if one of the two nodes is the
				// 1-terminal and the other one is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToObdd(value ^ otherNode.value);
			// case 7
			case OR:
				// applying "or":
				// returning the 1-terminal if at least one of the two nodes
				// is the 1-terminal, and otherwise the 0-terminal
				return booleanToObdd(value || otherNode.value);
			// case 8
			case NOR:
				// applying "nor":
				// returning the 1-terminal if none of the two nodes is the
				// 1-terminal, and otherwise the 0-terminal
				return booleanToObdd(!(value || otherNode.value));
			// case 9
			case EQUIVALENCE:
				// applying "equivalence"
				// return the 1-terminal if the two nodes are the same,
				// and otherwise the 0-terminal
				return booleanToObdd(value == otherNode.value);
			// case 10
			case NOT_B:
				// applying "not b":
				// returning the 1-terminal if b is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToObdd(!otherNode.value);
			// case 11
			case B_IMPLIES_A:
				// applying "b implies a":
				// returning the 1-terminal if this node is the 1-terminal or 
				// the other one is the 0-terminal (or both),
				// and otherwise the 0-terminal
				return booleanToObdd(value || !otherNode.value);
			// case 12
			case NOT_A:
				// applying "not a":
				// returning the 1-terminal if this node is the 0-terminal,
				// and otherwise the 0-terminal
				return booleanToObdd(!value);
			// case 13
			case A_IMPLIES_B:
				// applying "a implies b":
				// returning the 1-terminal if this node is the 0-terminal
				// or the other one is the 1-terminal (or both),
				// and otherwise the 0-terminal
				return booleanToObdd(!value || otherNode.value);
			// case 14
			case NAND:
				// applying "nand":
				// returning the 1-terminal if at least one of the two
				// nodes is the 0-terminal, and otherwise the 0-terminal
				return booleanToObdd(!(value && otherNode.value));
			// case 15
			case TAUTOLOGY:
				 // "applying" the tautology: returning the 1-terminal
				return ONE;
			// Default case: None of the sixteen boolean operations was given.
			default:
				// tentative default value: null
				return null;
			}
		}
		// If neither OBDD node is a terminal, 
		// first the computed table is checked.
		else {
			// initializing a pair of the two OBDD nodes
			Pair<Integer, Integer> applyPair = 
					new Pair<Integer, Integer>(id, otherNode.id);
			// Return the OBDD stated for the two nodes in the computed table
			// if there is one.
			if (applyCT.containsKey(applyPair)) {
				return applyCT.get(applyPair);
			} else {
				// If both nodes have the same variable,
				// both nodes' children can be called recursively.
				if (!terminal && !otherNode.terminal && var == otherNode.var) {
					// applying the operation on the both nodes' high children
					OBDD applyHighChildren = highChild.applyRec
							(otherNode.highChild, op, varOrd);
					// applying the operation on the both nodes' low children
					OBDD applyLowChildren = 
							lowChild.applyRec(otherNode.lowChild, op, varOrd);
					// combining the two resulting nodes
					OBDD newNode = 
							applyHighChildren.cons
							(var, applyLowChildren, varOrd);
					// naming the new node
					newNode.name = nameApplyNode(op, otherNode);
					// putting the resulting node for the two nodes
					// into the computed table
					applyCT.put(applyPair, newNode);
					// returning the node
					return newNode;
				}
				else {
					// creating a VariableOrderingComparator for the 
					// VariableOrdering
					VarOrdComparator complVarOrdComp = 
							new VarOrdComparator(varOrd);
					// If this OBDD node isn't a terminal and it's variable has
					// a higher position in the variable ordering than the 
					// other node's one, only this node's children are called 
					// recursively (here).
					if (!terminal && 
							(complVarOrdComp.compare(var, otherNode.var) > 0))
					{
					// applying the operation on this node's high child
					// and the other node
					OBDD applyHighChild = 
							highChild.applyRec(otherNode, op, varOrd);
					// applying the operation on this node's low child
					// and the other node
					OBDD applyLowChild = 
							lowChild.applyRec(otherNode, op, varOrd);
					// combining the two resulting nodes
					OBDD newNode = 
							applyHighChild.cons(var, applyLowChild, varOrd);
					// naming the new node
					newNode.name = nameApplyNode(op, otherNode);
					// putting the resulting node for the two nodes
					// into the computed table
					applyCT.put(applyPair, newNode);
					// returning the node
					return newNode;
					}
					// Otherwise the other node isn't a terminal and it's 
					// variable has a higher position in the variable ordering 
					// than this node's one. So only the other node's children 
					// are called recursively (here).
					else {
						// applying the operation on this node
						// and the other node's high child
						OBDD applyHighChild = 
								applyRec(otherNode.highChild, op, varOrd);
						// applying the operation on this node
						// and the other node's low child
						OBDD applyLowChild = 
								applyRec(otherNode.lowChild, op, varOrd);
						// combining the two resulting nodes
						OBDD newNode = applyHighChild.cons
								(var, applyLowChild, varOrd);
						// naming the new node
						newNode.name = nameApplyNode(op, otherNode);
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
	private OBDD booleanToObdd(boolean bool) {
		if (bool) {
			return ONE;
		} else return ZERO;
	}

	
	/**
	 * method that provides names for nodes created via the Apply method
	 * @param op
	 * @param otherNode
	 * @return
	 */
	private String nameApplyNode(int op, OBDD otherNode) {
		// stating the use of the Apply method
		String applyName = "Apply(";
		// a switch for all possible binary operations to add the used one to 
		// the String
		switch(op) {
		case CONTRADICTION:
			applyName += "0";
		case AND:
			applyName += "A * B";
		case A_GREATER_THAN_B:
			applyName += "A > B";
		case IDENTITY_OF_A:
			applyName += "A";
		case B_GREATER_THAN_A:
			applyName += "A < B";
		case IDENTITY_OF_B:
			applyName += "B";
		case XOR:
			applyName += "A ^ B";
		case OR:
			applyName += "A + B";
		case NOR:
			applyName += "-(A + B)";
		case EQUIVALENCE:
			applyName += "A = B";
		case NOT_B:
			applyName += "-B";
		case B_IMPLIES_A:
			applyName += "A <- B";
		case NOT_A:
			applyName += "-A";
		case A_IMPLIES_B:
			applyName += "a -> B";
		case NAND:
			applyName += "-(A * B)";
		case TAUTOLOGY:
			applyName += "1";
		default:
			// If none of the possible binary operations was given, nothing is 
			// added.
		}
		// adding the other parameters of the Apply method: the two nodes
		applyName += "," + name + "," + otherNode.name + ")";
		// returning the complete String
		return applyName;
	}
	
	
	/**
	 * provides the negation algorithm on OBDDs
	 * and clears the computed table before
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public OBDD negate(VariableOrdering varOrd) {
		// clearing the computed table
		negCT.clear();
		// calling the actual (recursive) negate algorithm
		return negateRec(varOrd);
	}
	
	
	/**
	 * negates an OBDD
	 * (3.4.5)
	 * @param varOrd - the VariableOrdering (used for the cons operation)
	 * @return the negated OBDD
	 */
	private OBDD negateRec(VariableOrdering varOrd) {
		// If the node is a terminal, it's negation is the opposite terminal.
		if (terminal) {
			return booleanToObdd(!value);
		}
		else {
			// Return the OBDD stated for this node in the computed table 
			// (if there is one).
			if (negCT.containsKey(id)) {
				return negCT.get(id);
			} else {
				// negating the high child
				OBDD negHC = highChild.negateRec(varOrd);
				// negating the low child
				OBDD negLC = lowChild.negateRec(varOrd);
				// combining the two negated children
				OBDD neg = negHC.cons(var, negLC, varOrd);
				// putting the negated node into the computed table
				negCT.put(id, neg);
				// returning the negated node
				return neg;
			}
		}
	}
	
	
	/**
	 * provides the satisfaction algorithm and 
	 * clears the assignment and the value before
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
			return null;
		}
	}
	
	
	/**
	 * provides a satisfying assignment for this OBDD (if possible)
	 * (3.2.3)
	 */
	private void satisfyRec() {
		// If the node is a terminal, it's value is returned.
		if (terminal) {
			satVal = value;
		} else {
			// First the node's variable is assigned one
			if (!satAO.contains(var)) {
				// adding the variable only if it isn't in there yet
				satAO.add(var);
			}
			// The search continues from the high child on.
			this.highChild.satisfyRec();
			if (!satVal) {
				// If the current assignment isn't a satisfying one,
				// zero is tested for this node's variable.
				satAO.removeFirstOccurrence(var);
				// The search then continues from the low child on.
				lowChild.satisfyRec();
			}
		}
	}
	
	
	/**
	 * provides the toFormula method and clears the computed table before
	 * @return
	 */
	public Formula toFormula() {
		// clearing the computed table
		formulaCT.clear();
		// calling the actual (recursive) toFormula method
		Formula result = toFormulaRec();
		// returning the result
		return result;
	}
	
	
	/**
	 * @return the Formula represented by the OBDD 
	 */
	private Formula toFormulaRec() {
		// In the case of a terminal, a constant Formula is constructed.
		if (terminal) {
			if (value) {
				// returning the tautological Formula if the node is the
				// 1-terminal
				return new Formula(true);
			}
			else {
				// returning the contradictory Formula if the node is the
				// 0-terminal
				return new Formula(false);
			}
		}
		else {
			// Return the Formula stated in the computed table for this node
			// (if there is one).
			if (formulaCT.containsKey(id)) {
				return formulaCT.get(id);
			} else {
				// Formula representing the node's variable
				Formula xn = new Formula(var);
				// Formula represented by the OBDD induced by the node's 
				// high child
				Formula hcFormula = highChild.toFormulaRec();
				// Formula represented by the OBDD induced by the node's 
				// low child
				Formula lcFormula = lowChild.toFormulaRec();
				// the left half of the Shannon expansion
				Formula shannonLeft = xn.and(hcFormula);
				// the right half of the Shannon expansion
				Formula shannonRight = xn.not().and(lcFormula);
				// Shannon expansion
				Formula shannon = shannonLeft.or(shannonRight);
				// putting the Formula into the computed table
				formulaCT.put(id, shannon);
				// returning the Formula
				return shannon;
			}
		}
	}
	
	
	/**
	 * provides the evaluation of the Formula represented by the OBDD relating 
	 * to a given assignment but sorts the assignment list before
	 * @param assignedOne - list of all variables assigned one
	 * @param varOrd - the VariableOrdering (used for sorting the assignment)
	 * @return the value of the Formula
	 */
	public boolean valueByObdd(LinkedList<Integer> assignedOne, 
			VariableOrdering varOrd) {
		// creating a comparator for the complete VariableOrdering
		VarOrdComparator complVarOrdComp = 
				new VarOrdComparator(varOrd);
		// sorting the list of variables assigned one by means of the 
		// VariableOrdering
		Collections.sort(assignedOne, complVarOrdComp);
		// calling the actual (recursive) algorithm
		return valueByObddRec(assignedOne, complVarOrdComp);
	}
	
	
	/**
	 * Evaluates the formula represented by the OBDD relating to a given
	 * assignment.
	 * (3.2.1)
	 * @param assignedOne - list of all variables assigned one
	 * @param complVarOrdComp - the VariableOrderingComparator
	 * @return the value of the formula as a boolean
	 */
	public boolean valueByObddRec(LinkedList<Integer> assignedOne, 
			VarOrdComparator complVarOrdComp) {
		if (terminal) {
			// If the node is a terminal, its value is returned.
			return value;
		}
		// Otherwise the OBDD is run through as directed by the assignment.
		else {
			// As long as the first variable in the list of the variables 
			// assigned one is higher by means of the VariableOrdering than 
			// this node's one, it is removed from the list.
			while (complVarOrdComp.compare(assignedOne.getFirst(), var) > 0) {
				assignedOne.removeFirst();
			}
			if (assignedOne.getFirst() == var) {
				// If the node's variable is assigned one, first it is removed 
				// from the list.
				assignedOne.removeFirst();
				// Then the high child gets to continue the calculation.
				return highChild.valueByObddRec(assignedOne, complVarOrdComp);
			}
			else {
				// Otherwise the node's variable is assigned zero and therefore
				// the low child gets to continue the calculation.
				return lowChild.valueByObddRec(assignedOne, complVarOrdComp);
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
		return this.toFormula().evaluate(assignedOne);
	}

	
	/**
	 * provides the number algorithm and clears the computed table before
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public int number(VariableOrdering varOrd) {
		// clearing the computed table
		numberCT.clear();
		// calling the actual (recursive) number algorithm
		return numberRec(varOrd);
	}
	
	
	/**
	 * provides the number of satisfying assignments for the OBDD
	 * @param varOrd - the VariableOrdering (used for considering the missing 
	 * 					layers on each path)
	 * @return the number of satisfying assignments
	 */
	private int numberRec(VariableOrdering varOrd) {
		// If the node is a terminal, the number of satisfying assignments can
		// easily be returned.
		if (terminal) {
			if (value) {
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
			if (numberCT.containsKey(id)) {
				return numberCT.get(id);
			}
			// Otherwise the number of satisfying assignments is calculated by 
			// summing the numbers of satisfying assignments for both children.
			else {
				// position of this node's variable in the variable ordering
				int varPos = varOrd.indexOf(var);
				// position of the high child's variable in the variable ordering
				// (The variable ordering has to be the same in the entire OBDD.)
				int hcVarPos = varOrd.indexOf(highChild.var);
				// position of the low child's variable in the variable ordering
				int lcVarPos = varOrd.indexOf(lowChild.var);
				// the high child's number of satisfying assignments 
				int hcNumber = highChild.numberRec(varOrd);
				// the low child's number of satisfying assignments
				int lcNumber = lowChild.numberRec(varOrd);
				// Satz 3.2.6
				int number = (int) ((hcNumber * Math.pow(2, hcVarPos - varPos) 
						+ lcNumber * Math.pow(2, lcVarPos - varPos)) / 2);
				// putting the number for this node into the computed table
				numberCT.put(id, number);
				// returning the number
				return number;
			}
		}
	}
	
	
	/**
	 * @param varOrd - the VariableOrdering
	 * @return a list with two equivalent nodes in the OBDD if possible
	 */
	public LinkedList<OBDD> findAnyEquivalent(VariableOrdering varOrd) {
		// updating the layer HashMap
		updateLayers(this);
		// initializing the list for the equivalent nodes
		LinkedList<OBDD> candidates = new LinkedList<OBDD>();
		// initializing the list of the current layer's nodes
		// which are possibly equivalent to another one
		LinkedList<OBDD> residualLayerList;
		// trying to find equivalent nodes in each layer individually
		for (int var : varOrd.getOrdList()) {
			// getting the current layer list
			residualLayerList = layers.get(var);
			// While there are two or more nodes in the layer list,
			// they might be equivalent.
			while (residualLayerList != null && residualLayerList.size()>=2) {
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
				}
				// clearing the candidate list
				candidates.clear();
				// If no equivalent node could be found for this one,
				// check for other ones.
			}
			// otherwise checking other layers
		}
		// At this point there are no equivalent nodes.
		return null;
	}
	
	
	/**
	 * method that provides all nodes in the entire OBDD that are equivalent to
	 * this node (excluding the node itself)
	 * @param root - the OBDD's root
	 * @return a list of all of the OBDD's nodes equivalent to this node; 
	 * null if there aren't any
	 */
	public LinkedList<OBDD> findEquivalent(OBDD root) {
		// updating the layer HashMap
		updateLayers(root);
		// initializing the list of nodes equivalent to this node
		LinkedList<OBDD> equivNodes = new LinkedList<OBDD>();
		// the node's layer list
		LinkedList<OBDD> layerList = layers.get(var);
		// removing the node itself from the list
		layerList.remove(this);
		// iterator over the layer list
		java.util.Iterator<OBDD> iter = layerList.iterator();
		// checking each node in the layer list, whether it's equivalent to 
		// this node
		while (iter.hasNext()) {
			OBDD currentNode = iter.next();
			if (isEquivalent(currentNode)) equivNodes.add(currentNode);
		}
		// returning null if there are no equivalent nodes
		if (equivNodes.isEmpty()) return null; 
		// returning the list of nodes equivalent to this node
		else return equivNodes;
	}
	
	
	/**
	 * provides the equivalence test and clears the computed table before
	 * @param otherNode
	 * @return
	 */
	public boolean isEquivalent(OBDD otherNode) {
		// clearing the computed table
		equivCT.clear();
		// calling the actual (recursive) equivalence test
		return isEquivalentRec(otherNode);
	}
	
	
	/**
	 * @param otherNode
	 * @return whether another node is equivalent to this node
	 */
	private boolean isEquivalentRec(OBDD otherNode) {
		// If at least one of the two nodes is a terminal,
		// they aren't equivalent unless they're the same.
		if (terminal || otherNode.terminal) {
			return (id == otherNode.id);
		}
		else {
			// initializing a pair of the two OBDDs' IDs
			Pair<Integer, Integer> checkPair = 
					new Pair<Integer, Integer>(id,otherNode.id);
			// Return the value stated for the two nodes in the computed table
			// if there is one.
			if (equivCT.containsKey(checkPair)) {
				return equivCT.get(checkPair);
			} else {
				// For equivalence the two high children have to be equivalent.
				boolean equivalentHC =
						highChild.isEquivalentRec(otherNode.highChild);
				// For equivalence the two low children have to be equivalent.
				boolean equivalentLC =
						lowChild.isEquivalentRec(otherNode.lowChild);
				// For equivalence the two variables have to be equivalent.
				boolean equivalentVar = var == otherNode.var;
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
	 * provides a redundant node of this OBDD (if possible)
	 * @return a redundant node (if possible)
	 */
	public OBDD findRedundant() {
		// If the node is a terminal, the search has failed.
		if (terminal) {
			// tentative value: null
			return null;
		}
		// Otherwise the node has children.
		else if (isRedundant()) {
			// If the node is redundant, it is returned.
			return this;
		}
		// If the node itself isn't redundant, the search is continued for its
		// children recursively.
		else {
			// trying to find a redundant node along the high child's paths
			OBDD redundantFind = highChild.findRedundant();
			// If the search along the high child's paths didn't provide a 
			// redundant node, the search is continued along the low child's
			// paths.
			if ((redundantFind == null)) {
				redundantFind = lowChild.findRedundant();
			}
			// returning the "find"
			return redundantFind;
		}
	}
	
	
	/**
	 * states whether the OBDD node is redundant
	 * @return
	 */
	public boolean isRedundant() {
		// A terminal isn't redundant.
		// A decision node is redundant, if it's children are the same 
		// (which can be indicated by their IDs).
		return !terminal && (highChild.id == lowChild.id);
	}
	
	
	/**
	 * states, whether the OBDD is a QOBDD
	 * @param varOrd - the VariableOrdering
	 * @return 
	 */
	public boolean isQobdd(VariableOrdering varOrd) {
		// An OBDD is a QOBBD if there are no equivalent nodes and each path 
		// from the root to a terminal doesn't miss any variables from the 
		// given variable ordering (and is ordered that way).
		return (findAnyEquivalent(varOrd) == null) && 
				noVarMissing(varOrd.getOrdList());
	}
	
	
	/**
	 * states, whether in all paths from this node on there is no variable of a
	 * given list missing
	 * @param varOrdList
	 * @return
	 */
	public boolean noVarMissing(LinkedList<Integer> varOrdList) {
		// creating a copy of the ordering list to be shortened
		LinkedList<Integer> checkList = new LinkedList<Integer>();
		checkList.addAll(varOrdList);
		// If the list is empty, no variable is missing on this path.
		// If the node is a terminal or the first variable in the ordered 
		// variable list isn't the one of this node, it is missing on this 
		// path.
		// Otherwise the node's children are checked recursively.
		return checkList.isEmpty() || (!terminal && (var == checkList.poll()) 
				&& highChild.noVarMissing(checkList) 
				&& lowChild.noVarMissing(checkList));
	}
	
	
	/**
	 * states, whether the entire OBDD is an ROBDD
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public boolean isRobdd(VariableOrdering varOrd) {
		// An OBDD is an ROBBD if there are no equivalent nodes and no 
		// redundant nodes.		
		return (findAnyEquivalent(varOrd) == null) && 
				(findRedundant() == null);
	}
	
	
	/**
	 * provides an OBDD equivalent to this one, reduced by merging all 
	 * equivalent nodes, resulting in a QOBDD if no path from this node to a 
	 * terminal misses a variable
	 * @param varOrd - the VariableOrdering
	 * @return the reduced (Q)OBDD
	 */
	public OBDD reduceQ(VariableOrdering varOrd) {
		// initializing the reduced (Q)OBDD
		OBDD qobdd = this;
		// trying to find a pair of equivalent nodes in the new OBDD
		LinkedList<OBDD> equivalentFind = qobdd.findAnyEquivalent(varOrd);
		// While there are equivalent nodes in the OBDD, they have to be merged
		// to create a QOBDD.
		while(equivalentFind != null) {
			// merging the two found equivalent nodes
			qobdd = qobdd.merge(equivalentFind.poll(), equivalentFind.getFirst(), varOrd);
			// searching for more equivalent nodes
			equivalentFind = qobdd.findAnyEquivalent(varOrd);
		}
		// After adding all missing variables and merging all equivalent nodes,
		// the new OBDD is a QOBDD.
		return qobdd;
	}
	
	
	/**
	 * method that constructs a new OBDD like this one, adding all missing 
	 * variables on each path
	 * @param varOrd - the VariableOrdering
	 * @param varOrdList - list of variables to be used in the new OBDD
	 * @return the new OBDD
	 */
	public OBDD addMissingVars(VariableOrdering varOrd, 
			LinkedList<Integer> varOrdList) {
		// If the ordering list is empty, there are no missing variables on 
		// this path and the node itself is returned.
		if (varOrdList.isEmpty()) return this;
		// initializing a variable for the "fixed" node
		OBDD thisMVA;
		// creating a copy of the ordering list to be shortened
		LinkedList<Integer> addList = new LinkedList<Integer>();
		addList.addAll(varOrdList);
		// retrieving the current variable
		int currentVar = addList.poll();
		// setting the node MVA("missing variables added") to the node itself, 
		// if it's a terminal
		if (terminal) thisMVA = this;
		// If the node is a decision node, its children have to be "fixed" 
		// recursively
		else {
			// constructing the high child with all missing variables added
			OBDD highChildMVA = highChild.addMissingVars(varOrd, addList);
			// constructing the low child with all missing variables added
			OBDD lowChildMVA = lowChild.addMissingVars(varOrd, addList);
			// constructing this node with all missing variables added
			thisMVA = 
					highChildMVA.cons(var, lowChildMVA, varOrd);
		}
		// If this node's variable isn't the current one, it is assumed
		// that there are variables missing at this point.
		if (var != currentVar) {
			// initializing a variable for a list of all variables missing at 
			// this point
			List<Integer> gapVars;
			// If the node is a terminal, all variables from the list are 
			// missing here.
			if (terminal) gapVars = varOrdList;
			// If the node is a decision node, all variables from the current 
			// one to the first one before this node's variable are missing 
			// here.
			else gapVars = varOrdList.subList(
					varOrdList.indexOf(currentVar), varOrdList.indexOf(var));
			// creating a copying linked list for an iterating reason
			LinkedList<Integer> gapVarsLinked = new LinkedList<Integer>();
			gapVarsLinked.addAll(gapVars);
			// a descending iterator over the linked list of gap variables
			java.util.Iterator<Integer> iter = gapVarsLinked
					.descendingIterator();
			// for each gap variable adding a redundant node
			while (iter.hasNext())
				thisMVA = thisMVA.cons(iter.next(), thisMVA, varOrd);
		}
		// returning the resulting node
		return thisMVA;
	}
	
	
	/**
	 * provides the ROBDD equivalent to this OBDD by merging all equivalent 
	 * nodes and removing all redundant ones
	 * @param varOrd - the VariableOrdering
	 * @return the ROBDD
	 */
	public OBDD reduceR(VariableOrdering varOrd) {
		// initializing the ROBDD
		OBDD robdd = this;
		// merging all equivalent nodes by using the QOBDD reduction method
		robdd = robdd.reduceQ(varOrd);
		// trying to find a redundant node
		OBDD redundantFind = robdd.findRedundant();
		// while there are redundant nodes in the OBDD, they have to be removed 
		// to create an ROBBD.
		while (!(redundantFind == null)) {
			// removing the found redundant node
			robdd = robdd.remove(redundantFind, varOrd);
			// searching for another redundant node
			redundantFind = robdd.findRedundant();
		}
		// After merging all equivalent nodes and removing all redundant ones, 
		// the OBDD is an ROBDD.
		return robdd;
	}
	
	
	/**
	 * removes a given node from the OBDD
	 * (only accurate for redundant nodes)
	 * @param node
	 * @return the resulting OBDD
	 */
	public OBDD remove(OBDD node, VariableOrdering varOrd) {
		// clearing the computed table
		removeAndMergeCT.clear();
		// adding the node's high child to the computed table for the node 
		// itself
		removeAndMergeCT.put(node.id, node.highChild);
		// returning the rebuilt OBDD
		return rebuild(varOrd, new VarOrdComparator(varOrd), 
				node.var);
	}
	
	
	/**
	 * merges two given nodes
	 * (only accurate for equivalent nodes)
	 * @param firstNode
	 * @param secondNode
	 * @param varOrd
	 * @return
	 */
	public OBDD merge(OBDD firstNode, OBDD secondNode, 
			VariableOrdering varOrd) {
		// clearing the computed table
		removeAndMergeCT.clear();
		// putting the first node into the computed table for both itself and 
		// the second node
		removeAndMergeCT.put(firstNode.id, firstNode);
		removeAndMergeCT.put(secondNode.id, firstNode);
		// returning the rebuilt OBDD
		return rebuild(varOrd, new VarOrdComparator(varOrd), 
				firstNode.var);
	}
	
	
	/**
	 * auxiliary method that "rebuilds" the OBDD after removing a node or 
	 * merging two nodes
	 * @param varOrd - the variable ordering
	 * @param varOrdComp - a variable ordering comparator
	 * @param mergeVar - the variable of the changed node(s)
	 * @return the rebuilt OBDD
	 */
	private OBDD rebuild(VariableOrdering varOrd, 
			VarOrdComparator varOrdComp, int mergeVar) {
		// If there is already a calculated result for this node, 
		// it gets returned.
		if (removeAndMergeCT.containsKey(id)) return removeAndMergeCT.get(id);
		// If this node's variable is lower or equal to the one of the changed 
		// node(s) relating to the variable ordering, it doesn't change.
		else if (varOrdComp.compare(var, mergeVar) <= 0) {
			removeAndMergeCT.put(id, this);
			return this;
		}
		// Otherwise the resulting node has to be calculated.
		else {
			// recursively calling the node's children
			OBDD rebuiltHighChild = 
					highChild.rebuild(varOrd, varOrdComp, mergeVar);
			OBDD rebuiltLowChild = 
					lowChild.rebuild(varOrd, varOrdComp, mergeVar);
			// combining the two rebuilt children to a new version of this node
			OBDD rebuiltNode = 
					rebuiltHighChild.cons(id, var, rebuiltLowChild, varOrd);
			removeAndMergeCT.put(id, rebuiltNode);
			return rebuiltNode;
		}
	}
	
	
	/**
	 * provides the "allSat" algorithm and clears the computed 
	 * table before
	 * @param varOrd - the VariableOrdering
	 * @return
	 */
	public LinkedList<LinkedList<Integer>> allSat(VariableOrdering varOrd) {
		// clearing the computed table
		allSatCT.clear();
		// calling the actual (recursive) algorithm
		return allSatRec(varOrd.getOrdList());
	}
	
	
	/**
	 * provides all satisfying assignments for an OBDD
	 * @param varOrdList - the list of "all" variables
	 * @return
	 */
	private LinkedList<LinkedList<Integer>> allSatRec(LinkedList<Integer> varOrdList) {
		if (terminal) {
			LinkedList<LinkedList<Integer>> emptyList = new LinkedList<LinkedList<Integer>>();
			if (value) {
				// If the node is the 1-terminal, a list containing an empty 
				// list is returned.
				emptyList.add(new LinkedList<Integer>());
			}
			// If the node is the 0-terminal, only an empty list is returned.
			return emptyList;
		}
		// returning the list stated in the computed table for this node
		// (if there is one)
		if (allSatCT.containsKey(id)) {
			return allSatCT.get(id);
		} else {
			// getting the position of the node's variable in the given variable list
			int varPos = varOrdList.indexOf(var);
			// initializing a list for all variables that were "missing" before the 
			// node's one
			LinkedList<Integer> missingVars = new LinkedList<Integer>();
			// moving all variables in the list before the node's one to the list 
			// of "missing" variables
			for (int i = 0; i < varPos; i++) {
				 missingVars.add(varOrdList.poll());
			}
			// creating the "power list" of the list of "missing" variables
			LinkedList<LinkedList<Integer>> missingVarsPL = powerList(missingVars);
			// removing this node's variable form the variable list
			varOrdList.poll();
			// getting all satisfying assignments for the node's high child
			LinkedList<LinkedList<Integer>> allSatList = 
					highChild.allSatRec(varOrdList);
			// initializing a variable for satisfying assignments for the node's 
			// high child
			LinkedList<Integer> satHC;
			// for each satisfying assignment of the node's high child, adding the 
			// node's variable to the beginning of the list
			for (int i = 0; i < allSatList.size(); i++) {
				satHC = allSatList.get(i);
				satHC.addFirst(var);
				allSatList.set(i, satHC);
			}
			// getting all satisfying assignments for the node's low child
			allSatList.addAll(lowChild.allSatRec(varOrdList));
			// initializing an empty list for all satisfying assignments including 
			// the "missing" variables" 
			LinkedList<LinkedList<Integer>> allSatWithMissing = 
					new LinkedList<LinkedList<Integer>>();
			// initializing a variable for the individual satisfying assignments 
			// with "missing" variables
			LinkedList<Integer> satWithMissing;
			// for each list of "missing" variables and each satisfying assignment 
			// so far, adding each possible combination of elements of the two sets 
			// to the list of satisfying assignments with "missing" variables
			for (LinkedList<Integer> mVList : missingVarsPL) {
				for (LinkedList<Integer> satList : allSatList) {
					// setting the new assignment to the current list of "missing" 
					// variables
					satWithMissing = mVList;
					// adding the current satisfying assignment
					satWithMissing.addAll(satList);
					// adding the "complete" assignment to the list
					allSatWithMissing.add(satWithMissing);
				}
			}
			// putting all satisfying assignments including "missing" variables 
			// for this node into the computed table
			allSatCT.put(id, allSatWithMissing);
			// returning the assignments
			return allSatWithMissing;
		}
	}
	
	
	/**
	 * provides a "power list" for a given list of variables 
	 * (integers)
	 * @param varList - the list of variables (integers)
	 * @return the "power list"
	 */
	private LinkedList<LinkedList<Integer>> powerList(LinkedList<Integer> varList) {
		// initializing a list with an empty list in it
		LinkedList<LinkedList<Integer>> powList = new LinkedList<LinkedList<Integer>>();
		powList.add(new LinkedList<Integer>());
		// iterator for the list of variables
		java.util.Iterator<Integer> iter = varList.iterator();
		// iterating over the variables
		while (iter.hasNext()) {
			// for each list already in the "power list" adding a new one 
			// that's a copy of that list except the next variable is also in
			for (LinkedList<Integer> list : powList) {
				list.add(iter.next());
				powList.add(list);
			}
		}
		// returning the complete "power list"
		return powList;
	}
	
	
	/**
	 * method that provides the (recursive) naming method and updates the layer
	 * HashMap before
	 * @param obddName
	 */
	public void nameNodes(String obddName) {
		updateLayers(this);
		nameNodesRec(obddName, 1);
	}
	
	/**
	 * method that names all nodes of the OBDD by the given name of the entire 
	 * OBDD and the node's position in it
	 * @param obddName
	 * @param layerNumber - the number of the current layer
	 */
	private void nameNodesRec(String obddName, int layerNumber) {
		// Only non-terminal nodes are named.
		if (!terminal) {
			// retrieving the position inside the layer
			int layerPosition = layers.get(var).indexOf(this) + 1;
			// naming the node
			name = obddName + "(" + layerNumber + "," + layerPosition + ")";
			// recursively calling the node's children
			highChild.nameNodesRec(obddName, layerNumber + 1);
			lowChild.nameNodesRec(obddName, layerNumber + 1);
		}
	}
}
