package controller;

import java.util.LinkedList;

import javax.swing.JTable;

import com.Ostermiller.util.ArrayIterator;

import model.Formula;
import model.TruthTable;

public class TruthTableController {
	/**
	 * provides a JTable displaying the truth table created from the given 
	 * formula and variable list
	 * @param formula
	 * @param vars
	 * @return
	 */
	public static JTable truthTabletoJTable(Formula formula, 
			LinkedList<Integer> vars) {
		// creating the truth table
		TruthTable tt = new TruthTable(formula, vars);
		// retrieving the truth table's variable list
		LinkedList<Integer> ttVars = tt.getVars();
		// the number of variables
		int numberOfVars = ttVars.size();
		// an iterator over the variable list
		java.util.Iterator<Integer> varsIter = ttVars.iterator();
		// initializing the JTable's column names
		String[] columnNames = new String[numberOfVars + 1];
		// initializing the column position
		int columnPos = 0;
		// for each variable adding it with an "X" to the column name array
		while (varsIter.hasNext()) {
			columnNames[columnPos] = "X" + varsIter.next();
			columnPos++;
		}
		// setting the last column name
		columnNames[numberOfVars] = "f";
		// retrieving the truth table's data array
		Boolean[][] ttData = tt.getData();
		// the data arrays width and height
		int dataWidth = ttData[0].length;
		int dataHeight = ttData.length;
		// initializing the JTable's data array
		Object[][] data = new Object[dataHeight][dataWidth];
		// filling the JTable's data array
		for (int row = 0; row < dataHeight; row++) {
			for (int column = 0; column < dataWidth; column++) {
				// transferring the booleans into ones and zeros
				if (ttData[row][column]) data[row][column] = 1;
				else data[row][column] = 0;
			}
		}
		// returning the resulting JTable
		return new JTable(data, columnNames);
	}
}
