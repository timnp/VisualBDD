package controller;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JPanel;

import view.MainGui;
import model.AbstractObddLayout;
import model.OBDD;
import model.Pair;
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
		// the stack for the OBDD
		Stack<
			// a pair of the OBDD and its nodes' positions
			Pair<
				// the OBDD itself (at that point)
				OBDD,
				// a HashMap for the position of each of the OBDD's nodes
				HashMap<
					// the node's ID
					Integer,
					// the node's (relative) position
					Pair<Double, Double>>>>> obddStacks;
	
	
	
	/**
	 * constructor for an OBDD controller
	 * @param mainGui
	 */
	public ObddController(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	
//	private static void obddToLayout(String obddName, int horizontalPixels, 
//			int verticalPixels) {
//		 if (obddStacks.containsKey(obddName)) {
//			//TODO Is this even needed? 
//		 }
//		 else {
//			 //TODO user message
//		 }
//	}
}
