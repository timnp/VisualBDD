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
	 * constructor for a VariableOrdering with variables of the type X0,X1,...
	 * @param ordList
	 */
	public VariableOrdering (LinkedList<Integer> ordList) {
		/**
		 * initializing a "storage" for the moved variable
		 */
		int movedVar;
		/**
		 * moving variables until the input list is empty
		 */
		while (!ordList.isEmpty()) {
			/**
			 * moving the first variable first to keep the order
			 */
			movedVar = ordList.getFirst();
			/**
			 * TODO?
			 * tentative solution for multiple appearances of the same integer:
			 * removing all but the first appearance 
			 */
			while (ordList.lastIndexOf(movedVar) != -1) {
				ordList.removeLastOccurrence(movedVar);
			}
			/**
			 * adding the variable to this objects list
			 */
			this.ordList.add(movedVar);
		}
	}

}
