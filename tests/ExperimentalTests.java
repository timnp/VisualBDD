package tests;


import model.Formula;
import controller.FormulaController;

public class ExperimentalTests {
	
	public static void main(String[] args) {
		String f_string = "x2 + x1 *  -(X2 * x1) * -x3";
		String f_fstring = FormulaController.toFormulaString(f_string);
		Formula f = new Formula(f_fstring);
		String f_fstring2 = f.toString();
		System.out.println(f_fstring2);
	}

}
