package datatypes;

import java.util.LinkedList;

import javax.swing.JTable;

/**
 * 
 * @author TimNP
 * 
 * class for test cases
 *
 */
public class DatatypeTests {

	public static void main(String[] args) {
		// some variables
		Formula x1 = new Formula(1);
		Formula x2 = new Formula(2);
		Formula x3 = new Formula(3);
		// some "extended" Formulas
		Formula a = x1.or(x2);
		Formula b = x3.not();
		Formula c = a.and(b);
		// assignment: x1=1, x2=0, x3=1
		LinkedList<Integer> assignment = new LinkedList<Integer>();
		assignment.add(1);
		assignment.add(3);
		// evaluating the Formulas
		boolean x1_eval = x1.assign(assignment);
		boolean x2_eval = x2.assign(assignment);
		boolean x3_eval = x3.assign(assignment);
		boolean a_eval = a.assign(assignment);
		boolean b_eval = b.assign(assignment);
		boolean c_eval = c.assign(assignment);
		// entire truth table for ((x1 or x2) and not(x3))
		JTable c_ett = c.entireTruthTable();
		// turning the truth table into a string
		String c_ett_s = "\n";
		for(int j = 0; j < c_ett.getColumnCount() - 1; j++) {
			c_ett_s += c_ett.getColumnName(j) + " |";
		}
		c_ett_s += "|" + c_ett.getColumnName(c_ett.getColumnCount() - 1) 
				+ "\n---|---|---||------------\n";
		for (int i = 0; i < c_ett.getRowCount(); i++) {
			for(int j = 0; j < c_ett.getColumnCount() - 1; j++) {
				c_ett_s += " " + c_ett.getValueAt(i, j).toString() + " |";
			}
			c_ett_s += "| " + c_ett.getValueAt(i, c_ett.getColumnCount() - 1);
			c_ett_s += "\n";
		}
		// "reading" a Formula: ((x1 and not(x2)) or not(x3))
		Formula d = new Formula("((X1 * (-X2)) + (-X3))");
		// turning the Formula back into a string
		String d_string = d.toString();
		// constructing a small OBDD
		OBDD nodeD = OBDD.ONE.cons(3, OBDD.ZERO);
		OBDD nodeC = nodeD.cons(2, OBDD.ZERO);
		OBDD nodeB = OBDD.ONE.cons(2, nodeD);
		OBDD nodeA = nodeB.cons(1, nodeC);
		// the OBDD's (Node A's) Formula
		Formula nodeAFormula = nodeA.toFormula();
		// turning the OBDD's Formula into a string
		String nodeAFormula_String = nodeAFormula.toString();
		
		// displaying the results
		System.out.println("x1 is " + x1_eval + "\nx2 is " + x2_eval + "\n"
		+ "x3 is " + x3_eval + "\n(x1 or x2) is " + a_eval + "\n"
		+ "not(x3) is " + b_eval + "\n((x1 or x2) and not(x3)) is " + c_eval
		+ "\n\nEntire truth table for ((x1 or x2) and not(x3)):" + c_ett_s
		+ "\n((X1 * (-X2)) + (-X3)) =?=\n" + d_string 
		+ "\nThe OBDD's Formula:\n" + nodeAFormula_String);
	}

}
