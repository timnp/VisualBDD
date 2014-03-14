package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import controller.*;
import model.AbstractObddLayout;
import model.Pair;
import model.VisualObdd;

/**
 * 
 * @author TimNP
 *
 */
public class MainGui extends JFrame {
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the MainGui's controllers
	 */
	private ObddController oController;
	
	/**
	 * the menu bar
	 */
	private JMenuBar menuBar = new JMenuBar();
	
	/**
	 * text field for the name of the OBDD
	 */
	private JTextField obddNameField = new JTextField();
	/**
	 * text field for the variable ordering
	 */
	private JTextField varOrdField = new JTextField();
	/**
	 * text field for the formula to be represented by the OBDD
	 */
	private JTextField formulaField = new JTextField();
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
	 * table displaying the truth table
	 */
	private JTable ttTable = new JTable();
	/**
	 * scroll panel for the truth table
	 */
	private JScrollPane ttScrollPane = new JScrollPane(ttTable);
	/**
	 * button for combining/separating similar lines of the truth table
	 */
	private JButton combineSeparateButton = 
			new JButton("Combine/Separate Lines");
	/**
	 * button for showing the truth table in a new window
	 */
	private JButton ttWindowButton = new JButton("Truth Table Window");
	/**
	 * list model for the OBDD list
	 */
	private DefaultListModel<String> obddListModel = new DefaultListModel<String>();
	/**
	 * list of all OBDDs created during this "session"
	 */
	private JList<String> obddList = new JList<String>(obddListModel);
//	/**
//	 * scroll panel for the OBDD list
//	 */
//	private JScrollPane obddScrollPane = new JScrollPane(obddList);
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
			new JButton("Reduce to QOBDD"),
			new JButton("Reduce to ROBDD"),
			new JButton("Get Formula")
	};
	
	/**
	 * array of the last column buttons' listeners 
	 */
	private ActionListener[] lastColumnButtonListeners = {
			// the undo button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's undo method for the OBDD 
					// identified by the given name and showing the resulting 
					// OBDD
					showObdd(oController.undo(obddNameField.getText(), 
									obddPane.getSize()));
				}
			},
			// the find equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's find equivalent method and
					// showing the resulting OBDD
					showObdd(oController.
							findEquivalentNodes(varOrdField.getText()));
				}	
			},
			// the merge equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's merge equivalent method 
					// for the OBDD identified by the given name and showing 
					// the resulting OBDD
					showObdd(oController.mergeEquivalent
							(obddNameField.getText(), varOrdField.getText(), 
									obddPane.getSize()));
				}
			},
			// the find redundant node button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's find redundant method and 
					// showing the resulting OBDD
					showObdd(oController.findRedundant());
				}
			},
			// the remove redundant node button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's remove redundant method 
					// for the OBDD identified by the given name and showing 
					// the resulting OBDD
					showObdd(oController.removeRedundant
							(obddNameField.getText(), varOrdField.getText(), 
									obddPane.getSize()));
				}
			},
			// the reduce to QOBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method for the OBDD
					// identified by the given name, stating that a QOBDD 
					// should be created by the parameter "false", and showing 
					// the resulting OBDD
					showObdd(oController.reduce(obddNameField.getText(), 
							varOrdField.getText(), obddPane.getSize(), false));
				}
			},
			// the reduce to ROBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method for the OBDD
					// identified by the given name, stating that an ROBDD 
					// should be created by the parameter "true", and showing 
					// the resulting OBDD
					showObdd(oController.reduce(obddNameField.getText(), 
							varOrdField.getText(), obddPane.getSize(), true));
				}
			},
			// the get formula button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's represented formula method
					// to show both it and the initial formula given by the 
					// formula field's text
					oController.representedFormula(formulaField.getText());
				}
			}
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
	
	/**
	 * preferred size for the GUI's buttons
	 */
	private static final Dimension preferredButtonSize = new Dimension(175,25);
	
	
	
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
		// initializing the MainGui's OBDD controller
		oController = new ObddController();
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
		addLabel(new JLabel("BDD Name"), 0, 0, 1, 1);
		// adding the OBDD name text field
		addTextField(obddNameField, 0, 1, 1, 1);
		// setting its preferred size
		obddNameField.setPreferredSize(preferredButtonSize);
		// adding the VariableOrdering label
		addLabel(new JLabel("Variable Ordering"), 1, 0, 1, 1);
		// adding the VariableOrdering text field
		addTextField(varOrdField, 1, 1, 1, 1);
		// setting its preferred size
		varOrdField.setPreferredSize(preferredButtonSize);
		// adding the Formula label
		addLabel(new JLabel("Formula"), 2, 0, 4, 1);
		// adding the Formula text field
		addTextField(formulaField, 2, 1, 4, 1);
		// setting its preferred size
		formulaField.setPreferredSize(new Dimension(350,25));
		// adding the OBDD type label
		addLabel(new JLabel("BDD Type"), 6, 0, 1, 1);
		// adding the OBDD type drop down menu
		addSubLineComboBoxOrButton(obddTypeCB, 6, 1);
//		// adding the OBDD source label
//		addLabel(new JLabel("OBDD Source"), 8, 0, 1, 1);
//		// adding the OBDD source drop down menu
//		addSubLineComboBoxOrButton(obddSourceCB, 8, 1);
		// adding the generate button
		addSubLineComboBoxOrButton(generateButton, 8, 1);
		// setting its preferred size
		generateButton.setPreferredSize(preferredButtonSize);
		// adding a listener to the generate button
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's OBDD from formula method
				Pair<VisualObdd, String[]> results = 
						oController.obddFromFormula(obddNameField.getText(), 
								formulaField.getText(), varOrdField.getText(), 
								obddTypeCB.getSelectedIndex(), 
								obddPane.getSize());
				// updating the possibly OBDD name, variable ordering string 
				// and formula (input) string
				obddNameField.setText(results.getSecond()[0]);
				varOrdField.setText(results.getSecond()[1]);
				formulaField.setText(results.getSecond()[2]);
				// adding the OBDD's name to the OBDD list
				obddListModel.addElement(results.getSecond()[0]);
				// showing the visual OBDD
				showObdd(results.getFirst());
			}
		});

		// adding the truth table scroll panel
		addScrollable(ttScrollPane, 0, 2, 2, 3);
		// setting its preferred size
		ttScrollPane.setPreferredSize(new Dimension(350,175));
		// adding the line combining/separating button
		addSubPaneButton(combineSeparateButton, 0, 5);
		// setting its preferred size
		combineSeparateButton.setPreferredSize(preferredButtonSize);
		// adding the truth table window button
		addSubPaneButton(ttWindowButton, 1, 5);
		// setting its preferred size
		ttWindowButton.setPreferredSize(preferredButtonSize);
//		// adding the OBDD scroll panel
//		addScrollable(obddScrollPane, 0, 6, 2, 3);
//		// setting its preferred size
//		obddScrollPane.setPreferredSize(new Dimension(350,175));
		// adding the OBDD list
		addScrollable(obddList, 0, 6, 2, 3);
		// setting its preferred size
		obddList.setPreferredSize(new Dimension(350,175));
		// adding the OBDD showing button
		addSubPaneButton(showObddButton, 0, 9);
		// setting its preferred size
		showObddButton.setPreferredSize(preferredButtonSize);
		//
		showObddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// retrieving the selected OBDD name
				String obddName = obddList.getSelectedValue();
				// calling the OBDD controller's load method for the OBDD name
				// selected in the OBDD list
				Pair<VisualObdd,Pair<String,String>> results = 
						(oController.loadObdd(obddName, obddPane.getSize()));
				// retrieving the results' pair of text field strings
				Pair<String,String> textFieldStrings = results.getSecond();
				// updating the text fields
				updateTextFields(obddName, textFieldStrings.getFirst(), 
						textFieldStrings.getSecond());
				// showing the visual OBDD
				showObdd(results.getFirst());
			}
		});
		// adding the OBDD applying button
		addSubPaneButton(applyObddsButton, 1, 9);
		// setting its preferred size
		applyObddsButton.setPreferredSize(preferredButtonSize);
		
		// adding the OBDD panel
		addToMainFrame(obddPane, 2, 2, 6, 8, GridBagConstraints.BOTH, 1, 1, 
				new Insets(2, 2, 2, 2), GridBagConstraints.NORTH, 
				1.0, 1.0);
		// setting its preferred size
		obddPane.setPreferredSize(new Dimension(525,400));
		// setting the OBDD panel's color
		obddPane.setBackground(Color.WHITE);
		// adding a listener to the OBDD panel
		obddPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// calling the OBDD controller to unselect the selected node 
				// and select the clicked node (if there is one)
				showObdd(oController.clickOnObddPanel(e.getPoint()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// adding the GUI's last column's buttons, setting their preferred 
		// size(s) and adding their listeners
		for (int i = 0; i < lastColumnButtons.length; i++) {
			// setting the preferred size
			lastColumnButtons[i].setPreferredSize(preferredButtonSize);
			// adding the the button
			addToMainFrame(lastColumnButtons[i], 8, i + 2, 1, 1, 
					GridBagConstraints.HORIZONTAL, 1, 1, 
					new Insets(1, 1, 1, 1), GridBagConstraints.CENTER, 
					0.2, 0.1);
			lastColumnButtons[i].addActionListener(
					lastColumnButtonListeners[i]);
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
	 * scrollable components with fixed filling (both), internal (1,1) and 
	 * external (1,1,1,1) padding and anchor (center) and calculated weights
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param insets
	 */
	private void addScrollable(Component comp, int gridx, int gridy, 
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
	 * auxiliary method that shows an OBDD given as a VisualObdd
	 * @param visualObdd
	 */
	private void showObdd(VisualObdd visualObdd) {
		try {
			// trying to check whether the visual OBDD is visible in order to 
			// check whether it's null
			visualObdd.isVisible();
			// showing it if it isn't null
			obddPane.removeAll();
			obddPane.add(visualObdd);
			obddPane.repaint();
		} catch (NullPointerException e) {}
	}
	
	
	/**
	 * auxiliary method that updates the field texts to the given ones
	 * @param obddName
	 * @param varOrdFieldText
	 * @param formulaFieldText
	 */
	private void updateTextFields(String obddName, String varOrdFieldText, 
			String formulaFieldText) {
		obddNameField.setText(obddName);
		varOrdField.setText(varOrdFieldText);
		formulaField.setText(formulaFieldText);
	}
}
