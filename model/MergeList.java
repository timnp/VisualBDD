package model;

import java.util.LinkedList;

/**
 * 
 * @author TimNP
 *
 * @param <E>
 */
public class MergeList<E> extends LinkedList<E>{
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * adds all elements of a given list that aren't already in this list to 
	 * this list
	 * @param otherList
	 */
	public void mergeAll(MergeList<E> otherList) {
		// going through the entire given list
		while (!otherList.isEmpty()) {
			// retrieving the given list's next element
			E currentElement = otherList.removeFirst();
			// If the element isn't in already this list, it gets added. 
			if (!contains(currentElement)) add(currentElement);
		}
	}
}
