package controller;

import java.util.HashMap;
import java.util.Stack;

import model.OBDD;
import model.Pair;

public class ObddController {
	/**
	 * HashMap for storing a stack for each OBDD currently worked with
	 */
	private static HashMap<
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
	
	private static void obddToLayout(String obddName, int horizontalPixels, 
			int verticalPixels) {
		 if (obddStacks.containsKey(obddName)) {
			//TODO Is this even needed? 
		 }
		 else {
			 //TODO user message
		 }
	}
}
