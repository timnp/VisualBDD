package model;

import java.util.LinkedList;

/**
 * 
 * @author TimNP
 *
 */
public class TruthTable {
	/**
	 * list of the variables used in the TruthTable
	 */
	private LinkedList<Integer> vars;
	/**
	 * the TruthTable's data: each possible combination of "zeros" (represented
	 * by the boolean value "false") and "ones" (represented by the boolean 
	 * value "true") of the used variables along with the respective function 
	 * value of the Formula given at construction
	 */
	private Boolean[][] data;
	
	
	
	/**
	 * getter for the used variables
	 * @return
	 */
	public LinkedList<Integer> getVars() {
		return vars;
	}
	
	/**
	 * getter for the TruthTable's data
	 * @return
	 */
	public Boolean[][] getData() {
		return data;
	}
	
	
	/**
	 * constructor for a TruthTable for a given Formula considering a given set
	 * of variables
	 * @param f - the Formula
	 * @param vars - the considered variables
	 */
	public TruthTable (Formula f, LinkedList<Integer> vars) {
		// setting the TruthTale's variables
		this.vars = vars;
		// initializing the data array
		data = new Boolean[(int) Math.pow(2, vars.size())][vars.size() + 1];
		// writing "zeros" and "ones" for the variable values into the data 
		// array iterating over the columns
		for (int column = 0 ; column < vars.size() ; column++) {
			// A row of "zeros" and then "ones" before the next "zero"
			// is considered a "run".
			int maxRun = (int) Math.pow(2, column);
			// A "zero" or "one" after (below) another instance of
			// the same "number" is considered a "repeat".
			int maxRepeat = (int) Math.pow(2, vars.size() - column - 1);
			for (int run = 0 ; run < maxRun ; run++) {
				for (int repeat = 0 ; repeat < maxRepeat; repeat++) {
					// Each "run" first has the repeats of "zeros". 
					data[2 * maxRepeat * run + repeat][column] = false;
					// After (below) the "zeros" there are the repeats of 
					// "ones".
					data[2 * maxRepeat * run + maxRepeat + repeat][column] 
							= true;
				}
			}
		}
		// initializing the list for the assignments to calculate the values
		// for the last column
		LinkedList<Integer> assignedOne = new LinkedList<Integer>();
		// calculating the values one row after another
		for (int row = 0 ; row < (int) Math.pow(2, vars.size()) ; row++) {
			// clearing the assignment list
			assignedOne.clear();
			// adding each variable in which's column is a "one" (in this row) 
			// to the assignment list
			for (int column = 0 ; column < vars.size() ; column++) {
				if (data[row][column]) {
					// If there is a "one" in the column, the respective 
					// variable is added to the assignment list.
					assignedOne.add(vars.get(column));
				}
			}
			// function value of this Formula's function under the assignment
			// represented by the row
			boolean funVal = f.evaluate(assignedOne);
			// transferring the boolean function value into one/zero and
			// writing it into the data array
			data[row][vars.size()] = funVal;
		}
	}
	
	
	/**
	 * provides an complete OBDD from the TruthTable's data
	 * @param varOrd - the VariableOrdering (used for the OBDD constructors)
	 * @return
	 */
	public OBDD toObdd(VariableOrdering varOrd) {
		// the number of variables in the VariableOrdering 
		int varOrdSize = vars.size();
		// The variable for the current node has to be initialized, because 
		// it's the return statement.
		OBDD currentNode = OBDD.ZERO;
		// variables for the current node's children
		OBDD highChild;
		OBDD lowChild;
		// initializing a list for the OBDD nodes of the layer below 
		// the current one
		LinkedList<OBDD> layerBelow = new LinkedList<OBDD>();
		// creating the lowest layer of decision nodes
		for (int node = (int) Math.pow(2, varOrdSize - 1); node > 0; node--) {
			// Each of the children of a node of the lowest decision node layer
			// is a terminal node that can be identified by checking the 
			// TruthTable.
			if (data[(2 * node) - 1][varOrdSize]) {
				highChild = OBDD.ONE;
			} else highChild = OBDD.ZERO;
			if (data[(2 * node) - 2][varOrdSize]) {
				lowChild = OBDD.ONE;
			} else lowChild = OBDD.ZERO;
			// creating the new node
			currentNode = highChild.cons(vars.getLast(), lowChild, varOrd);
			// adding the new node to (the end of) the layer below list
			layerBelow.add(currentNode);
		}
		// creating the other layers of decision nodes
		for (int layer = varOrdSize - 2 ; layer >= 0 ; layer--) {
			// creating the nodes of the current layer
			for (int node = (int) Math.pow(2, layer) ; node > 0 ; node--) {
				// The nodes in the layer below list are ordered the way that 
				// there is always the high child before the low child.
				highChild = layerBelow.poll();
				lowChild = layerBelow.poll();
				// creating the new node
				currentNode = 
						highChild.cons(vars.get(layer), lowChild, varOrd);
				// adding the new node to (the end of) the layer below list
				layerBelow.add(currentNode);
			}
		}
		// returning the current node which finally is the complete OBDD's root
		return currentNode;
	}
}
