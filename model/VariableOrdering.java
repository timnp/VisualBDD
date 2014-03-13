package model;

import java.util.LinkedList;

/**
 * 
 * @author TimNP
 *
 */
public class VariableOrdering {
	/**
	 * list representing the actual variable ordering
	 */
	private LinkedList<Integer> ordList;
	
	
	/**
	 * getter for the ordering list
	 * @return the ordering list
	 */
	public LinkedList<Integer> getOrdList() {
		return ordList;
	}
	
	/**
	 * setter for the ordering list
	 * @param ordList
	 */
	public void setOrdList(LinkedList<Integer> ordList) {
		// initializing the ordering list
		this.ordList = new LinkedList<Integer>();
		// initializing a "storage" for the moved variable
		int movedVar;
		// moving variables until the input list is empty
		while (!ordList.isEmpty()) {
			// moving the first variable first to keep the order
			movedVar = ordList.removeFirst();
			// TODO?
			// tentative solution for multiple appearances of the same integer:
			// removing all but the first appearance 
			while (ordList.lastIndexOf(movedVar) >= 0) {
				ordList.removeLastOccurrence(movedVar);
			}
			// adding the variable to this objects list
			this.ordList.add(movedVar);
			}
		}
	
	
	/**
	 * constructor for a VariableOrdering with variables of the type X0,X1,...
	 * @param ordList
	 */
	public VariableOrdering (LinkedList<Integer> ordList) {
		// setting the ordering list
		setOrdList(ordList);
	}
	
	
	/**
	 * function that provides a variable's position in the order
	 * @param var
	 * @return the variable's position (as integer)
	 */
	public int indexOf(int var) {
		return this.ordList.indexOf(var);
	}
	
	
	/**
	 * method that provides a VariableOrdering like this one, except that all
	 * variables in the VariableOrdering before the given one are removed
	 * @param var
	 * @return the reduced VariableOrdering
	 */
	public VariableOrdering removeBefore(int var) {
		// initializing the new VariableOrdering as this one
		VariableOrdering reducedOrd = this;
		// getting the index of the given variable
		int index = reducedOrd.indexOf(var);
		// removing all variables in the VariableOrdering before the given one
		for (int i = 0; i < index; i++) {
			reducedOrd.ordList.removeFirst();
		}
		// returning the reduced VariableOrdering
		return reducedOrd;
	}
	
	
	/**
	 * @return whether the VariableOrdering is empty
	 */
	public boolean isEmpty() {
		return this.ordList.isEmpty();
	}
	
	
	/**
	 * @return the number of variables in the VariableOrdering
	 */
	public int size() {
		return this.ordList.size();
	}
	
	
	/**
	 * @return the VariableOrdering's first (highest) variable
	 */
	public int getFirst() {
		return this.ordList.getFirst();
	}
	
	
	/**
	 * @return the VariableOrdering's last (lowest) variable
	 */
	public int getLast() {
		return this.ordList.getLast();
	}
	
	
	/**
	 * @param index
	 * @return the VariableOrdering's variable at the given index
	 */
	public int get(int index) {
		return this.ordList.get(index);
	}

}