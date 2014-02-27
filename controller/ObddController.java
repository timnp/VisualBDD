package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Stack;





import view.MainGui;
import model.AbstractObddLayout;
import model.Formula;
import model.OBDD;
import model.VariableOrdering;
import model.VisualObdd;

/**
 * 
 * @author TimNP
 *
 */
public class ObddController {
	/**
	 * the MainGui the OBDDs are shown in
	 */
	private MainGui mainGui;
	/**
	 * HashMap for storing a stack for each OBDD currently worked with
	 */
	private HashMap<
		// the name of the OBDD
		String,
		// the stack for the VisualObdd representing the OBDD
		Stack<VisualObdd>> obddStacks;
//	/**
//	 * 
//	 */
//	private static final int GENERATE = 1, CLEAR_TT = 2, TT_WINDOW = 3, 
//			SHOW_OBDD = 4, APPLY_OBDDS = 5, UNDO = 6, FIND_EQUIV = 7, 
//			MERGE_EQUIV = 8, FIND_RED = 9, REMOVE_RED = 10, REDUCE_Q = 11, 
//			REDUCE_R = 12, FORMULA = 13;
	
	
	
	/**
	 * constructor for an OBDD controller
	 * @param mainGui
	 */
	public ObddController(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	
	/**
	 * generates an OBDD from a formula given from the user as a string
	 * @param obddName - the OBDD's name
	 * @param formulaFieldText - the string representing the formula
	 * @param varOrdFieldText - the string representing the variable ordering
	 * @param obddTypeNumber - the OBDD's type number
	 * @param panelSize - the OBDD panel's size
	 */
	public void obddFromFormula(String obddName, String formulaFieldText, 
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
		// creating the visual OBDD
		VisualObdd visualObdd = 
				new VisualObdd(new AbstractObddLayout(obdd), panelSize);
		// initializing a new stack for the OBDD
		Stack<VisualObdd> obddStack = new Stack<VisualObdd>();
		// pushing the OBDD's first visualization onto the stack
		obddStack.push(visualObdd);
		// adding the OBDD's stack to the stack HashMap
		obddStacks.put(obddName, obddStack);
		// showing the OBDD in the MainGui
		mainGui.showObdd(visualObdd);
	}
}
