package tests;

import java.util.LinkedList;

import model.*;
import controller.*;

public class ControllerTests {
	public static void main(String[] args) {
		// a "messy" but possible input String for a Formula
		String f_string = "x2 + x1 *  -(X2 * x1) * -x3";
		// converting the String into a Formula String
		String f_fstring = FormulaController.toFormulaString(f_string);
		// creating a Formula from the Formula String
		Formula f = new Formula(f_fstring);
		// converting the Formula back into a Formula String
		String f_fstring2 = f.toString();
		// printing all three Strings representing the same Formula
		System.out.println(f_string);
		System.out.println(f_fstring);
		System.out.println(f_fstring2);
		// creating a VariableOrdering for the OBDD representing the Formula
		LinkedList<Integer> varOrdList = new LinkedList<Integer>();
		for (int i = 1; i <= 3; i++) {
			varOrdList.add(i);
		}
		VariableOrdering varOrd = new VariableOrdering(varOrdList);
		// creating the OBDD
		OBDD f_OBDD = f.toObdd(varOrd);
		// converting the OBDD into an ROBDD
		OBDD f_ROBDD = f_OBDD.toRobdd(varOrd);
		// retrieving the reduced Formula represented by the ROBDD
		Formula f2 = f_ROBDD.toFormula().reduce();
		// converting the Formula into a Formula String
		String f2_fstring = f2.toString();
		// printing the Formula String
		System.out.println(f2_fstring);
		// checking whether the two Formulas are logical equivalent
		boolean logicalEquiv = f.logicalEquivalent(f2);
		System.out.println(logicalEquiv);
	}
}
