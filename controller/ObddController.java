package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;







import view.MainGui;
import model.AbstractObddLayout;
import model.Formula;
import model.OBDD;
import model.Pair;
import model.VariableOrdering;
import model.VisualObdd;

/**
 * 
 * @author TimNP
 *
 */
public class ObddController {
	/**
	 * the current OBDD
	 */
	VisualObdd currentObdd = null;
	/**
	 * HashMap for storing a stack for each OBDD currently worked with
	 */
	private HashMap<
		// the name of the OBDD
		String,
		// the stack for the abstract OBDD layout representing the OBDD
		Stack<AbstractObddLayout>> obddStacks = 
		new HashMap<String, Stack<AbstractObddLayout>>();
//	/**
//	 * 
//	 */
//	private static final int GENERATE = 1, CLEAR_TT = 2, TT_WINDOW = 3, 
//			SHOW_OBDD = 4, APPLY_OBDDS = 5, UNDO = 6, FIND_EQUIV = 7, 
//			MERGE_EQUIV = 8, FIND_RED = 9, REMOVE_RED = 10, REDUCE_Q = 11, 
//			REDUCE_R = 12, FORMULA = 13;
	
	
	
	/**
	 * constructor for an OBDD controller
	 */
	public ObddController() {
		//TODO?
	}
	
	
	/**
	 * generates an OBDD from a formula given from the user as a string
	 * @param obddName - the OBDD's name
	 * @param formulaFieldText - the string representing the formula
	 * @param varOrdFieldText - the string representing the variable ordering
	 * @param obddTypeNumber - the OBDD's type number
	 * @param panelSize - the OBDD panel's size
	 * @return the visual OBDD
	 */
	public VisualObdd obddFromFormula(String obddName, String formulaFieldText, 
			String varOrdFieldText, int obddTypeNumber, Dimension panelSize) {
		// If the OBDD's name already exists, it gets numbered automatically
		if (obddStacks.containsKey(obddName)) {
			// initializing the number
			int i = 1;
			// creating the first numbered name
			String numberedName = obddName + " (1)";
			// As long as the numbered name also exists, a new one is created.
			while (obddStacks.containsKey(numberedName)) {
				// increasing the number
				i++;
				// creating the new numbered name
				numberedName = obddName + " (" + i + ")";
			}
			// The first numbered name, that doesn't already exist, gets to 
			// replace the given name.
			obddName = numberedName;
		}
		// If no name was given, one is created.
		else if (obddName.equals("")) {
			// creating the base name
			String baseName = "BDD ";
			// initializing the number
			int i = 1;
			// creating the first numbered name
			obddName = "BDD 1";
			// As long as the name also exists, a new one is created.
			while (obddStacks.containsKey(obddName)) {
				// increasing the number
				i++;
				// creating the new numbered name
				obddName = baseName + i;
			}
		}
		// creating the formula
		Formula formula = FormulaController.stringToFormula(formulaFieldText);
		// creating the variable ordering
		VariableOrdering varOrd = VarOrdController.stringToVarOrd(varOrdFieldText);
		// creating the complete OBDD
		OBDD obdd = formula.toObdd(varOrd);
		// reducing to a QOBDD if one should be generated
		if (obddTypeNumber == 1) obdd = obdd.reduceQ(varOrd);
		// reducing to an ROBDD if one should be generated
		else if (obddTypeNumber == 2) obdd = obdd.reduceR(varOrd);
		// creating the abstract OBDD layout
		AbstractObddLayout abstractObdd = new AbstractObddLayout(obdd);
		// initializing a new stack for the OBDD
		Stack<AbstractObddLayout> obddStack = new Stack<AbstractObddLayout>();
		// pushing the OBDD's first abstract version onto the stack
		obddStack.push(abstractObdd);
		// adding the OBDD's stack to the stack HashMap
		obddStacks.put(obddName, obddStack);
		// creating the visual OBDD
		VisualObdd visualObdd = new VisualObdd(abstractObdd, panelSize);
		// setting the current visual OBDD
		currentObdd = visualObdd;
		// returning the visual OBDD
		return visualObdd;
	}
	
	
	/**
	 * undoes the last operation executed on the OBDD given by its name
	 * @param obddName - the OBDD's name
	 * @return the OBDD's previous version, if there is one; 
	 * 		   the current OBDD otherwise
	 */
	public VisualObdd undo(String obddName, Dimension panelSize) {
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(obddName);
		// checking whether there is a previous version of the OBDD
		if (obddStack.size() > 1) {
			// removing the OBDD's current version from the stack
			obddStack.pop();
			// putting the stack back into the stack HashMap
			obddStacks.put(obddName, obddStack);
			// setting the current OBDD
			currentObdd = new VisualObdd(obddStack.peek(), panelSize);
		}
		// returning the OBDD('s previous version)
		return currentObdd;
	}
	
	
	/**
	 * If a node is selected, all nodes equivalent to it are highlighted.
	 * Otherwise any two equivalent nodes are highlighted.
	 * (Highlighting is not stored on the stack.)
	 * @param varOrdFieldText - the variable ordering (in string form)
	 * @return
	 */
	public VisualObdd findEquivalentNodes(String varOrdFieldText) {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// creating the variable ordering
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdFieldText);
		// retrieving the selected node (if there is one)
		OBDD selectedNode = currentObdd.getSelectedNode();
		// variable for the equivalent nodes
		LinkedList<OBDD> equivalentNodes;
		// searching for any two equivalent nodes, if there is no selected node
		if (selectedNode.equals(null)) 
			equivalentNodes = obdd.findAnyEquivalent(varOrd);
		// searching for nodes equivalent to the selected node, if there is one
		else equivalentNodes = selectedNode.findEquivalent(obdd);
		// setting the current OBDD's highlighted nodes
		currentObdd.setHighlightedNodes(equivalentNodes);
		// returning the current OBDD
		return currentObdd;
	}
	
	
	/**
	 * merges selected/highlighted nodes if they are equivalent
	 * @param obddName - the OBDD's name
	 * @param varOrdFieldText - the variable ordering (in string form)
	 * @param panelSize - the OBDD panel's size
	 * @return the visual OBDD after merging; null if merging wasn't possible
	 */
	public VisualObdd mergeEquivalent(String obddName, 
			String varOrdFieldText, Dimension panelSize) {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// retrieving the selected and highlighted nodes
		OBDD firstSelNode = currentObdd.getSelectedNode();
		OBDD secondSelNode = currentObdd.getSecondSelectedNode();
		LinkedList<OBDD> highlightedNodes = currentObdd.getHighlightedNodes();
		// creating the variable ordering
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdFieldText);
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(obddName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = obddStack.peek();
		// checking whether there is a selected node
		if (!firstSelNode.equals(null)) {
			// checking whether there is a second selected node
			if (!secondSelNode.equals(null)) {
				// only merging, if the nodes are equivalent and decision nodes
				if (firstSelNode.isEquivalent(secondSelNode) && 
						!firstSelNode.isTerminal()) {
					// merging the nodes
					obdd = obdd.merge(firstSelNode, secondSelNode, varOrd);
					// updating the abstract OBDD
					abstractObdd.removeNode(secondSelNode.getId(), obdd);
					// pushing the changed abstract OBDD onto the stack
					obddStack.push(abstractObdd);
					// putting the stack back into the stack HashMap
					obddStacks.put(obddName, obddStack);
					// creating the new visual OBDD and returning it
					currentObdd = new VisualObdd(abstractObdd, panelSize);
					return currentObdd;
				}
				// returning null if the nodes weren't be merged
				else return null;
			}
			// adding the first selected node to the front of the list of 
			// highlighted nodes, If there is no second selected node
			highlightedNodes.addFirst(firstSelNode);
		}
		// checking whether there is more than one highlighted node
		if (highlightedNodes.size() > 1) {
			// retrieving the first highlighted node
			firstSelNode = highlightedNodes.poll();
			// merging all remaining highlighted nodes into the first one 
			while (!highlightedNodes.isEmpty()) {
				// retrieving the next node
				OBDD secondMergeNode = highlightedNodes.poll();
				// only merging, if the nodes are equivalent and decision nodes
				if (firstSelNode.isEquivalent(secondMergeNode) && 
						!firstSelNode.isTerminal()) {
					// merging the nodes
					obdd = obdd.merge(firstSelNode, secondMergeNode, varOrd);
					// updating the abstract OBDD
					abstractObdd.removeNode(secondMergeNode.getId(), obdd);
				}
				// returning null if the nodes weren't be merged				
				else return null;
			}
			// pushing the changed abstract OBDD onto the stack
			obddStack.push(abstractObdd);
			// putting the stack back into the stack HashMap
			obddStacks.put(obddName, obddStack);
			// creating the new visual OBDD and returning it
			currentObdd = new VisualObdd(abstractObdd, panelSize);
			return currentObdd;
		}
		// returning null if there weren't enough nodes given to be merged
		else return null;
	}
}
