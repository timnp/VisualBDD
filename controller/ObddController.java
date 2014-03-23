package controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	 * the connected main GUI
	 */
	private MainGui mainGui;
	/**
	 * the connected GUI controller
	 */
	private GuiController guiController;
	/**
	 * the current OBDD
	 */
	private VisualObdd currentObdd = null;
	/**
	 * HashMap for storing a stack for each OBDD currently worked with
	 */
	private HashMap<
		// the name of the OBDD
		String,
		// the stack for the abstract OBDD layout representing the OBDD
		Stack<AbstractObddLayout>> obddStacks = 
		new HashMap<String, Stack<AbstractObddLayout>>();
	/**
	 * the current OBDD's name
	 */
	private String currentName = null;
	/**
	 * HashMap for storing the text field strings representing 
	 * variable ordering and formula for each OBDD currently worked with
	 */
	private HashMap<String, Pair<String,String>> stringMap = 
			new HashMap<String, Pair<String,String>>();
	/**
	 * boolean that states whether the displayed OBDD's nodes are currently 
	 * dragable
	 */
	private boolean dragable = false;
	
	
	
	/**
	 * constructor for an OBDD controller
	 */
	public ObddController(MainGui mainGui, GuiController guiController) {
		this.mainGui = mainGui;
		this.guiController = guiController;
	}
	
	
//	/**
//	 * general shortcuts for the main GUI
//	 * @param e - the triggering key event
//	 */
//	public void generalShortcuts(KeyEvent e) {
//		if (e.isControlDown()) {
//			if ((e.getKeyCode() == KeyEvent.VK_Z)) undo();
//		}
//	}
	
	
	/**
	 * a shortcut method for generating the BDD by pressing enter
	 * @param e - the triggering key event
	 */
	public void generateShortcut(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) obddFromFormula();
	}
	
	
	/**
	 * generates an OBDD from a formula given from the user as a string
	 * @return a boolean that states whether the OBDD was created successfully
	 */
	public boolean obddFromFormula() {
		// retrieving the main GUI's input data
		String obddName = mainGui.getObddNameField().getText();
		String varOrdFieldText = mainGui.getVarOrdField().getText();
		String formulaFieldText = mainGui.getFormulaField().getText();
		int obddTypeNumber = mainGui.getSelectedObddType();
		// As long as the given name is either empty or already taken, the user
		// is asked to provide another one.
		while (improperName(obddName)) {
			// retrieving the new name for the OBDD
			obddName = guiController.improperName(obddName);
			// If the name is null, the dialog was cancelled.
			if (obddName == null) {
				// updating the possibly changed field texts
				guiController.updateTextFields(obddName, varOrdFieldText, 
						formulaFieldText);
				return false;
			}
		}
		// As long as the given variable ordering string doesn't fulfill the 
		// requirements and therefore the variable ordering couldn't be created
		// properly, the user is asked to provide another one.
		while (improperVarOrdString(varOrdFieldText)) {
			// having the GUI controller inform the user that the 
			// variable ordering string doesn't fulfill the requirements and 
			// ask for another one
			varOrdFieldText = guiController.
					improperVarOrdString(varOrdFieldText);
			// If the variable ordering string is null, the dialog was 
			// cancelled.
			if (varOrdFieldText == null) {
				// updating the possibly changed field texts
				guiController.updateTextFields(obddName, varOrdFieldText, 
						formulaFieldText);
				return false;
			}
		}
		// creating the variable ordering
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdFieldText);
		// As long as the input string doesn't fulfill the requirements and 
		// therefore the formula couldn't be created properly, the user is 
		// asked to provide another one.		
		while (improperFormulaInputString(formulaFieldText)) {
			// having the GUI controller inform the user that the formula input
			// string doesn't fulfill the requirements and ask for another one
			formulaFieldText = guiController.
					improperFormulaInputString(formulaFieldText);
			// If the formula input string is null, the dialog was cancelled. 
			if (formulaFieldText == null) {
				// updating the possibly changed field texts
				guiController.updateTextFields(obddName, varOrdFieldText, 
						formulaFieldText);
				return false;
			}
		}
		// creating the formula
		Formula formula = FormulaController.stringToFormula(formulaFieldText);
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
		// storing the variable ordering and formula text field strings
		stringMap.put(obddName, 
				new Pair<String,String>(varOrdFieldText, formulaFieldText));
		// creating the visual OBDD
		VisualObdd visualObdd = new VisualObdd(abstractObdd, 
				mainGui.getObddPane().getSize(), !obdd.isRobdd(varOrd));
		// setting the current visual OBDD
		currentObdd = visualObdd;
		// setting the current OBDD name
		currentName = obddName;
		// showing the visual OBDD
		guiController.showObdd(currentObdd);
		// updating the possibly changed OBDD name, variable ordering string 
		// and formula (input) string
		guiController.updateTextFields(obddName, varOrdFieldText, formulaFieldText);
		// adding the OBDD's name to the OBDD list
		guiController.addToObddList(obddName);
		return true;
	}
	
	
	/**
	 * loads the newest version of an OBDD given by its name from its stack
	 * @return a boolean that states whether the OBDD has been loaded 
	 * 		   successfully
	 */
	public boolean loadObdd() {
		// retrieving the selected name
		String selectedName = mainGui.getSelectedObdd();
		// checking whether the selected OBDD's name is null 
		// (e.g. because no OBDD was selected)
		if (selectedName == null) {
			// having the GUI controller inform the user that no OBDD was 
			// selected
			guiController.noObddSelected();
			return false;
		}
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(selectedName);
		// checking whether the stack is null
		if (obddStack == null) {
			// having the GUI controller inform the user that the selected OBDD
			// couldn't be found
			guiController.noSuchObdd();
			return false;
		}
		// retrieving the OBDD's variable ordering and formula 
		// text field strings
		Pair<String,String> textFieldStrings = stringMap.get(selectedName);
		// retrieving the variable ordering string
		String varOrdString = textFieldStrings.getFirst();
		// retrieving the OBDD's newest (abstract) version
		AbstractObddLayout abstractObdd = obddStack.peek();
		// creating the visual OBDD
		currentObdd = new VisualObdd(abstractObdd, 
				mainGui.getObddPane().getSize(), 
				!abstractObdd.getObdd().isRobdd(VarOrdController.
						stringToVarOrd(varOrdString)));
		// setting the current OBDD name
		currentName = selectedName;
		// showing the visual OBDD and updating the field texts
		guiController.showObdd(currentObdd);
		guiController.updateTextFields(selectedName, varOrdString, 
				textFieldStrings.getSecond());
		return true;
	}
	
	
	/**
	 * asks the user for a binary operation to apply on the current OBDD and 
	 * the chosen one and applies it on them, creating a new OBDD
	 * @return a boolean that states whether the two OBDDs have been applied 
	 * 		   successfully
	 */
	public boolean simpleApply() {
		// retrieving the second OBDD's name
		String secondName = mainGui.getSelectedObdd();
		// checking whether the second OBDD's name is null
		if (secondName == null) {
			// having the GUI controller inform the user that no OBDD was 
			// selected
			guiController.noObddSelected();
			return false;
		}
		// retrieving the second OBDD's stack
		Stack<AbstractObddLayout> secondStack = obddStacks.get(secondName);
		// checking whether the stack is null
		if (secondStack == null) {
			// having the GUI controller inform the user that the second OBDD 
			// couldn't be found
			guiController.noSuchObdd();
			return false;
		}
		// retrieving the current OBDD's variable ordering string
		String varOrdString = stringMap.get(currentName).getFirst();
		// creating the two OBDDs' variable orderings
		VariableOrdering firstVarOrd = 
				VarOrdController.stringToVarOrd(varOrdString);
		VariableOrdering secondVarOrd = VarOrdController.stringToVarOrd
				(stringMap.get(secondName).getFirst());
		// checking whether the two variable orderings are equal
		if (firstVarOrd.getOrdList().equals(secondVarOrd.getOrdList())) {
			// having the GUI controller ask the user for the operation to be 
			// applied on the two OBDDs
			int applyOp = 
					guiController.applyOperation(currentName, secondName);
			// checking whether the dialog was cancelled
			if (applyOp >=0 && applyOp <= 15) {
				// having the GUI controller ask the user for a name for the 
				// new OBDD
				String applyName = guiController.newObddName();
				// as long as the given name is either empty or already taken, 
				// the user is asked to provide another one
				while (improperName(applyName)) {
					// retrieving the new name for the OBDD
					applyName = guiController.improperName(applyName);
					// If the name is null, the dialog was cancelled.
					if (applyName == null) return false;
				}
				// retrieving the two actual OBDDs
				OBDD firstObdd = currentObdd.getObdd();
				OBDD secondObdd = secondStack.peek().getObdd();
				// creating the resulting OBDD
				OBDD applyObdd = 
						firstObdd.apply(secondObdd, applyOp, firstVarOrd);
				// creating the abstract OBDD layout
				AbstractObddLayout abstractObdd = 
						new AbstractObddLayout(applyObdd);
				// initializing a new stack for the OBDD
				Stack<AbstractObddLayout> obddStack = 
						new Stack<AbstractObddLayout>();
				// pushing the OBDD's first abstract version onto the stack
				obddStack.push(abstractObdd);
				// adding the OBDD's stack to the stack HashMap
				obddStacks.put(applyName, obddStack);
				// retrieving the formula represented by the OBDD and 
				// reducing it
				Formula formula = 
						FormulaController.reduce(applyObdd.toFormula());
				// retrieving the string representing the formula
				String applyFormulaString = formula.toString();
				// storing the variable ordering and formula text field strings
				stringMap.put(applyName, 
						new Pair<String,String>(varOrdString, 
								applyFormulaString));
				// creating the visual OBDD
				VisualObdd visualObdd = new VisualObdd(abstractObdd, 
						mainGui.getObddPane().getSize(),
						!applyObdd.isRobdd(firstVarOrd));
				// setting the current visual OBDD
				currentObdd = visualObdd;
				// setting the current OBDD name
				currentName = applyName;
				// showing the visual OBDD
				guiController.showObdd(currentObdd);
				// updating the field texts
				guiController.updateTextFields(applyName, varOrdString, 
						applyFormulaString);
				// adding the OBDD's name to the OBDD list
				guiController.addToObddList(applyName); 
				return true;
			}
		}
		else {
			// having the GUI controller inform the user that the two
			// variable orderings aren't equal
			guiController.unequalVarOrds();
		}
		return false;
	}
	
	
	/**
	 * forwards the point clicked at to the current visual OBDD
	 * @param p - the point
	 */
	public void clickOnObddPanel(Point p) {
		currentObdd.clickAtPoint(p);
		// showing the current OBDD
		guiController.showObdd(currentObdd);
	}
	
	
	public void pressOnObddPanel(Point p) {
		currentObdd.pressAtPoint(p);
	}
	
	
	public void releaseOnObddPanel(Point p) {
		// checking whether drag&drop is enabled
		if (dragable) {
			// checking whether a node was moved
			if(currentObdd.releaseAtPoint(p, 
					mainGui.getObddPane().getSize())) {
				// retrieving the current OBDD's stack
				Stack<AbstractObddLayout> obddStack = 
						obddStacks.get(currentName);
				// pushing the changed abstract OBDD onto the stack
				obddStack.push(currentObdd.getAbstractObdd());
				// putting the stack back into the stack HashMap
				obddStacks.put(currentName, obddStack);
			}
			// showing the current OBDD
			guiController.showObdd(currentObdd);
		}
//		else guiController.dragDropDisabled();
	}
	
	
	/**
	 * undoes the last change to the current OBDD
	 */
	public void undo() {
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(currentName);
		// checking whether there is a previous version of the OBDD
		if (obddStack.size() > 1) {
			// removing the OBDD's current version from the stack
			obddStack.pop();
			// retrieving the OBDD's previous version
			AbstractObddLayout abstractObdd = obddStack.peek();
			// creating the OBDD's variable ordering
			VariableOrdering varOrd = VarOrdController.stringToVarOrd
					(stringMap.get(currentName).getFirst());
			// setting the current OBDD
			currentObdd = new VisualObdd(abstractObdd, 
					mainGui.getObddPane().getSize(), 
					!abstractObdd.getObdd().isRobdd(varOrd));
			// putting the stack back into the stack HashMap
			obddStacks.put(currentName, obddStack);
			// showing the visual OBDD
			guiController.showObdd(currentObdd);
		}
		// otherwise calling the GUI controller to inform the user
		else guiController.nothingToUndo();
	}
	
	
	/**
	 * If a node is selected, all nodes equivalent to it are highlighted.
	 * Otherwise any two equivalent nodes are highlighted.
	 * (Highlighting is not stored on the stack.)
	 */
	public void findEquivalentNodes() {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// creating the variable ordering
		VariableOrdering varOrd = VarOrdController.stringToVarOrd
				(stringMap.get(currentName).getFirst());
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
		// calling the GUI controller to inform the user, if there are no 
		// equivalent nodes
		if (equivalentNodes == null) guiController.noEquivalentNodes();
		// otherwise setting the current OBDD's highlighted nodes and showing 
		// it
		else {
			currentObdd.setHighlightedNodes(equivalentNodes);
			guiController.showObdd(currentObdd);
		}
	}
	
	
	/**
	 * merges selected/highlighted nodes if they are equivalent
	 * @return a boolean that states whether the nodes have been merged 
	 * 		   successfully
	 */
	public boolean mergeEquivalent() {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// retrieving the selected and highlighted nodes
		OBDD firstSelNode = currentObdd.getSelectedNode();
		OBDD secondSelNode = currentObdd.getSecondSelectedNode();
		LinkedList<OBDD> highlightedNodes = currentObdd.getHighlightedNodes();
		// creating the variable ordering from the stored string
		VariableOrdering varOrd = VarOrdController.stringToVarOrd
				(stringMap.get(currentName).getFirst());
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(currentName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = 
				new AbstractObddLayout(obddStack.peek());
//		// initializing a list of remaining highlighted nodes with the 
//		// currently highlighted nodes
//		LinkedList<OBDD> remainingHighlightedNodes = highlightedNodes;
		// checking whether there is a first selected node
		if (firstSelNode != null) {
			// checking whether there is a second selected node
			if (secondSelNode != null) {
				// clearing the list of highlighted nodes, then adding the 
				// second selected node
				highlightedNodes.clear();
				highlightedNodes.add(secondSelNode);
			}
			// adding the first selected node, if there is one, to the front of
			// the list of highlighted nodes
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
				else {
					// calling the GUI controller to inform the user and 
					// returning the unchanged OBDD, if the nodes weren't 
					// all equivalent
					guiController.notAllEquivalent();
					return false;
				}
			}
			// pushing the changed abstract OBDD onto the stack
			obddStack.push(abstractObdd);
			// putting the stack back into the stack HashMap
			obddStacks.put(currentName, obddStack);
			// creating the new visual OBDD
			currentObdd = new VisualObdd(abstractObdd, 
					mainGui.getObddPane().getSize(), !obdd.isRobdd(varOrd));
			// showing the current OBDD
			guiController.showObdd(currentObdd);
			return true;
		}
		else {
			// calling the GUI controller to inform the user, if there weren't
			// enough nodes given to be merged
			guiController.notEnoughNodesSelected();
			return false;
		}
	}
	
	
	/**
	 * highlights a redundant node in the current OBDD if there is one
	 * (Highlighting is not stored on the stack.)
	 */
	public void findRedundant() {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// searching for a redundant node
		OBDD redundantNode = obdd.findRedundant();
		// 
		if (redundantNode == null) guiController.noRedundantNode();
		// calling the GUI controller to inform the user, if there is no 
		// redundant node
		else {
			// creating a new list of highlighted nodes containing only the 
			// found redundant node
			LinkedList<OBDD> highlightedNodes = new LinkedList<OBDD>();
			highlightedNodes.add(redundantNode);
			// setting the current OBDD's highlighted nodes
			currentObdd.setHighlightedNodes(highlightedNodes);
			// showing the visual OBDD
			guiController.showObdd(currentObdd);
		}
	}
	
	
	/**
	 * removes the selected node (or a highlighted one, if none is selected) if
	 * it's redundant
	 */
	public void removeRedundant() {
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// retrieving the selected and highlighted nodes
		OBDD selectedNode = currentObdd.getSelectedNode();
		LinkedList<OBDD> highlightedNodes = currentObdd.getHighlightedNodes();
		// initializing a variable for the node to be removed
		OBDD nodeToRemove = null;
		// creating the variable ordering from the stored string
		VariableOrdering varOrd = VarOrdController.stringToVarOrd
				(stringMap.get(currentName).getFirst());
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(currentName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = 
				new AbstractObddLayout(obddStack.peek());
		// setting the node to be removed to the selected node, if there is one
		if (selectedNode != null) nodeToRemove = selectedNode;
		// Otherwise, if there are any highlighted nodes, the node to be 
		// removed is set to the first one of them.
		else if (!highlightedNodes.isEmpty()) 
			nodeToRemove = highlightedNodes.peek();
		// checking whether there is a node to be removed and it's redundant
		if (nodeToRemove != null) {
			if (nodeToRemove.isRedundant()) {
				// removing the node
				obdd = obdd.remove(nodeToRemove, varOrd);
				// updating the abstract OBDD
				abstractObdd.removeNode(nodeToRemove.getId(), obdd);
				// pushing the changed abstract OBDD onto the stack
				obddStack.push(abstractObdd);
				// putting the stack back into the stack HashMap
				obddStacks.put(currentName, obddStack);
				// creating the new visual OBDD and showing it
				currentObdd = new VisualObdd(abstractObdd, 
						mainGui.getObddPane().getSize(), 
						!obdd.isRobdd(varOrd));
				guiController.showObdd(currentObdd);
			}
			// calling the GUI controller to inform the user, if the node isn't
			// redundant
			else guiController.notRedundant();
		}
		// calling the GUI controller to inform the user, if there aren't any 
		// selected or highlighted nodes
		else guiController.notEnoughNodesSelected();
	}
	
	
	/**
	 * reduces the shown OBDD to a QOBDD or an ROBDD
	 * (May not be accurate for all OBDDs.)
	 * @param removeRedundantNodes - boolean that states, whether redundant 
	 * 		  nodes should be removed (which would result in an ROBDD)
	 */
	public void reduce(boolean removeRedundantNodes) {
		// initializing a variable that states whether nodes have been added
		boolean nodesAdded = false;
		// retrieving the current (actual) OBDD
		OBDD obdd = currentObdd.getObdd();
		// creating the variable ordering from the stored string
		VariableOrdering varOrd = VarOrdController.stringToVarOrd
				(stringMap.get(currentName).getFirst());
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(currentName);
		// the current abstract OBDD
		AbstractObddLayout abstractObdd = new AbstractObddLayout(obddStack.peek());
		// reducing the OBDD, if it should be reduced to an ROBDD
		if (removeRedundantNodes) obdd = obdd.reduceR(varOrd);
		else {
			// the variable ordering's ordering list
			LinkedList<Integer> varOrdList = varOrd.getOrdList();
			// checking whether any path in the QOBDD is missing any variables,
			// if the OBDD should be reduced to a QOBDD
			if (!currentObdd.getObdd().noVarMissing(varOrdList)) {
				// calling the GUI controller's missing variable method to find
				// out whether the missing variables should be added
				if (guiController.missingVars()) {
					// adding the missing variables
					obdd = obdd.addMissingVars(varOrd, varOrdList);
					// stating that nodes have been added
					nodesAdded = true;
				}

			}
			// reducing the OBDD to a QOBDD
			obdd = obdd.reduceQ(varOrd);
		}
		// If nodes have been added, a new abstract OBDD layout has to be 
		// created.
		if (nodesAdded) abstractObdd = new AbstractObddLayout(obdd);
		// Otherwise the current abstract OBDD can simply be updated.
		else abstractObdd.reduceObdd(obdd);
		// pushing the changed abstract OBDD onto the stack
		obddStack.push(abstractObdd);
		// putting the stack back into the stack HashMap
		obddStacks.put(currentName, obddStack);
		// creating the new visual OBDD and show it
		currentObdd = new VisualObdd(abstractObdd, 
				mainGui.getObddPane().getSize(), !removeRedundantNodes);
		guiController.showObdd(currentObdd);
	}
	
	
	/**
	 * shows the formula represented by the current OBDD
	 */
	public void representedFormula() {
		// retrieving the current OBDD's represented formula
		Formula representedFormula = currentObdd.getObdd().toFormula();
		// calling the GUI controller's reduce formula method to find out 
		// whether the formula should be reduced
		if (guiController.reduceFormula()) 
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
		guiController.showFormula(representedFormulaString);
	}
	
	
	public void showTruthTable() {
		Pair<String,String> textFieldStrings = stringMap.get(currentName);
		Formula formula = FormulaController.
				stringToFormula(textFieldStrings.getSecond());
		LinkedList<Integer> vars = 
				VarOrdController.stringToOrdList(textFieldStrings.getFirst());
		guiController.showTruthTable(TruthTableController.
				truthTabletoJTable(formula, vars));
	}
	
	
	/**
	 * enables/disables the drag&drop for nodes and changes the belonging 
	 * button's tool tip
	 */
	public void enableDisableDragDrop() {
		dragable = !dragable;
		guiController.changeDragDrop(dragable);
		guiController.showObdd(currentObdd);
	}
	
	
	/**
	 * aligns the current OBDD's nodes' positions to the used standard
	 */
	public void alignNodes() {
		// retrieving the OBDD's stack
		Stack<AbstractObddLayout> obddStack = obddStacks.get(currentName);
		// creating a copy of the OBDD's newest version
		AbstractObddLayout abstractObdd = 
				new AbstractObddLayout(obddStack.peek());
		// aligning the nodes
		abstractObdd.alignNodes();
		// pushing the aligned version to the stack
		obddStack.push(abstractObdd);
		// putting the stack back into the stack HashMap
		obddStacks.put(currentName, obddStack);
		// creating the new visual OBDD and showing it
		currentObdd.updatePositions(abstractObdd, !abstractObdd.getObdd().
				isRobdd(VarOrdController.stringToVarOrd
						(stringMap.get(currentName).getFirst())));
		guiController.showObdd(currentObdd);
	}
	
	
	/**
	 * auxiliary method that states whether an OBDD name is improper
	 * @param obddName
	 * @return
	 */
	private boolean improperName(String obddName) {
		// If the name is empty or already exists, it's improper.
		// Otherwise it isn't.
		return obddName.isEmpty() || obddStacks.containsKey(obddName);
	}
	
	
	/**
	 * auxiliary method that states whether a variable ordering string is 
	 * improper
	 * @param varOrdString
	 * @return
	 */
	private boolean improperVarOrdString(String varOrdString) {
		// creating the variable ordering from the string
		VariableOrdering varOrd = 
				VarOrdController.stringToVarOrd(varOrdString);
		// If the variable ordering is null or empty, the string is improper. 
		// Otherwise it isn't.
		return (varOrd == null) || varOrd.isEmpty();
	}
	
	
	/**
	 * auxiliary method that states whether a formula input string is improper
	 * @param formulaInputString
	 * @return
	 */
	private boolean improperFormulaInputString(String formulaInputString) {
		// creating the formula string from the input string
		String formulaString = 
				FormulaController.toFormulaString(formulaInputString);
		// If the formula string is null or empty, 
		// the input string is improper. Otherwise it isn't.
		return (formulaString == null) || formulaString.isEmpty();
	}
}
