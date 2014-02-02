package tests;

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
	}
}
