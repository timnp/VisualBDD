package datatypes;

/**
 * 
 * @author TimNP
 *
 * @param <E>
 */
public class Pair<E> {
	/**
	 * the pair's first element
	 */
	private E first;
	/**
	 * the pair's second element
	 */
	private E second;

	/**
	 * default constructor for a pair
	 */
	public Pair() {

	}
	
	/**
	 * constructor for a pair
	 * @param first
	 * @param second
	 */
	public Pair(E first,E second) {
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
	public E getSecond() {
		return this.second;
	}
	
	/**
	 * set the pair's second element
	 * @param second
	 */
	public void setSecond(E second) {
		this.second = second;
	}
}
