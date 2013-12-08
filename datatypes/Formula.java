package datatypes;

import java.util.LinkedList;

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
	 * number for used constructor:
	 * 1 stands for a variable
	 * 2 stands for NOT
	 * 3 stands for AND
	 * 4 stands for OR
	 */
	private int constructor;
	/**
	 * variable number
	 */
	private int varNr;
	
	
	/**
	 * smart "constructor" for variables
	 * @param varNr
	 * @return Formula representing the chosen variable
	 */
	public Formula variable(int varNr) {
		Formula variable = new Formula();
		variable.varNr = varNr;
		variable.constructor = 1;
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
		not.constructor = 2;
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
		and.constructor = 3;
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
		or.constructor = 4;
		return or;
	}
	
	
	/**
	 * Evaluates the Formula under a given assignment.
	 * @param assignedOne - list of all variables assigned one
	 * @return the value of the Formula as a boolean
	 */
	public boolean assign(LinkedList<Integer> assignedOne) {
		/**
		 * a switch for the possible constructors for this Formula
		 */
		switch (constructor) {
			/**
			 * First case: The Formula represents a variable.
			 */
			case 1:
				/**
				 * If the variable is assigned the logical value one,
				 * true is returned. 
				 */
				if(assignedOne.contains(this.varNr)) {
					return true;
				}
				/**
				 * Otherwise the variable is assigned the logical value zero
				 * or it isn't assigned anything. Therefore false is returned. 
				 */
				return false;
			/**
			 * Second case: The Formula represents a logical negation.
			 */
			case 2:
				/**
				 * calculating the value of the successor
				 */
				boolean firstSucVal = this.firstSuccessor.assign(assignedOne);
				/**
				 * returning the negated value of the successor
				 */
				return !firstSucVal;
			/**
			 * Third case: The Formula represents a logical conjunction.
			 */
			case 3:
				/**
				 * calculating the value of the first successor
				 */
				firstSucVal = this.firstSuccessor.assign(assignedOne);
				/**
				 * calculating the value of the second successor
				 */
				boolean secondSucVal = this.secondSuccessor.assign(assignedOne);
				/**
				 * returning the conjunction of the two successor values
				 */
				return firstSucVal && secondSucVal;
			/**
			 * Fourth case: The Formula represents a logical disjunction.
			 */
			case 4:
				/**
				 * calculating the value of the first successor
				 */
				firstSucVal = this.firstSuccessor.assign(assignedOne);
				/**
				 * calculating the value of the second successor
				 */
				secondSucVal = this.secondSuccessor.assign(assignedOne);
				/**
				 * returning the disjunction of the two successor values
				 */
				return firstSucVal || secondSucVal;
			/**
			 * Default case: None of the constructors was used.
			 */
			default:
				/**
				 * tentative default value: false
				 */
				// TODO user message
				return false;
		}
	}
}
