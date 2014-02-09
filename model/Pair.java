package model;

/**
 * 
 * @author TimNP
 *
 */
public class Pair<E,F> {
	/**
	 * the pair's first element
	 */
	private E first;
	/**
	 * the pair's second element
	 */
	private F second;
	
	/**
	 * constructor for a pair
	 * @param first
	 * @param second
	 */
	public Pair(E first, F second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @return the pair's first element
	 */
	public E getFirst() {
		return this.first;
	}
	
	/**
	 * set the pair's first element
	 * @param first
	 */
	public void setFirst(E first) {
		this.first = first;
	}
	
	/**
	 * @return the pair's second element
	 */
	public F getSecond() {
		return this.second;
	}
	
	/**
	 * set the pair's second element
	 * @param second
	 */
	public void setSecond(F second) {
		this.second = second;
	}
}
