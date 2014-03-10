package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import view.MainGui;

/**
 * 
 * @author TimNP
 *
 */
public class GuiController {
	/**
	 * constructor for a MainGuiController
	 */
	public GuiController() {
		//TODO?
	}
	
	
	/**
	 * informs the user that there aren't any previous versions of the current 
	 * OBDD
	 */
	public static void nothingToUndo() {
		messageDialog("There aren't any previous versions of this BDD.");
	}
	
	
	/**
	 * informs the user that there aren't any equivalent nodes in the current 
	 * OBDD
	 */
	public static void noEquivalentNodes() {
		messageDialog("This BDD doesn't have any equivalent nodes.");
	}
	
	
	/**
	 * informs the user that there aren't any redundant nodes in the current 
	 * OBDD
	 */
	public static void noRedundantNode() {
		messageDialog("This BDD doesn't have any redundant nodes.");
	}
	
	
	/**
	 * auxiliary method that shows a message dialog with a given message
	 * @param message
	 */
	private static void messageDialog(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message);
	}
	
	
	/**
	 * informs the user that not all selected/highlighted nodes are equivalent
	 */
	public static void notAllEquivalent() {
		errorDialog(
				"Some of the selected/highlighted nodes aren't equivalent.");
	}
	
	
	/**
	 * informs the user that the selected/highlighted node isn't redundant
	 */
	public static void notRedundant() {
		errorDialog("The selected/highlighted node isnt't redundant.");
	}
	
	
	/**
	 * informs the user that too few nodes are selected/highlighted
	 */
	public static void notEnoughNodesSelected() {
		errorDialog("There aren't enough selected/highlighted nodes.");
	}
	
	
	/**
	 * auxiliary method that shows an error (message) dialog with a given 
	 * method
	 * @param message
	 */
	private static void errorDialog(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * informs the user that some of the OBDD's paths are missing variables and
	 * asks whether the nodes should be added
	 * @return "true" if the answer is "yes", "false" otherwise
	 */
	public static boolean missingVars() {
		return yesOrNoDialog("Some of the BDD's paths are missing variables.\n"
				+ "Should the nodes be added to produce a QOBDD?", "Warning", 
				JOptionPane.WARNING_MESSAGE);
	}
	
	
	/**
	 * asks the user whether the formula should be reduced
	 * @return "true" if the answer is "yes", "false" otherwise
	 */
	public static boolean reduceFormula() {
		return yesOrNoDialog("Should the represented formula be reduced?", 
				"Question", JOptionPane.QUESTION_MESSAGE);
	}
	
	
	/**
	 * auxiliary method that shows an option dialog with the given question and
	 * only "yes" and "no" as possible answers
	 * @param question - the question
	 * @param dialogName - the dialog's name
	 * @param messageType - the dialog's message type
	 * @return "true" if the answer is "yes", "false" otherwise
	 */
	private static boolean yesOrNoDialog(String question, String dialogName, 
			int messageType) {
		// array for the options
		String[] options = {"Yes", "No"};
		// obtaining the answer
		int answer = JOptionPane.showOptionDialog(new JFrame(), question, 
				dialogName, JOptionPane.YES_NO_OPTION, messageType, null, 
				options, options[0]);
		return (answer == 0);
	}
	
	
	/**
	 * shows two given formula strings
	 * @param initialFormula
	 * @param representedFormula
	 */
	public static void showFormulas(String initialFormula, 
			String representedFormula) {
		messageDialog("Initial Formula: " + initialFormula + 
				"\nRepresented Formula: " + representedFormula);
	}
	
	
	/**
	 * informs the user that the chosen name for a OBDD has already been taken 
	 * and how it got named instead
	 * @param newName
	 */
	public static void nameAlreadyTaken(String newName) {
		messageDialog("There already is a BDD with the chosen name.\n" 
				+ "The new BDD has been named '" + newName + "' instead.");
	}
	
	
	/**
	 * informs the user that no name for the OBDD has been provided and how it 
	 * got named
	 * @param name
	 */
	public static void noNameGiven(String name) {
		messageDialog("You didn't provide a name for the BDD.\n" + 
				"It has been named '" + name + "' by default.");
	}
}
