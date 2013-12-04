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
	private LinkedList<Integer> varOrd;
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
	 * @return whether the node is a terminal
	 */
	private boolean isTerminal() {
		return terminal;
	}
	
	/**
	 * @return
	 */
	private boolean getValue() {
		return value;
	}
	
	/**
	 * 
	 * @param value
	 */
	private void setValue(boolean value) {
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	private int getVar() {
		return var;
	}
	
	/**
	 * 
	 * @param var
	 */
	private void setVar(int var) {
		this.var = var;
	}
	
	/**
	 * 
	 * @return
	 */
	private LinkedList<Integer> getVarOrd() {
		return varOrd;
	}
	
	/**
	 * 
	 * @param varOrd
	 */
	private void setVarOrd(LinkedList<Integer> varOrd) {
		this.varOrd = varOrd;
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap<Integer, LinkedList<OBDD>> getLayers() {
		return layers;
	}
	
	/**
	 * 
	 * @param layers
	 */
	private void setLayers(HashMap<Integer, LinkedList<OBDD>> layers) {
		this.layers = layers;
	}
	
	
	//TODO getters, setters, constructors
	
	
	/**
	 * default constructor for OBDDs
	 */
	public OBDD() {
		
	}
	
	
	/**
	 * TEST constructor for terminals
	 * @param value
	 */
	public OBDD(boolean value, LinkedList<Integer> varOrd) {
		this.value = value;
		this.varOrd = varOrd;
	}
	
	
	/**
	 * TEST constructor for decision nodes
	 * @param var
	 * @param highChild
	 * @param lowChild
	 */
	public OBDD(int var, OBDD highChild, OBDD lowChild) {
		this.varOrd = highChild.varOrd;
		this.var = var;
		this.highChild = highChild;
		this.lowChild = lowChild;
		highChild.parents.add(this);
		lowChild.parents.add(this);
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
		if (!this.isTerminal()) {
			/**
			 * retrieving this node's layer list
			 */
			LinkedList<OBDD> layerList = layers.get(this.getVar());
			/**
			 * adding the node to its layer list if it isn't already in
			 */
			if(!layerList.contains(this)) {
				layerList.add(this);
			}
			/**
			 * putting the updated layer list into the layer HashMap
			 */
			layers.put(this.getVar(), layerList);
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
		for (int var : this.getVarOrd()) {
			/**
			 * getting the current layer list
			 */
			residualLayerList = this.getLayers().get(var);
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
				HashMap<Pair<OBDD>,Boolean> emptyCT =
						new HashMap<Pair<OBDD>,Boolean>();
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
	public boolean isEquivalent(OBDD otherNode, HashMap<Pair<OBDD>, Boolean> cT) {
		/**
		 * If at least one of the two nodes is a terminal,
		 * they aren't equivalent unless they're the same.
		 */
		if (this.isTerminal() || otherNode.isTerminal()) {
			return (this == otherNode);
		}
		else {
			/**
			 * initializing a pair of the two OBDDs
			 */
			Pair<OBDD> checkPair = new Pair<OBDD>(this,otherNode);
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
				boolean equivalentV = this.getVar() == otherNode.getVar();
				/**
				 * combining all three criteria
				 */
				boolean equivalent =
						(equivalentHC && equivalentLC && equivalentV);
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
