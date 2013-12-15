package datatypes;

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
	public static final int XNOR = 9;
	public static final int NOT_B = 10;
	public static final int B_IMPLIES_A = 11;
	public static final int NOT_A = 12;
	public static final int A_IMPLIES_B = 13;
	public static final int NAND = 14;
	public static final int TAUTOLOGY = 15;
	
	
	
	/**
	 * smart "constructor" for the 1-terminal
	 * @return the 1-terminal
	 */
	private static OBDD one() {
		OBDD one = new OBDD();
		one.id = 1;
		one.terminal = true;
		one.value = true;
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
		return zero;
	}
	
	
	/**
	 * updates the node's layer HashMap
	 */
	private void updateLayers() {
		/**
		 * initializing the new layer HashMap
		 */
		HashMap<Integer,LinkedList<OBDD>> layers =
				new HashMap<Integer,LinkedList<OBDD>>();
		/**
		 * retrieving the OBDD's root node to begin with
		 */
		OBDD currentNode = this;
		while (!currentNode.getParents().isEmpty()) {
			currentNode = currentNode.getParents().getFirst();
		}
		/**
		 * adding all of the OBBD's non-terminal nodes to the layer HashMap
		 */
		layers = currentNode.addToLayerHashMap(layers);
		/**
		 * saving the updated layer HashMap for this node
		 */
		this.layers = layers;
	}
	
	
	/**
	 * adds the node to its respective layer in a layer HashMap and
	 * lets it's children do the same if the node isn't a terminal 
	 * @param layers
	 * @return the updated layer HashMap
	 */
	private HashMap<Integer,LinkedList<OBDD>> addToLayerHashMap(HashMap<Integer,LinkedList<OBDD>> layers) {
		/**
		 * if the node is a terminal, nothing is done
		 */
		if (!this.terminal) {
			/**
			 * retrieving this node's layer list
			 */
			LinkedList<OBDD> layerList = layers.get(this.var);
			/**
			 * adding the node to its layer list if it isn't already in
			 */
			if(!layerList.contains(this)) {
				layerList.add(this);
			}
			/**
			 * putting the updated layer list into the layer HashMap
			 */
			layers.put(this.var, layerList);
			/**
			 * recursively adding the high child
			 */
			layers = this.getHighChild().addToLayerHashMap(layers);
			/**
			 * recursively adding the low child
			 */
			layers = this.getLowChild().addToLayerHashMap(layers);
		}
		return layers;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public LinkedList<OBDD> getParents() {
		return parents;
	}
	
	/**
	 * 
	 * @param parents
	 */
	public void setParents(LinkedList<OBDD> parents) {
		this.parents = parents;
	}
	
	/**
	 * 
	 * @return
	 */
	public OBDD getHighChild() {
		return highChild;
	}
	
	/**
	 * 
	 * @param highChild
	 */
	public void setHighChild(OBDD highChild) {
		this.highChild = highChild;
	}
	
	/**
	 * 
	 * @return
	 */
	public OBDD getLowChild() {
		return lowChild;
	}
	
	/**
	 * 
	 * @param lowChild
	 */
	public void setLowChild(OBDD lowChild) {
		this.lowChild = lowChild;
	}
	
	
	/**
	 * creates a new OBDD with this node as high child, a given variable,
	 * and a given node as low child
	 * @param variable
	 * @param lowChild
	 * @return the new node
	 */
	public OBDD cons(int variable, OBDD lowChild) {
		/**
		 * initializing the new node
		 */
		OBDD newNode = new OBDD();
		/**
		 * The new node isn't a terminal since it's created with children.
		 */
		newNode.terminal = false;
		/**
		 * The new node's variable ordering is the same as this node's.
		 */
		newNode.varOrd = this.varOrd;
		/**
		 * The given variable becomes the new node's one.
		 */
		newNode.var = variable;
		/**
		 * This node becomes the new node's high child.
		 */
		newNode.highChild = this;
		/**
		 * The given node becomes the new node's low child.
		 */
		newNode.lowChild = lowChild;
		/**
		 * The new node becomes a parent of this node.
		 */
		this.parents.add(newNode);
		/**
		 * The new node becomes a parent of the given node.
		 */
		lowChild.parents.add(newNode);
		return newNode;
	}
	
	
//	public OBDD apply(OBDD b, int op) {
//		/**
//		 * If both OBDD nodes are terminals, the resulting terminal can be
//		 * calculated by means of the boolean operation.
//		 */
//		if (this.terminal && b.terminal) {
//			/**
//			 * switch for the sixteen possible boolean operations
//			 */
//			switch (op) {
//			/**
//			 * case 0
//			 */
//			case CONTRADICTION:
//				/**
//				 * "applying" the contradiction: returning the 0-terminal
//				 */
//				return ZERO;
//			/**
//			 * case 1
//			 */
//			case AND:
//				/**
//				 * applying "and"
//				 */
//				if (this.value && b.value) {
//					/**
//					 * Return the 1-terminal if both nodes are the 1-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 2
//			 */
//			case A_GREATER_THAN_B:
//				/**
//				 * applying "a greater than b"
//				 */
//				if (this.value && !b.value) {
//					/**
//					 * Return the 1-terminal if this node is the 1-terminal and
//					 * the other one is the 0-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 3
//			 */
//			case IDENTITY_OF_A:
//				/**
//				 * "applying" the "identity of a": returning this node
//				 */
//				return this;
//			/**
//			 * case 4
//			 */
//			case B_GREATER_THAN_A:
//				/**
//				 * applying "b greater than a"
//				 */
//				if (!this.value && b.value) {
//					/**
//					 * Return the 1-terminal if this node is the 0-terminal and
//					 * the other one is the 1-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 5
//			 */
//			case IDENTITY_OF_B:
//				/**
//				 * "applying" the "identity of b": returning the other node
//				 */
//				return b;
//			/**
//			 * case 6
//			 */
//			case XOR:
//				/**
//				 * applying "xor"
//				 */
//				if (this.value ^ b.value) {
//					/**
//					 * Return the 1-terminal if one of the two nodes is the
//					 * 1-terminal and the other one is the 0-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 7
//			 */
//			case OR:
//				/**
//				 * applying "or"
//				 */
//				if (this.value || b.value) {
//					/**
//					 * Return the 1-terminal if at least one of the two nodes
//					 * is the 1-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 8
//			 */
//			case NOR:
//				/**
//				 * applying "nor"
//				 */
//				if (!(this.value || b.value)) {
//					/**
//					 * Return the 1-terminal if none of the two nodes is the
//					 * 1-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 9
//			 */
//			case XNOR:
//				/**
//				 * applying "xnor"
//				 */
//				if (this.value == b.value) {
//					/**
//					 * Return the 1-terminal if the two nodes are the same.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 10
//			 */
//			case NOT_B:
//				/**
//				 * applying "not b"
//				 */
//				if (!b.value) {
//					/**
//					 * Return the 1-terminal if b is the 0-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 11
//			 */
//			case B_IMPLIES_A:
//				/**
//				 * applying "b implies a"
//				 */
//				if (this.value || !b.value) {
//					/**
//					 * Return the 1-terminal if this node is the 1-terminal or
//					 * the other one is the 0-terminal (or both).
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 12
//			 */
//			case NOT_A:
//				/**
//				 * applying "not a"
//				 */
//				if (!this.value) {
//					/**
//					 * Returning the 1-terminal if this node is the 0-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 13
//			 */
//			case A_IMPLIES_B:
//				/**
//				 * applying "a implies b"
//				 */
//				if (!this.value || b.value) {
//					/**
//					 * Returning the 1-terminal if this node is the 0-terminal
//					 * or the other one is the 1-terminal (or both).
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 14
//			 */
//			case NAND:
//				/**
//				 * applying "nand"
//				 */
//				if (!(this.value && b.value)) {
//					/**
//					 * Returning the 1-terminal if at least one of the two
//					 * nodes is the 0-terminal.
//					 */
//					return ONE;
//				}
//				/**
//				 * Otherwise return the 0-terminal.
//				 */
//				else return ZERO;
//			/**
//			 * case 15
//			 */
//			case TAUTOLOGY:
//				/**
//				 * "applying" the tautology: returning the 1-terminal
//				 */
//				return ONE;
//			}
//		}
//	}
	
	
	/**
	 * 
	 * @return a list with two equivalent nodes in the OBDD if possible
	 */
	public LinkedList<OBDD> findEquivalent() {
		/**
		 * updating the layer HashMap
		 */
		this.updateLayers();
		/**
		 * initializing the list for the equivalent nodes
		 */
		LinkedList<OBDD> candidates = new LinkedList<OBDD>();
		/**
		 * initializing the list of the current layer's nodes
		 * which are possibly equivalent to another one
		 */
		LinkedList<OBDD> residualLayerList;
		/**
		 * trying to find equivalent nodes in each layer individually
		 */
		for (int var : this.varOrd.getOrdList()) {
			/**
			 * getting the current layer list
			 */
			residualLayerList = this.layers.get(var);
			/**
			 * While there are two or more nodes in the layer list,
			 * they might be equivalent.
			 */
			while (residualLayerList.size()>=2) {
				/**
				 * moving the first node from the residual layer list
				 * to the candidate list
				 */
				candidates.add(residualLayerList.removeFirst());
				/**
				 * initializing an empty computed table
				 */
				HashMap<Pair<Integer>,Boolean> emptyCT =
						new HashMap<Pair<Integer>,Boolean>();
				/**
				 * checking for each node of the residual layer list
				 * whether it's equivalent to the first candidate node
				 */
				for (OBDD secondCandidate : residualLayerList) {
					/**
					 * If the two nodes are equivalent,
					 * return the list with the two of them.
					 */
					if (candidates.getFirst().isEquivalent(secondCandidate, emptyCT)) {
						candidates.add(secondCandidate);
						return candidates;
					}
					/**
					 * otherwise checking other nodes from the list
					 */
				}
				/**
				 * If no equivalent node could be found for this one,
				 * check for other ones.
				 */
			}
			/**
			 * otherwise checking other layers
			 */
		}
		/**
		 * At this point there are no equivalent nodes.
		 */
		//TODO user message
		return null;
	}
	
	
	/**
	 * @param otherNode
	 * @param cT
	 * @return whether another node is equivalent to this node
	 */
	public boolean isEquivalent(OBDD otherNode, HashMap<Pair<Integer>, Boolean> cT) {
		/**
		 * If at least one of the two nodes is a terminal,
		 * they aren't equivalent unless they're the same.
		 */
		if (this.terminal || otherNode.terminal) {
			return (this == otherNode);
		}
		else {
			/**
			 * initializing a pair of the two OBDDs
			 */
			Pair<Integer> checkPair = new Pair<Integer>(this.id,otherNode.id);
			/**
			 * Return the value stated for the two nodes in the computed table
			 * if there is one.
			 */
			if (cT.containsValue(checkPair)) {
				return cT.get(checkPair);
			}
			else {
				/**
				 * For equivalence the two high children have to be equivalent.
				 */
				boolean equivalentHC =
						this.getHighChild().isEquivalent(otherNode.getHighChild(), cT);
				/**
				 * For equivalence the two low children have to be equivalent.
				 */
				boolean equivalentLC =
						this.getLowChild().isEquivalent(otherNode.getLowChild(), cT);
				/**
				 * For equivalence the two variables have to be equivalent.
				 */
				boolean equivalentVar = this.var == otherNode.var;
				/**
				 * combining all three criteria
				 */
				boolean equivalent =
						(equivalentHC && equivalentLC && equivalentVar);
				/**
				 * putting the value for the two nodes into the computed table
				 */
				cT.put(checkPair, equivalent);
				/**
				 * finally returning the value
				 */
				return equivalent;
			}
		}
	}
	
}
