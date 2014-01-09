package datatypes;

import java.util.Comparator;

/**
 * 
 * @author TimNP
 *
 */
public class VariableOrderingComparator implements Comparator<Integer>{
	/**
	 * the VariableOrdering the variables (Integers) are sorted by
	 */
	private VariableOrdering varOrd;
	
	
	/**
	 * constructor for VariableOrderingComparators
	 * @param varOrd
	 */
	public VariableOrderingComparator(VariableOrdering varOrd) {
		this.varOrd = varOrd;
	}
	
	
	/**
	 * method that compares two variables (Integers) by means of the 
	 * comparator's VariableOrdering
	 */
	@Override
	public int compare(Integer a, Integer b) {
		// getting the indexes of the two variables in the VariableOrdering
		int indexA = varOrd.indexOf(a);
		int indexB = varOrd.indexOf(b);
		// If the two indexes are the same, the two variables are equal 
		// by means of the VariableOrdering.
		if (indexA == indexB) {
			return 0;
		}
		// If the first variable isn't part of the VariableOrdering, the second 
		// one is higher by means of the VariableOrdering.
		else if (indexA < 0) {
			return -1;
		}
		// If the second variable isn't part of the VariableOrdering, the first 
		// one is higher by means of the VariableOrdering.
		else if (indexB <0) {
			return 1;
		}
		// Otherwise the two variables are both part of the VariableOrdering 
		// and aren't equal by its means. A lower index means a higher position 
		// in a VariableOrdering and vice versa.
		else return indexB - indexA;
	}

}
