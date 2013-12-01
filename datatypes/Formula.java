package datatypes;

/**
 * 
 * @author TimNP
 *
 */
public class Formula {
	/**
	 * left successor (for binary operations)
	 */
	private Formula leftSuccessor;
	/**
	 * binary operation
	 */
	private BinaryOperation op;
	/**
	 * right successor (for binary operations)
	 */
	private Formula rightSuccessor;
	/**
	 * successor (for unary operations)
	 */
	private Formula successor;
	/**
	 * variable
	 */
	private String variable;
	/**
	 * logical value
	 */
	private boolean value;
	
	
	/**
	 * constructor for binary operations 'and' and 'or'
	 * @param leftSuccessor
	 * @param rightSuccessor
	 * @param and
	 */
	public Formula(Formula leftSuccessor, BinaryOperation op, Formula rightSuccessor) {
		this.op = op;
		this.leftSuccessor = leftSuccessor;
		this.rightSuccessor = rightSuccessor;
	}
	
	
	/**
	 * constructor for the unary operation 'not'
	 * @param successor
	 */
	public Formula(Formula successor) {
		this.successor = successor;
	}
	
	
	/**
	 * constructor for variables
	 * @param variable
	 */
	public Formula(String variable) {
		this.variable = variable;
	}
	
	
	/**
	 * constructor for logical values/constants
	 * @param value
	 */
	public Formula(boolean value) {
		this.value = value;
	}
}
