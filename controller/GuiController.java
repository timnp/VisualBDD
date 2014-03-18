package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Pair;
import model.VisualObdd;
import view.MainGui;

/**
 * 
 * @author TimNP
 *
 */
public class GuiController {
	private MainGui mainGui;
	/**
	 * constructor for a GUI controller
	 */
	public GuiController(MainGui mainGui) {
		//TODO?
		this.mainGui = mainGui;
	}
	
	
	/**
	 * informing the user that the given formula (input string) doesn't fulfill
	 * the requirements and asks for another one
	 * @param givenFormulaString
	 * @return the new formula (input string)
	 */
	public String improperFormulaInputString(String givenFormulaInputString) {
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
	public String improperName(String givenName) {
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
	public String improperVarOrdString(String givenVarOrdString) {
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
	private String improperInput(String message, String givenInput) {
		return (String) JOptionPane.showInputDialog(new JFrame(), 
				message, "Improper Input", JOptionPane.INFORMATION_MESSAGE, null, 
				null, givenInput);
	}
	
	
	/**
	 * informs the user that there aren't any previous versions of the current 
	 * OBDD
	 */
	public void nothingToUndo() {
		messageDialog("There aren't any previous versions of this BDD.");
	}
	
	
	/**
	 * informs the user that there aren't any equivalent nodes in the current 
	 * OBDD
	 */
	public void noEquivalentNodes() {
		messageDialog("No equivalent nodes were found.");
	}
	
	
	/**
	 * informs the user that there aren't any redundant nodes in the current 
	 * OBDD
	 */
	public void noRedundantNode() {
		messageDialog("This BDD doesn't have any redundant nodes.");
	}
	
	
	/**
	 * auxiliary method that shows a message dialog with a given message
	 * @param message
	 */
	private void messageDialog(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message);
	}
	
	
	/**
	 * informs the user that not all selected/highlighted nodes are equivalent
	 */
	public void notAllEquivalent() {
		errorDialog(
				"Some of the selected/highlighted nodes aren't equivalent.");
	}
	
	
	/**
	 * informs the user that the selected/highlighted node isn't redundant
	 */
	public void notRedundant() {
		errorDialog("The selected/highlighted node isn't redundant.");
	}
	
	
	/**
	 * informs the user that too few nodes are selected/highlighted
	 */
	public void notEnoughNodesSelected() {
		errorDialog("There aren't enough selected/highlighted nodes.");
	}
	
	
	/**
	 * informs the user that no OBDD was selected
	 */
	public void noObddSelected() {
		errorDialog("You didn't select a BDD.");
	}
	
	
	/**
	 * informs the user that the selected OBDD couldn't be found
	 */
	public void noSuchObdd() {
		errorDialog("The selected BDD couldn't be found.");
	}
	
	
	/**
	 * auxiliary method that shows an error (message) dialog with a given 
	 * method
	 * @param message
	 */
	private void errorDialog(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * informs the user that some of the OBDD's paths are missing variables and
	 * asks whether the nodes should be added
	 * @return "true" if the answer is "yes", "false" otherwise
	 */
	public boolean missingVars() {
		return yesOrNoDialog("Some of the BDD's paths are missing variables.\n"
				+ "Should the nodes be added to produce a QOBDD?", "Warning", 
				JOptionPane.WARNING_MESSAGE);
	}
	
	
	/**
	 * asks the user whether the formula should be reduced
	 * @return "true" if the answer is "yes", "false" otherwise
	 */
	public boolean reduceFormula() {
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
	private boolean yesOrNoDialog(String question, String dialogName, 
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
	public void showFormulas(String initialFormula, 
			String representedFormula) {
		messageDialog("Initial Formula: " + initialFormula + 
				"\nRepresented Formula: " + representedFormula);
	}
	
	
	/**
	 * informs the user that the chosen name for a OBDD has already been taken 
	 * and how it got named instead
	 * @param newName
	 */
	public void nameAlreadyTaken(String newName) {
		messageDialog("There already is a BDD with the chosen name.\n" 
				+ "The new BDD has been named '" + newName + "' instead.");
	}
	
	
	/**
	 * informs the user that no name for the OBDD has been provided and how it 
	 * got named
	 * @param name
	 */
	public void noNameGiven(String name) {
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
	public int applyOperation(String firstName, String secondName) {
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
	public String newObddName() {
		return JOptionPane.showInputDialog(
				"How should the resulting BDD be named?");
	}
	
	
	/**
	 * informs the user that two variable orderings aren't equal
	 */
	public void unequalVarOrds() {
		errorDialog("The two BDDs' variable orderings aren't equal.\n" + 
				"Therefore the apply operation isn't executed.");
	}
	
	
	/**
	 * shows an OBDD given as a VisualObdd if it isn't null
	 * @param visualObdd
	 */	
	public void showObdd(VisualObdd visualObdd) {
			// showing it if it isn't null
			if (visualObdd != null) {
				mainGui.getObddPane().removeAll();
				mainGui.getObddPane().add(visualObdd);
				mainGui.getObddPane().repaint();
			}
	}
	
	
	/**
	 * updates the field texts to the given ones
	 * @param obddName
	 * @param varOrdFieldText
	 * @param formulaFieldText
	 */
	public void updateTextFields(String obddName, String varOrdFieldText, 
			String formulaFieldText) {
		mainGui.getObddNameField().setText(obddName);
		mainGui.getVarOrdField().setText(varOrdFieldText);
		mainGui.getFormulaField().setText(formulaFieldText);
	}
	
	
	/**
	 * 
	 * @param obddName
	 */
	public void addToObddList(String obddName) {
		mainGui.getObddListModel().addElement(obddName);
	}
}
