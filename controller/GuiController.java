package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Pair;
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
	 * informing the user that the given formula (input string) doesn't fulfill
	 * the requirements and asks for another one
	 * @param givenFormulaString
	 * @return the new formula (input string)
	 */
	public static String improperFormulaInputString(String givenFormulaInputString) {
		return improperInput(
				"The given formula doesn't fulfill the requirements.\n" + 
				"Please change it so it does.", 
				givenFormulaInputString);
	}
	
	
	/**
	 * informs the user that the given name for the OBDD is either empty or 
	 * already taken and asks for another one
	 * @param givenName
	 * @return the new name
	 */
	public static String improperName(String givenName) {
		return improperInput(
				"The given name is either empty or already taken.\n" + 
				"Please change it to a new non-empty one.", givenName);
	}
	
	
	/**
	 * informs the user that the given variable ordering (string) doesn't 
	 * fulfill the requirements and asks for another one
	 * @param givenVarOrdString
	 * @return the new variable ordering (string)
	 */
	public static String improperVarOrdString(String givenVarOrdString) {
		return improperInput("The given variable ordering doesn't fulfill " + 
				"the requirements.\nPlease change it so it does.", 
				givenVarOrdString);
	}
	
	
	/**
	 * auxiliary method that shows an input dialog with the given message and 
	 * no given options
	 * @param message
	 * @return the user's answer (input)
	 */
	private static String improperInput(String message, String givenInput) {
		return (String) JOptionPane.showInputDialog(new JFrame(), 
				message, "Improper Input", JOptionPane.INFORMATION_MESSAGE, null, 
				null, givenInput);
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
	 * informs the user that no OBDD was selected
	 */
	public static void noObddSelected() {
		errorDialog("You didn't select a BDD.");
	}
	
	
	/**
	 * informs the user that the selected OBDD couldn't be found
	 */
	public static void noSuchObdd() {
		errorDialog("The selected BDD couldn't be found.");
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
	
	
	/**
	 * asks the user which binary operation should be applied on the two OBDDs
	 * @param firstName - the first OBDD's name
	 * @param secondName - the second OBDD's name
	 * @return the number of the chosen operation; 
	 * 		   -1 if the dialog was cancelled
	 */
	public static int applyOperation(String firstName, String secondName) {
		// creating the options
		String[] options = {"Contradiction", 
				firstName + " AND " + secondName, 
				firstName + " greater than " + secondName, 
				"Identity of " + firstName, 
				secondName + " greater than " + firstName, 
				"Identity of " + secondName, 
				firstName + " XOR " + secondName,
				firstName + " OR " + secondName,
				firstName + " NOR " + secondName,
				firstName + " equals " + secondName,
				"NOT " + secondName,
				secondName + " implies " + firstName,
				"NOT " + firstName,
				firstName + " implies " + secondName,
				firstName + " NAND " + secondName,
				"Tautology"};
		// obtaining the answer
		Object answer = JOptionPane.showInputDialog(new JFrame(), 
				"Which binary operation should be applied on the two BDDs?", 
				"Question", JOptionPane.QUESTION_MESSAGE, null, options, 
				options[0]);
		// for each option checking whether it was selected
		for (int i = 0; i < 16; i++) {
			if (options[i].equals(answer)) {
				// returning the answer's number
				return i;
			}
		}
		// returning -1, if none of the options was selected 
		// (e.g. if the dialog was cancelled)
		return -1;
	}
	
	
	/**
	 * asks the user how a resulting OBDD should be named
	 * @return the chosen name
	 */
	public static String newObddName() {
		return JOptionPane.showInputDialog(
				"How should the resulting BDD be named?");
	}
	
	
	/**
	 * informs the user that two variable orderings aren't equal
	 */
	public static void unequalVarOrds() {
		errorDialog("The two BDDs' variable orderings aren't equal.\n" + 
				"Therefore the apply operation isn't executed.");
	}
}
