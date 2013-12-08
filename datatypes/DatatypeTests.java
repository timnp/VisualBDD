package datatypes;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author TimNP
 * 
 * class for test cases
 *
 */
public class DatatypeTests {

	public static void main(String[] args) {
		/**
		 * empty Formula for smart "constructor" calls
		 */
		Formula base = new Formula();
		/**
		 * some variables
		 */
		Formula x1 = base.variable(1);
		Formula x2 = base.variable(2);
		Formula x3 = base.variable(3);
		/**
		 * some "extended" Formulas
		 */
		Formula a = x1.or(x2);
		Formula b = x3.not();
		Formula c = a.and(b);
		/**
		 * assignment: x1=1, x2=0, x3=1
		 */
		LinkedList<Integer> assignment = new LinkedList<Integer>();
		assignment.add(1);
		assignment.add(3);
		/**
		 * evaluating the Formulas
		 */
		boolean x1_eval = x1.assign(assignment);
		boolean x2_eval = x2.assign(assignment);
		boolean x3_eval = x3.assign(assignment);
		boolean a_eval = a.assign(assignment);
		boolean b_eval = b.assign(assignment);
		boolean c_eval = c.assign(assignment);
		/**
		 * displaying the results
		 */
		System.out.println("x1 is " + x1_eval + "\nx2 is " + x2_eval + "\n"
		+ "x3 is " + x3_eval + "\n(x1 or x2) is " + a_eval + "\n"
		+ "not(x3) is " + b_eval + "\n((x1 or x2) and not(x3)) is " + c_eval);
	}

}
