package datatypes;

/**
 * 
 * @author TimNP
 *
 */
public class Formula {
	/**
	 * first successor (for operations)
	 */
	private Formula firstSuccessor;
	/**
	 * second successor (for binary operations)
	 */
	private Formula secondSuccessor;
	/**
	 * number for operations:
	 * 0 means NOT
	 * 1 means AND
	 * 2 means OR
	 */
	private int operation;
	/**
	 * variable number
	 */
	private int varNr;
	/**
	 * logical value
	 */
	private boolean value;
	
	
	/**
	 * smart "constructor" for variables
	 * @param varNr
	 * @return Formula representing the chosen variable
	 */
	public Formula variable(int varNr) {
		Formula variable = new Formula();
		variable.varNr = varNr;
		return variable;
	}
	
	
	/**
	 * smart "constructor" for logical negation
	 * @param successor
	 * @return Formula representing a logical negation
	 */
	public Formula not() {
		Formula not = new Formula();
		not.firstSuccessor = this;
		not.operation = 0;
		return not;
	}
	
	
	/**
	 * smart "constructor" for logical conjunction
	 * @param secondSuccessor
	 * @return Formula representing a logical conjunction
	 */
	public Formula and(Formula secondSuccessor) {
		Formula and = new Formula();
		and.firstSuccessor = this;
		and.secondSuccessor = secondSuccessor;
		and.operation = 1;
		return and;
	}
	
	
	/**
	 * smart "constructor" for logical disjunction
	 * @param secondSuccessor
	 * @return Formula representing a logical disjunction
	 */
	public Formula or(Formula secondSuccessor) {
		Formula or = new Formula();
		or.firstSuccessor = this;
		or.secondSuccessor = secondSuccessor;
		or.operation = 2;
		return or;
	}
}
