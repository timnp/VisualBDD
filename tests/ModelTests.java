package tests;

import java.util.LinkedList;

import datatypes.*;

/**
 * 
 * @author TimNP
 * 
 * class for test cases
 *
 */
public class ModelTests {

	public static void main(String[] args) {
		// a list of "variables"
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(2);
		list.add(4);
		list.add(3);
		list.add(1);
		// constructing a VariableOrdering from the list
		VariableOrdering varOrd = new VariableOrdering(list);
		// constructing a Formula from a String (with varying spaces)
		Formula f = new Formula("( - ((  ( (-   X1 )+  X2 )    *X3  )*(- (  (" 
								+ " X2*X3   )*  X4))  ) )");
		// turning Formula f back into a String
		String fString = f.toString();
		// checking whether Formula f was "read" correctly
		boolean fCorrect = 
				fString.equals("(-((((-X1) + X2) * X3) * (-((X2 * X3) * X4))))");
		// Formula f's entire TruthTable
		TruthTable fTruthTable = f.entireTruthTable(varOrd);
		// constructing a complete OBDD from the TruthTable
		OBDD fOBDD = fTruthTable.toOBDD(varOrd);
		// constructing a Formula from the OBDD
		Formula f2 = fOBDD.toFormula();
		// turning Formula f2 into a String
		String f2String = f2.toString();
		// constructing f2's entire TruthTable
		TruthTable f2TruthTable = f2.entireTruthTable(varOrd);
		// checking whether the two TruthTables have the same data
		boolean sameTruthTable = 
				fTruthTable.getData().equals(f2TruthTable.getData());
		
		System.out.println("Formula f was interpreted correctly: " + fCorrect +
				"\nFormula f looks like this: " + fString + 
				"\nThe Formulas f and f2 have equal TruthTables: " + 
				sameTruthTable + "\nFormula f2 looks like this: " + f2String);
	}

}
