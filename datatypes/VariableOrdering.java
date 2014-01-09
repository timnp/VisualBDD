package datatypes;

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
		return this.ordList;
	}
	
	/**
	 * setter for the ordering list
	 * @param ordList
	 */
	public void setOrdList(LinkedList<Integer> ordList) {
		// initializing a "storage" for the moved variable
		int movedVar;
		// moving variables until the input list is empty
		while (!ordList.isEmpty()) {
			// moving the first variable first to keep the order
			movedVar = ordList.getFirst();
			// TODO?
			// tentative solution for multiple appearances of the same integer:
			// removing all but the first appearance 
			while (ordList.lastIndexOf(movedVar) != -1) {
				ordList.removeLastOccurrence(movedVar);
			}
			// adding the variable to this objects list
			this.ordList.add(movedVar);
		}	}
	
	
	/**
	 * constructor for a VariableOrdering with variables of the type X0,X1,...
	 * @param ordList
	 */
	public VariableOrdering (LinkedList<Integer> ordList) {
		// initializing a "storage" for the moved variable
		int movedVar;
		// moving variables until the input list is empty
		while (!ordList.isEmpty()) {
			// moving the first variable first to keep the order
			movedVar = ordList.getFirst();
			// TODO?
			// tentative solution for multiple appearances of the same integer:
			// removing all but the first appearance 
			while (ordList.lastIndexOf(movedVar) != -1) {
				ordList.removeLastOccurrence(movedVar);
			}
			// adding the variable to this objects list
			this.ordList.add(movedVar);
		}
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
	 * function that states if a variable has a lower position in this
	 * variable ordering than another
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public boolean lowerPosition(int a, int b) {
		// a higher index means a "lower" position
		if (this.ordList.indexOf(a) > this.ordList.indexOf(b)) {
			return true;
		} else return false;
	}
	
	
	/**
	 * function that states if a variable has a higher position in this
	 * variable ordering than another
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public boolean higherPosition(int a, int b) {
		// a lower index means a "higher" position
		if (this.ordList.indexOf(a) < this.ordList.indexOf(b)) {
			return true;
		} else return false;
	}
	
	
	/**
	 * method that removes all variables in the VariableOrdering 
	 * before the given one
	 * @param var
	 */
	public void removeBefore(int var) {
		// getting the index of the given variable
		int index = this.indexOf(var);
		// removing all variables before the given one
		for (int i = 0; i < index; i++) {
			this.ordList.removeFirst();
		}
	}

}