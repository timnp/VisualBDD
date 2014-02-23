package tests;
import java.util.LinkedList;

import model.*;
import controller.*;
import view.*;

/**
 * 
 * @author TimNP
 *
 */
public class ExperimentalTests {
	
	public static void main(String[] args) {
		String fString = "(x1+X3) * -(X2 *x1)";
		Formula f = new Formula(fString);
		LinkedList<Integer> ordList = new LinkedList<Integer>();
		for (int i = 1; i <= 5; i++) {
			ordList.add(i);
		}
		VariableOrdering varOrd = new VariableOrdering(ordList);
		OBDD bdd = f.toObdd(varOrd);
		AbstractObddLayout layout = new AbstractObddLayout(bdd);
		MainGui gui = new MainGui();
		gui.showObdd(layout);
	}

}
