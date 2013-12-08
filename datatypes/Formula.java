package datatypes;

import java.util.LinkedList;

import javax.swing.JTable;

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
		 * Default case: None of the given constructors was used.
		 */
		default:
			/**
			 * tentative default value: false
			 */
			// TODO user message
			return false;
		}
	}
	
	
	/**
	 * @return the numbers of the variables used in
	 * the Formula and its sub-Formulas
	 */
	private LinkedList<Integer> vars() {
		/**
		 * initializing the LinkedList for the return
		 */
		LinkedList<Integer> vars = new LinkedList<Integer>();
		/**
		 * a switch for the possible constructors for this Formula
		 */
		switch (constructor) {
		/**
		 * First case: The Formula represents a variable.
		 */
		case 1:
			/**
			 * clearing the list
			 */
			vars.clear();
			/**
			 * adding the variable's number to the list
			 */
			vars.add(this.varNr);
			/**
			 * returning the list
			 */
			return vars;
		/**
		 * Second case: The Formula represents a logical negation.
		 */
		case 2:
			/**
			 * clearing the list
			 */
			vars.clear();
			/**
			 * adding all variable numbers of the successor to the list
			 */
			vars.addAll(this.firstSuccessor.vars());
			/**
			 * returning the list
			 */
			return vars;
		/**
		 * Third case: The Formula represents a logical conjunction.
		 */
		case 3:
			/**
			 * clearing the list
			 */
			vars.clear();
			/**
			 * adding all variable numbers of the first successor to the list
			 */
			vars.addAll(this.firstSuccessor.vars());
			/**
			 * getting all variable numbers of the second successor
			 */
			LinkedList<Integer> secondSucVars = this.secondSuccessor.vars();
			/**
			 * merging the list with the second successor's variable numbers
			 */
			for(Integer i : secondSucVars) {
				/**
				 * adding the variable number to the list if it isn't already in
				 */
				if(!vars.contains(i)) {
					vars.add(i);
				}
			}
			/**
			 * returning the list
			 */
			return vars;
		/**
		 * Fourth case: The Formula represents a logical disjunction.
		 */
		case 4:
			/**
			 * clearing the list
			 */
			vars.clear();
			/**
			 * adding all variable numbers of the first successor to the list
			 */
			vars.addAll(this.firstSuccessor.vars());
			/**
			 * getting all variable numbers of the second successor
			 */
			secondSucVars = this.secondSuccessor.vars();
			/**
			 * merging the list with the second successor's variable numbers
			 */
			for(Integer i : secondSucVars) {
				/**
				 * adding the variable number to the list if it isn't already in
				 */
				if(!vars.contains(i)) {
					vars.add(i);
				}
			}
			/**
			 * returning the list
			 */
			return vars;
		/**
		 * Default case: None of the given constructors was used.
		 */
		default:
			/**
			 * tentative default value: the empty list
			 */
			// TODO user message
			vars.clear();
			return vars;
		}
	}
	
	
	public JTable entireTruthTable() {
		/**
		 * initializing the greatest and least numbers
		 * among the Formula's variables
		 */
		int maxVarNo = -1;
		int minVarNo = -1;
		/**
		 * checking for each of the Formula's variable numbers
		 * whether it's the greatest or the least
		 */
		for (int i : this.vars()) {
			if (i > maxVarNo || maxVarNo == -1) {
				/**
				 * if a number is greater than the current maximum or there is
				 * no maximum, the number becomes the new maximum
				 */
				maxVarNo = i;
			}
			if (i < minVarNo || minVarNo == -1) {
				/**
				 * if a number is less than the current minimum or there is
				 * no minimum, the number becomes the new minimum
				 */
				minVarNo = i;
			}
		}
		/**
		 * initializing a list for all variable numbers from minVarNo
		 * to maxVarNo
		 */
		LinkedList<Integer> vars = new LinkedList<Integer>();
		/**
		 * adding the variable numbers to the list
		 */
		for (int i = minVarNo ; i <= maxVarNo ; i++) {
			vars.add(i);
		}
		/**
		 * returning the truth table for "all" variables
		 */
		return truthTable(vars);
	}
	
	
	private JTable truthTable(LinkedList<Integer> vars) {
		/**
		 * initializing the column name array
		 */
		String[] columnNames = new String[vars.size() + 1];
		/**
		 * making each of the Formula's variables a column name
		 */
		for (int i : vars) {
			columnNames[i - vars.getFirst()] = "X"+i;
		}
		/**
		 * making the function value the last column name
		 */
		columnNames[vars.size()] =
				"f(X" + vars.getFirst() + ",...,X" + vars.getLast() + ")";
		/**
		 * initializing the data array
		 */
		Integer[][] data = new Integer[vars.size() + 1]
						[(int) Math.pow(2, vars.size())];
		/**
		 * writing zeros and ones for the variable values into the data array
		 * 
		 * iterating over the columns
		 */
		for (int column = 0 ; column < vars.size() ; column++) {
			/**
			 * A row of zeros and then ones before the next zero
			 * is considered a "run".
			 */
			for (int run = 0 ; run < Math.pow(2, column) ; run++) {
				/**
				 * A zero or one after (below) another instance of
				 * the same number is considered a "repeat".
				 */
				for (int repeat = 0 ; repeat < Math.pow(2, vars.size() - column- 1); repeat++) {
					/**
					 * Each "run" first has the repeats of zeros. 
					 */
					data[column][(int) Math.pow(2, vars.size() - run) * run + repeat] = 0;
					/**
					 * After (below) the zeros there are the repeats of ones.
					 */
					data[column][(int) (Math.pow(2, vars.size() - run) * run +
							Math.pow(2, vars.size() - run - 1)) + repeat] = 1;
				}
			}
		}
		/**
		 * initializing the list for the assignments to calculate the values
		 * for the last column
		 */
		LinkedList<Integer> assignedOne = new LinkedList<Integer>();
		/**
		 * calculating the values one row after another
		 */
		for (int row = 0 ; row < Math.pow(2, vars.size()) ; row++) {
			/**
			 * clearing the assignment list
			 */
			assignedOne.clear();
			/**
			 * adding each variable in which's column is a one (in this row)
			 * to the assignment list
			 */
			for (int column = 0 ; column < vars.size() ; column++) {
				if (data[column][row] == 1) {
					/**
					 * If there is a one in the column, the respective variable
					 * is added to the assignment list.
					 */
					assignedOne.add(vars.get(column));
				}
			}
			/**
			 * function value of this Formula's function under the assignment
			 * represented by the row
			 */
			boolean funVal = this.assign(assignedOne);
			/**
			 * transferring the boolean function value into one/zero and
			 * writing it into the data array
			 */
			if (funVal) {
				data[vars.size()][row] = 1;
			}
			else data[vars.size()][row] = 0;
		}
		/**
		 * creating the function table as a concrete JTable
		 */
		JTable entireFunTab = new JTable(data, columnNames);
		/**
		 * returning the function table
		 */
		return entireFunTab;
	}
}
