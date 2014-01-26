package tests;

import model.*;
import controller.*;

public class ControllerTests {
	public static void main(String[] args) {
		String input = " -X3  *x1  +-(X2   + X1*x4  * x3) ";
		Formula f = FormulaController.stringToFormula(input);
		String output = f.toString();
		System.out.println(output);
	}
}
