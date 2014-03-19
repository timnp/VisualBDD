package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import controller.*;

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
	 * the main GUI's GUI controller
	 */
	private GuiController guiController;
	
	/**
	 * the MainGui's OBDD controller
	 */
	private ObddController obddController;
	
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
	 * list model for the OBDD list
	 */
	private DefaultListModel<String> obddListModel = new DefaultListModel<String>();
	/**
	 * list of all OBDDs created during this "session"
	 */
	private JList<String> obddList = new JList<String>(obddListModel);
	/**
	 * scroll panel for the OBDD list
	 */
	private JScrollPane obddScrollPane = new JScrollPane(obddList);
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
					// identified by the given name
					obddController.undo(obddNameField.getText(), 
							obddPane.getSize());
				}
			},
			// the find equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's find equivalent method
					obddController.
							findEquivalentNodes(varOrdField.getText());
				}	
			},
			// the merge equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's merge equivalent method 
					// for the OBDD identified by the given name
					obddController.mergeEquivalent(obddNameField.getText(), 
									obddPane.getSize());
				}
			},
			// the find redundant node button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's find redundant method
					obddController.findRedundant();
				}
			},
			// the remove redundant node button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's remove redundant method 
					// for the OBDD identified by the given name
					obddController.removeRedundant(obddNameField.getText(), 
							obddPane.getSize());
				}
			},
			// the reduce to QOBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method for the OBDD
					// identified by the given name, stating that a QOBDD 
					// should be created by the parameter "false"
					obddController.reduce(obddNameField.getText(), 
							obddPane.getSize(), false);
				}
			},
			// the reduce to ROBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method for the OBDD
					// identified by the given name, stating that an ROBDD 
					// should be created by the parameter "true"
					obddController.reduce(obddNameField.getText(), 
							obddPane.getSize(), true);
				}
			},
			// the get formula button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's represented formula method
					// to show both it and the initial formula given by the 
					// formula field's text
					obddController.representedFormula(formulaField.getText());
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
	
	
	
	/**
	 * getter for the OBDD name field
	 * @return
	 */
	public JTextField getObddNameField() {return obddNameField;}
	
	/**
	 * getter for the variable ordering field
	 * @return
	 */
	public JTextField getVarOrdField() {return varOrdField;}
	
	/**
	 * getter for the formula field
	 * @return
	 */
	public JTextField getFormulaField() {return formulaField;}
	
//	/**
//	 * getter for the OBDD type menu
//	 * @return
//	 */
//	public JComboBox<String> getObddTypeCB() {return obddTypeCB;}
	
//	/**
//	 * getter for the generate button
//	 * @return
//	 */
//	public JButton getGenerateButton() {return generateButton;}
	
	/**
	 * getter for the OBDD list model
	 * @return
	 */
	public DefaultListModel<String> getObddListModel() {return obddListModel;}
	
//	/**
//	 * getter for the OBDD list
//	 * @return
//	 */
//	public JList<String> getObddList() {return obddList;}
	
//	/**
//	 * getter for the OBDD scroll panel
//	 * @return
//	 */
//	public JScrollPane getObddScrollPane() {return obddScrollPane;}
	
//	/**
//	 * getter for the OBDD showing button
//	 * @return
//	 */
//	public JButton getShowObddButton() {return showObddButton;}
	
//	/**
//	 * getter for the OBDD apply button
//	 * @return
//	 */
//	public JButton getApplyObddsButton() {return applyObddsButton;}
	
	/**
	 * getter for the truth table
	 * @return
	 */
	public JTable getTtTable() {return ttTable;}
	
//	/**
//	 * getter for the truth table scroll panel
//	 * @return
//	 */
//	public JScrollPane getTtScrollPane() {return ttScrollPane;}
	
//	/**
//	 * getter for the line combining/separating button
//	 * @return
//	 */
//	public JButton getCombineSeparateButton() {return combineSeparateButton;}
	
//	/**
//	 * getter for the truth table window button
//	 * @return
//	 */
//	public JButton getTtWindowButton() {return ttWindowButton;}
	
	/**
	 * getter for the OBDD panel
	 * @return
	 */
	public JPanel getObddPane() {return obddPane;}
	
//	/**
//	 * getter for the GUI's last column's buttons
//	 * @return
//	 */
//	public JButton[] getLastColumnButtons() {return lastColumnButtons;}
	
	
	/**
	 * constructor for the main GUI
	 */
	public MainGui () {
		// initializing the main GUI's GUI controller
		guiController = new GuiController(this);
		// initializing the MainGui's OBDD controller
		obddController = new ObddController(guiController);
		// setting the title
		setTitle("VisualOBDD");
		// When the frame is closed, the program is exited.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// adding the GUI's components to its visualization
		addComponents();
		// setting the main frame's preferred size
		setPreferredSize(new Dimension(1000,500));
		this.setSize(getPreferredSize());
		// adding all required listeners
		addListeners();
		// making the main frame visible
		setVisible(true);
	}
	
	
	/**
	 * auxiliary method that adds all of the GUI's components to its 
	 * visualization
	 */
	private void addComponents() {
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

		// adding the OBDD scroll panel
		addScrollPane(obddScrollPane, 0, 2, 2, 3);
		// setting its preferred size
		obddScrollPane.setPreferredSize(new Dimension(350,200));
//		// adding the OBDD list
//		addScrollable(obddList, 0, 2, 2, 3);
//		// setting its preferred size
//		obddList.setPreferredSize(new Dimension(350,200));
		// adding the OBDD showing button
		addSubPaneButton(showObddButton, 0, 5);
		// setting its preferred size
		showObddButton.setPreferredSize(preferredButtonSize);
		// adding the OBDD applying button
		addSubPaneButton(applyObddsButton, 1, 5);
		// setting its preferred size
		applyObddsButton.setPreferredSize(preferredButtonSize);
		// adding the truth table scroll panel
		addScrollPane(ttScrollPane, 0, 6, 2, 3);
		// setting its preferred size
		ttScrollPane.setPreferredSize(new Dimension(350,200));
		// adding the line combining/separating button
		addSubPaneButton(combineSeparateButton, 0, 9);
		// setting its preferred size
		combineSeparateButton.setPreferredSize(preferredButtonSize);
		// adding the truth table window button
		addSubPaneButton(ttWindowButton, 1, 9);
		// setting its preferred size
		ttWindowButton.setPreferredSize(preferredButtonSize);
		
		// adding the OBDD panel
		addToMainFrame(obddPane, 2, 2, 6, 8, GridBagConstraints.BOTH, 1, 1, 
				new Insets(2, 2, 2, 2), GridBagConstraints.NORTH, 
				1.0, 1.0);
		// setting its preferred size
		obddPane.setPreferredSize(new Dimension(450,450));
		// setting the OBDD panel's color
		obddPane.setBackground(Color.WHITE);
		
		// adding the GUI's last column's buttons, setting their preferred 
		// size(s)
		for (int i = 0; i < lastColumnButtons.length; i++) {
			// setting the preferred size
			lastColumnButtons[i].setPreferredSize(preferredButtonSize);
			// adding the the button
			addToMainFrame(lastColumnButtons[i], 8, i + 2, 1, 1, 
					GridBagConstraints.HORIZONTAL, 1, 1, 
					new Insets(1, 1, 1, 1), GridBagConstraints.CENTER, 
					0.2, 0.1);
		}
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
	 * auxiliary method that adds listeners to all of the GUI's components 
	 * (which need any)
	 */
	private void addListeners() {
		// adding a listener to the generate button
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's OBDD from formula method
				obddController.obddFromFormula(obddNameField.getText(), 
								varOrdField.getText(), formulaField.getText(), 
								obddTypeCB.getSelectedIndex(), 
								obddPane.getSize());
//				// updating the possibly OBDD name, variable ordering string 
//				// and formula (input) string
//				updateTextFields(results.getSecond());
//				// showing the visual OBDD
//				if (showObdd(results.getFirst())) {
//					// adding the OBDD's name to the OBDD list if the OBDD is 
//					// shown
//					obddListModel.addElement(results.getSecond()[0]);
//				}
			}
		});
		// adding a listener to the OBDD showing button
		showObddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's load method for the OBDD name 
				// selected in the OBDD list
				obddController.loadObdd(obddList.getSelectedValue(), 
								obddPane.getSize());
			}
		});
		// adding a listener to the OBDD applying button
		applyObddsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// calling the OBDD controller's simple apply method
				obddController.simpleApply(obddNameField.getText(), 
								obddList.getSelectedValue(), 
								varOrdField.getText(), obddPane.getSize());
			}
		});
		// adding a listener to the OBDD panel
		obddPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// calling the OBDD controller to unselect the selected node 
				// and select the clicked node (if there is one)
				obddController.clickOnObddPanel(e.getPoint());
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				obddController.pressOnObddPanel(e.getPoint());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				obddController.releaseOnObddPanel(e.getPoint());
			}
		});
		// adding the GUI's last column's buttons' listeners
		for (int i = 0; i < lastColumnButtons.length; i++) {
			lastColumnButtons[i].addActionListener(
					lastColumnButtonListeners[i]);
		}
	}
	
	
//	/**
//	 * auxiliary method that shows an OBDD given as a VisualObdd if it isn't 
//	 * null and states whether it did
//	 * @param visualObdd
//	 * @return
//	 */
//	private boolean showObdd(VisualObdd visualObdd) {
//		// showing it if it isn't null
//		if (visualObdd != null) {
//			obddPane.removeAll();
//			obddPane.add(visualObdd);
//			obddPane.repaint();
//			return true;
//		}
//		else return false;
//	}
//	
//	
//	/**
//	 * auxiliary method that updates the field texts to the given ones
//	 * @param textFieldStrings - an array of the new OBDD name field string, 
//	 * 							 the new variable ordering field string and 
//	 * 							 the new formula field string
//	 */
//	private void updateTextFields(String[] textFieldStrings) {
//		obddNameField.setText(textFieldStrings[0]);
//		varOrdField.setText(textFieldStrings[1]);
//		formulaField.setText(textFieldStrings[2]);
//	}
}
