package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.*;
import model.AbstractObddLayout;
import model.VisualObdd;

/**
 * 
 * @author TimNP
 *
 */
public class MainGui extends JFrame {
	/**
	 * the MainGui's controllers
	 */
	private FormulaController fController;
	private GuiController gController;
	private ObddController oController;
	private VarOrdController vController;
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the menu bar
	 */
	private JMenuBar menuBar = new JMenuBar();
	
	/**
	 * text field for the name of the OBDD
	 */
	private JTextField obddNameField = new JTextField();
	/**
	 * text field for the formula to be represented by the OBDD
	 */
	private JTextField formulaField = new JTextField();
	/**
	 * text field for the variable ordering
	 */
	private JTextField varOrdField = new JTextField();
	/**
	 * drop down menu for the type of OBDD to be generated
	 */
	private JComboBox<String> obddTypeCB = new JComboBox<String>(new String[]
					{"Complete OBDD", "QOBDD", "ROBDD"});
//	/**
//	 * drop down menu for the source of the OBDD to be generated from
//	 */
//	private JComboBox<String> obddSourceCB = 
//			new JComboBox<String>(new String[]{"Formula", "Truth Table"});
	/**
	 * button to generate a new OBDD
	 */
	private JButton generateButton = new JButton("Generate");
	
	/**
	 * scroll panel for the truth table
	 */
	private JScrollPane ttScrollPane = new JScrollPane();
	/**
	 * button for clearing the truth table
	 */
	private JButton clearTtButton = new JButton("Clear Truth Table");
	/**
	 * button for showing the truth table in a new window
	 */
	private JButton ttWindowButton = new JButton("Truth Table Window");
	/**
	 * scroll panel for all OBDDs created during this "session"
	 */
	private JScrollPane obddScrollPane = new JScrollPane();
	/**
	 * button for showing a particular OBDD
	 */
	private JButton showObddButton = new JButton("Show chosen OBDD");
	/**
	 * button for applying a binary operation on the current OBDD and the 
	 * chosen one
	 */
	private JButton applyObddsButton = new JButton("Apply with chosen OBDD");
	
	/**
	 * panel for the OBDD itself
	 */
	private JPanel obddPane = new JPanel();
	
	/**
	 * array of the main frame's last column's buttons
	 */
	private JButton[] lastColumnButtons = {
			new JButton("Undo"),
			new JButton("Find Equivalent Nodes"),
			new JButton("Merge Equivalent Nodes"),
			new JButton("Find a Redundant Node"),
			new JButton("Remove Redundant Node"),
			new JButton("Recuce to QOBDD"),
			new JButton("Reduce to ROBDD"),
			new JButton("Get Formula")
	};
	
	/**
	 * insets for components that are directly over another component and 
	 * "connected" to that component
	 */
	private static final Insets over = new Insets(1,1,0,1);
	
	/**
	 * insets for components that are directly under another component and 
	 * "connected" to that component
	 */
	private static final Insets under = new Insets(0,1,1,1);
	
	/**
	 * insets for components that are independent of other components
	 */
	private static final Insets independent = new Insets(1,1,1,1);
	
	
	
//	/**
//	 * getter for the OBDD panel
//	 * @return
//	 */
//	public JPanel getObddPane() {
//		return obddPane;
//	}
	
	
	/**
	 * constructor for the main GUI
	 */
	public MainGui () {
		// initializing the MainGui's controllers
		fController = new FormulaController(this);
		gController = new GuiController(this);
		oController = new ObddController(this);
		vController = new VarOrdController(this);
		// maximizing the frame
		setExtendedState(MAXIMIZED_BOTH);
		// setting the title
		setTitle("VisualOBDD");
		// When the frame is closed, the program is exited.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// setting the layout to GridBagLayout 
		setLayout(new GridBagLayout());
		
		// adding the menu bar
		setJMenuBar(menuBar);
		
		// adding the OBDD name label
		addLabel(new JLabel("OBDD Name"), 0, 0, 1, 1);
		// adding the OBDD name text field
		addTextField(obddNameField, 0, 1, 1, 1);
		// adding the Formula label
		addLabel(new JLabel("Formula"), 1, 0, 4, 1);
		// adding the Formula text field
		addTextField(formulaField, 1, 1, 4, 1);
		// adding the VariableOrdering label
		addLabel(new JLabel("Variable Ordering"), 5, 0, 2, 1);
		// adding the VariableOrdering text field
		addTextField(varOrdField, 5, 1, 2, 1);
		// adding the OBDD type label
		addLabel(new JLabel("OBDD Type"), 7, 0, 1, 1);
		// adding the OBDD type drop down menu
		addSubLineComboBoxOrButton(obddTypeCB, 7, 1);
//		// adding the OBDD source label
//		addLabel(new JLabel("OBDD Source"), 8, 0, 1, 1);
//		// adding the OBDD source drop down menu
//		addSubLineComboBoxOrButton(obddSourceCB, 8, 1);
		// adding the generate button
		addSubLineComboBoxOrButton(generateButton, 9, 1);
		// adding a listener to the generate button
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If the OBDD should be generated from the given Formula, 
				// the formula field's text is retrieved.
				String formulaFieldText = formulaField.getText();
				if (formulaFieldText != null && formulaFieldText != "") {
					// If there is a String given, calling the OBDD 
					// controller to generate an OBDD from that.
					oController.obddFromFormula(formulaFieldText);
				}
				else {
					// TODO pop-up
				}
			}
		});
		
		// adding the truth table scroll panel
		addScrollPane(ttScrollPane, 0, 2, 2, 3);
		
		// adding the truth table clearing button
		addSubPaneButton(clearTtButton, 0, 5);
		
		// adding the truth table window button
		addSubPaneButton(ttWindowButton, 1, 5);
		
		// adding the OBDD scroll panel
		addScrollPane(obddScrollPane, 0, 6, 2, 3);
		
		// adding the OBDD showing button
		addSubPaneButton(showObddButton, 0, 9);
		
		// adding the OBDD applying button
		addSubPaneButton(applyObddsButton, 1, 9);
		
		// adding the OBDD panel
		addToMainFrame(obddPane, 2, 2, 7, 8, GridBagConstraints.BOTH, 1, 1, 
				new Insets(2, 2, 2, 2), GridBagConstraints.NORTH, 
				1.0, 1.0);
		
		// adding the GUI's first column's buttons
		for (int i = 0; i < lastColumnButtons.length; i++) {
			addToMainFrame(lastColumnButtons[i], 9, i + 2, 1, 1, 
					GridBagConstraints.HORIZONTAL, 1, 1, 
					new Insets(1, 1, 1, 1), GridBagConstraints.CENTER, 
					0.2, 0.1);
		}
		
		// making the main frame visible
		setVisible(true);
	}
	
	
	/**
	 * auxiliary method that adds a component to the main frame with given 
	 * GridBagConstraints
	 * @param comp - the component
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param fill
	 * @param ipadx
	 * @param ipady
	 * @param insets
	 * @param anchor
	 */
	private void addToMainFrame(Component comp, int gridx, int gridy, 
			int gridwidth, int gridheight, int fill, int ipadx, int ipady, 
			Insets insets, int anchor, double weightx, double weighty) {
		//initializing the constraints
		GridBagConstraints gbc = new GridBagConstraints();
		// setting the component's position
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		// setting the component's display area size
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		// setting the component's filling
		gbc.fill = fill;
		// setting the component's internal padding
		gbc.ipadx = ipadx;
		gbc.ipady = ipady;
		// setting the component's external padding
		gbc.insets = insets;
		// setting the component's anchor
		gbc.anchor = anchor;
		// setting the component's "weights"
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		// adding the component to the main frame
		getContentPane().add(comp, gbc);
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for labels with
	 * fixed filling (horizontal), internal (2,2) and external (1,1,0,1) 
	 * padding and anchor (page end) and calculated weights
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 */
	private void addLabel(Component comp, int gridx, int gridy, 
			int gridwidth, int gridheight) {
		addToMainFrame(comp, gridx, gridy, gridwidth, gridheight, 
				GridBagConstraints.HORIZONTAL, 2, 2, over, 
				GridBagConstraints.PAGE_END, 
				calculatedWeight(gridwidth), calculatedWeight(gridheight));
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for text fields
	 * with fixed filling (horizontal), internal (2,2) and external (0,1,1,1) 
	 * padding and anchor (page start) and calculated weights
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 */
	private void addTextField(Component comp, int gridx, int gridy, 
			int gridwidth, int gridheight) {
		addToMainFrame(comp, gridx, gridy, gridwidth, gridheight, 
				GridBagConstraints.HORIZONTAL, 2, 2, under, 
				GridBagConstraints.PAGE_START, 
				calculatedWeight(gridwidth), calculatedWeight(gridheight));
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for combo boxes
	 * and buttons under another line with fixed "gridsize" (1,1) filling 
	 * (horizontal), internal (1,1) and external (0,1,1,1) padding, anchor (page start) and weights (0.1,0.1)
	 * @param comp
	 * @param gridx
	 * @param gridy
	 */
	private void addSubLineComboBoxOrButton
	(Component comp, int gridx, int gridy) {
		addToMainFrame(comp, gridx, gridy, 1, 1, GridBagConstraints.HORIZONTAL,
				1, 1, under, GridBagConstraints.PAGE_START, 0.1, 0.1);
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for buttons 
	 * under a panel with fixed "gridsize" (1,1) filling (horizontal), 
	 * internal (1,1) and external padding (1,1,1,1), anchor (page start) and 
	 * weights (0.1,0.1) 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 */
	private void addSubPaneButton(Component comp, int gridx, int gridy) {
		addToMainFrame(comp, gridx, gridy, 1, 1, GridBagConstraints.HORIZONTAL,
				1, 1, independent, GridBagConstraints.PAGE_START, 0.1, 0.1);
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for 
	 * scroll panels with fixed filling (both), internal (1,1) and 
	 * external (1,1,1,1) padding and anchor (center) and calculated weights
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param insets
	 */
	private void addScrollPane(Component comp, int gridx, int gridy, 
			int gridwidth, int gridheight) {
		addToMainFrame(comp, gridx, gridy, gridwidth, gridheight, 
				GridBagConstraints.BOTH, 1, 1, independent, 
				GridBagConstraints.CENTER, 
				calculatedWeight(gridwidth), calculatedWeight(gridheight));
	}
	
	
	/**
	 * auxiliary method that returns a weight for a "gridsize" 
	 * (gridwidth or gridheight)
	 * @param gridsize
	 * @return
	 */
	private Double calculatedWeight(int gridsize) {
		switch(gridsize) {
		case 1:
			return 0.1;
		case 2:
			return 0.2;
		case 3:
			return 0.4;
		default:
			// In the default case, 
			// the "gridsize" is considered to be greater than 3.
			return 1.0;
		}
	}
	
	
	/**
	 * shows an OBDD given by an AbstractObddLayout
	 * @param layout
	 */
	public void showObdd(AbstractObddLayout layout) {
		obddPane.removeAll();
		obddPane.add(new VisualObdd(layout, obddPane.getPreferredSize()));
		obddPane.repaint();
	}
}
