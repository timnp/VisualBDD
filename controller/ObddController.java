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
			// having the GUI controller inform the user that the given name 
			// already existed and how the OBDD has been named instead
			GuiController.nameAlreadyTaken(obddName);
		}
		// If no name was given, one is created.
		else if (obddName.equals("")) {
			// initializing the number
			int i = 1;
			// creating the first numbered name
			obddName = "BDD 1";
			// As long as the name also exists, a new one is created.
			while (obddStacks.containsKey(obddName)) {
				// increasing the number
				i++;
				// creating the new numbered name
				obddName = "BDD " + i;
			}
			// having the GUI controller inform the user that no name for the 
			// OBDD has been provided and how it got named
			GuiController.noNameGiven(obddName);
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
	 * @return the OBDD's previous version, if there is one; otherwise null
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
		// otherwise calling the GUI controller to inform the user
		else GuiController.nothingToUndo();
		// returning the OBDD('s previous version)
		return currentObdd;
	}
	
	
	/**
	 * If a node is selected, all nodes equivalent to it are highlighted.
	 * Otherwise any two equivalent nodes are highlighted.
	 * (Highlighting is not stored on the stack.)
	 * @param varOrdFieldText - the variable ordering (in string form)
	 * @return the current visual OBDD with highlighted equivalent nodes if any
	 * 		   have been found; otherwise null
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
		try {
			// trying to search nodes equivalent to the selected node
			// (only works if there is a selected node)
			equivalentNodes = selectedNode.findEquivalent(obdd);
		} catch (NullPointerException e) {
			// searching for any two equivalent nodes, if there is no selected 
			// node
			equivalentNodes = obdd.findAnyEquivalent(varOrd);
		}
		try {
			// trying to check whether the list of equivalent nodes is empty in
			// order to check whether it's null
			equivalentNodes.isEmpty();
			// otherwise setting the current OBDD's highlighted nodes
			currentObdd.setHighlightedNodes(equivalentNodes);
		} catch (NullPointerException e) {
			// calling the GUI controller to inform the user, if there are no 
			// equivalent nodes
			GuiController.noEquivalentNodes();
		}
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
		// initializing a list of remaining highlighted nodes
		LinkedList<OBDD> remainingHighlightedNodes = new LinkedList<OBDD>();
		try {
			// trying to get the first selected node's ID in order to check 
			// whether it's null
			firstSelNode.getId();
			try {
				// trying to get the second selected node's ID in order to 
				// check whether it's null
				secondSelNode.getId();
				// adding all highlighted nodes to the list of remaining 
				// highlighted nodes
				remainingHighlightedNodes = highlightedNodes;
				// clearing the list of highlighted nodes, then adding the 
				// second selected node
				highlightedNodes.clear();
				highlightedNodes.add(secondSelNode);
			} catch (NullPointerException e) {}
			// adding the first selected node, if there is one, to the front of
			// the list of highlighted nodes
			highlightedNodes.addFirst(firstSelNode);
		} catch (NullPointerException e) {}
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
				else {
					// calling the GUI controller to inform the user and 
					// returning the unchanged OBDD, if the nodes weren't 
					// all equivalent
					GuiController.notAllEquivalent();
					return currentObdd;
				}
			}
			// pushing the changed abstract OBDD onto the stack
			obddStack.push(abstractObdd);
			// putting the stack back into the stack HashMap
			obddStacks.put(obddName, obddStack);
			// creating the new visual OBDD
			currentObdd = new VisualObdd(abstractObdd, panelSize);
			// setting the visual OBDD's highlighted nodes to the remaining 
			// highlighted nodes
			currentObdd.setHighlightedNodes(remainingHighlightedNodes);
		}
		// calling the GUI controller to inform the user, if there weren't 
		// enough nodes given to be merged
		else GuiController.notEnoughNodesSelected();
		// returning the current OBDD
		return currentObdd;
	}
	
	
	/**
	 * highlights a redundant node in the current OBDD if there is one
	 * (Highlighting is not stored on the stack.)
	 * @return the current visual OBDD with a highlighted redundant node if 
	 * 		   there is one; otherwise null
	 */
	public VisualObdd findRedundant() {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// searching for a redundant node
		OBDD redundantNode = obdd.findRedundant();
		try {
			// trying to get the redundant node's ID in order to check whether 
			// it's null
			redundantNode.getId();
			// creating a new list of highlighted nodes containing only the 
			// found redundant node
			LinkedList<OBDD> highlightedNodes = new LinkedList<OBDD>();
			highlightedNodes.add(redundantNode);
			// setting the current OBDD's highlighted nodes
			currentObdd.setHighlightedNodes(highlightedNodes);
		} catch (NullPointerException e) {
			// calling the GUI controller to inform the user, if there is no 
			// redundant node
			GuiController.noRedundantNode();
		}
		// returning the current OBDD
		return currentObdd;
	}
	
	
	/**
	 * removes the selected node (or a highlighted one, if none is selected) if
	 * it's redundant
	 * @param obddName - the OBDD's name
	 * @param varOrdFieldText - the variable ordering (in string form)
	 * @param panelSize - the OBDD panel's size
	 * @return the visual OBDD if a node has been removed; otherwise null
	 */
	public VisualObdd removeRedundant(String obddName, String varOrdFieldText, 
			Dimension panelSize) {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// retrieving the selected and highlighted nodes
		OBDD selectedNode = currentObdd.getSelectedNode();
		LinkedList<OBDD> highlightedNodes = currentObdd.getHighlightedNodes();
		// creating the variable ordering
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdFieldText);
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(obddName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = obddStack.peek();
		try {
			// trying to get the selected node's ID in order to check
			// whether it's null
			selectedNode.getId();
			// adding the selected node, if there is one, to the front of the 
			// list of highlighted nodes
			highlightedNodes.addFirst(selectedNode);
		} catch (NullPointerException e) {}
		// checking whether the list of highlighted nodes is empty
		if (!highlightedNodes.isEmpty()) {
			// retrieving the first node from the list
			OBDD highlightedNode = highlightedNodes.peek();
			// checking whether the node is redundant
			if (highlightedNode.isRedundant()) {
				// removing the node
				obdd = obdd.remove(highlightedNode, varOrd);
				// updating the abstract OBDD
				abstractObdd.removeNode(highlightedNode.getId(), obdd);
				// pushing the changed abstract OBDD onto the stack
				obddStack.push(abstractObdd);
				// putting the stack back into the stack HashMap
				obddStacks.put(obddName, obddStack);
				// creating the new visual OBDD
				currentObdd = new VisualObdd(abstractObdd, panelSize);
			}
			// calling the GUI controller to inform the user, if the node isn't
			// redundant
			else GuiController.notRedundant();
		}
		// calling the GUI controller to inform the user, if there aren't any 
		// selected or highlighted nodes
		else GuiController.notEnoughNodesSelected();
		// returning the current OBDD
		return currentObdd;
	}
	
	
	/**
	 * reduces the shown OBDD to a QOBDD or an ROBDD
	 * (May not be accurate for all OBDDs.)
	 * @param obddName - the OBDD's name
	 * @param varOrdFieldText - the variable ordering (in string form)
	 * @param panelSize - the OBDD panel's size
	 * @param removeRedundantNodes - boolean that states, whether redundant 
	 * 		  nodes should be removed (which would result in an ROBDD)
	 * @return the visual OBDD
	 */
	public VisualObdd reduce(String obddName, String varOrdFieldText, 
			Dimension panelSize, boolean removeRedundantNodes) {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// creating the variable ordering
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdFieldText);
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(obddName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = obddStack.peek();
		// reducing the OBDD, if it should be reduced to an ROBDD
		if (removeRedundantNodes) obdd = obdd.reduceR(varOrd);
		else {
			// checking whether any path in the QOBDD is missing any variables,
			// if the OBDD should be reduced to a QOBDD
			if (!currentObdd.getObdd().noVarMissing(varOrd.getOrdList())) {
				// calling the GUI controller's missing variable method to find
				// out whether the missing variables should be added
				if (GuiController.missingVars())
					// adding the missing variables
					obdd = obdd.addMissingVars(varOrd, varOrd.getOrdList());
			}
			// reducing the OBDD to a QOBDD
			obdd = obdd.reduceQ(varOrd);
		}
		// updating the abstract OBDD
		abstractObdd.reduceObdd(obdd);
		// pushing the changed abstract OBDD onto the stack
		obddStack.push(abstractObdd);
		// putting the stack back into the stack HashMap
		obddStacks.put(obddName, obddStack);
		// creating the new visual OBDD and returning it
		currentObdd = new VisualObdd(abstractObdd, panelSize);
		return currentObdd;
	}
	
	
	/**
	 * shows both the initial formula and the currently represented one of the 
	 * current OBDD
	 * @param formulaFieldText
	 */
	public void representedFormula(String formulaFieldText) {
		// retrieving the current OBDD's represented formula
		Formula representedFormula = currentObdd.getObdd().toFormula();
		// calling the GUI controller's reduce formula method to find out 
		// whether the formula should be reduced
		if (GuiController.reduceFormula()) 
			// reducing the formula
			representedFormula = representedFormula.reduce();
		// turning the represented formula into a string
		String representedFormulaString = representedFormula.toString();
		// removing the string's first and last character, if the string is 
		// parenthesized
		if (FormulaController.isParenthesized(representedFormulaString)) 
			representedFormulaString = representedFormulaString.substring(1, 
					representedFormulaString.length() - 1);
		// calling the GUI controller's show formulas method
		GuiController.showFormulas(formulaFieldText, 
				representedFormula.toString());
	}
}
