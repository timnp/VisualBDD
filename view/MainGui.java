package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private JButton showObddButton = new JButton("Show chosen BDD");
	/**
	 * button for applying a binary operation on the current OBDD and the 
	 * chosen one
	 */
	private JButton applyObddsButton = new JButton("Apply with chosen BDD");
	
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
			new JButton("Get Formula"),
			new JButton("Get Truth Table"),
			new JButton("Enable/Disable Drag&Drop"),
			new JButton("Align Nodes")//,
//			new JButton("Export BDD")
	};
	
	/**
	 * the text fields' enter to generate key listener
	 */
	private KeyListener enterToGenerate = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			// calling the OBDD controller's generate shortcut method
			obddController.generateShortcut(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	};
	
	/**
	 * array of the last column buttons' listeners 
	 */
	private ActionListener[] lastColumnButtonListeners = {
			// the undo button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's undo method
					obddController.undo();
				}
			},
			// the find equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's find equivalent method
					obddController.findEquivalentNodes();
				}	
			},
			// the merge equivalent nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's merge equivalent method
					obddController.mergeEquivalent();
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
					obddController.removeRedundant();
				}
			},
			// the reduce to QOBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method, stating 
					// that a QOBDD should be created by the parameter "false"
					obddController.reduce(false);
				}
			},
			// the reduce to ROBDD button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's reduce method, stating 
					// that an ROBDD should be created by the parameter "true"
					obddController.reduce(true);
				}
			},
			// the get formula button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's represented formula method
					obddController.representedFormula();
				}
			},
			// the get truth table button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's show truth table method
					obddController.showTruthTable();
				}
			},
			// the node drag&drop button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's 
					// enable/disable drag&drop method
					obddController.enableDisableDragDrop();
				}
			},
			// the align nodes button listener
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// calling the OBDD controller's align nodes method
					obddController.alignNodes();
				}
			}//,
//			// the export button listener
//			new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// 
//					
//				}
//			}
	};
	
	/**
	 * array of the last column buttons' tool tips
	 */
	private String[] lastColumnButtonToolTips = {
			// the undo button tool tip
			"Undoes the last change to the BDD.",
			// the find equivalent button tool tip
			"<html>If a node is selected, all nodes equivalent to it are "
			+ "highlighted.<br>Otherwise any two equivalent nodes are "
			+ "highlighted.",
			// the merge equivalent button tool tip
			"Merges all selected/highlighted nodes if they are equivalent.",
			// the find redundant button tool tip
			"Highlights a redundant node.",
			// the remove redundant button tool tip
			"Removes the selected/highlighted node if it's redundant.",
			// the reduce to QOBDD button tool tip
			"Reduces the BDD to a QOBDD.",
			// the reduce to ROBDD button tool tip
			"Reduces the BDD to an ROBDD.",
			// the get formula button tool tip
			"Provides the formula represented by the BDD.",
			// the get truth table button listener
			"Provides the BDD's truth table.",
			// the node drag&drop button tool tip
			"Drag&drop for the BDD's nodes is currently disabled.",
			// the align nodes button listener
			"Aligns the BDD's nodes to the application's standard."//,
//			// the export button listener
//			"Exports the BDD into a file of a chosen format."
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
	
	/**
	 * getter for the selected OBDD type
	 * @return
	 */
	public int getSelectedObddType() {return obddTypeCB.getSelectedIndex();}
	
	/**
	 * getter for the OBDD list model
	 * @return
	 */
	public DefaultListModel<String> getObddListModel() {return obddListModel;}
	
	/**
	 * getter for the OBDD list's selected value
	 * @return
	 */
	public String getSelectedObdd() {return obddList.getSelectedValue();}
	
	/**
	 * getter for the OBDD panel
	 * @return
	 */
	public JPanel getObddPane() {return obddPane;}
	
	/**
	 * getter for the node drag&drop button
	 * @return
	 */
	public JButton getDragDropButton() {return lastColumnButtons[9];}
	
	
	/**
	 * constructor for the main GUI
	 */
	public MainGui () {
		// initializing the main GUI's GUI controller
		guiController = new GuiController(this);
		// initializing the MainGui's OBDD controller
		obddController = new ObddController(this, guiController);
		// setting the title
		setTitle("VisualOBDD");
		// When the frame is closed, the program is exited.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// adding the GUI's components to its visualization
		addComponents();
		// setting the main frame's preferred size
		setPreferredSize(new Dimension(1000,560));
		setSize(getPreferredSize());
		// adding all required listeners
		addListeners();
		// adding all tool tips and text prompts
		addToolTipsAndPrompts();
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
		
		// adding the OBDD name label
		addTopLabel(new JLabel("BDD Name"), 0, 0, 1, 1);
		// adding the OBDD name text field
		addTextField(obddNameField, 0, 1, 1, 1);
		// setting its preferred size
		obddNameField.setPreferredSize(preferredButtonSize);
		// adding the VariableOrdering label
		addTopLabel(new JLabel("Variable Ordering"), 1, 0, 1, 1);
		// adding the VariableOrdering text field
		addTextField(varOrdField, 1, 1, 1, 1);
		// setting its preferred size
		varOrdField.setPreferredSize(preferredButtonSize);
		// adding the Formula label
		addTopLabel(new JLabel("Formula"), 2, 0, 4, 1);
		// adding the Formula text field
		addTextField(formulaField, 2, 1, 4, 1);
		// setting its preferred size
		formulaField.setPreferredSize(new Dimension(350,25));
		// adding the OBDD type label
		addTopLabel(new JLabel("BDD Type"), 6, 0, 1, 1);
		// adding the OBDD type drop down menu
		addSubLineComboBoxOrButton(obddTypeCB, 6, 1);
		// adding the generate button
		addSubLineComboBoxOrButton(generateButton, 8, 1);
		// setting its preferred size
		generateButton.setPreferredSize(preferredButtonSize);

		// 
		addFreeLabelOrButton(new JLabel("A red node is selected primarily."), 
				0, 2);
		addFreeLabelOrButton(new JLabel("An orange node is selected secondary."
				), 0, 3);
		addFreeLabelOrButton(new JLabel("A yellow node is \"highlighted\"."), 
				0, 4);
		// adding the OBDD scroll panel
		addToMainFrame(obddScrollPane, 0, 5, 1, 6, GridBagConstraints.BOTH, 
				1, 1, independent, GridBagConstraints.CENTER, 
				calculatedWeight(2), calculatedWeight(3));
		// setting its preferred size
		obddScrollPane.setPreferredSize(new Dimension(175,250));
		// adding the OBDD showing button
		addFreeLabelOrButton(showObddButton, 0, 11);
		// setting its preferred size
		showObddButton.setPreferredSize(preferredButtonSize);
		// adding the OBDD applying button
		addFreeLabelOrButton(applyObddsButton, 0, 12);
		// setting its preferred size
		applyObddsButton.setPreferredSize(preferredButtonSize);
		
		// adding the OBDD panel
		addToMainFrame(obddPane, 1, 2, 6, 11, GridBagConstraints.BOTH, 1, 1, 
				new Insets(2, 2, 2, 2), GridBagConstraints.NORTH, 
				1.0, 1.0);
		// setting its preferred size
		obddPane.setPreferredSize(new Dimension(600,475));
		// setting the OBDD panel's color
		obddPane.setBackground(Color.WHITE);
		
		// adding the GUI's last column's buttons, setting their preferred 
		// size(s)
		for (int i = 0; i < lastColumnButtons.length; i++) {
			// setting the preferred size
			lastColumnButtons[i].setPreferredSize(preferredButtonSize);
			// adding the the button
			addFreeLabelOrButton(lastColumnButtons[i], 8, i + 2);
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
	 * auxiliary method that provides the addToMainFrame method for 
	 * "top" labels with fixed filling (horizontal), internal (2,2) and 
	 * external (1,1,0,1) padding and anchor (page end) and calculated weights
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 */
	private void addTopLabel(Component comp, int gridx, int gridy, 
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
	 * (horizontal), internal (1,1) and external (0,1,1,1) padding, anchor 
	 * (page start) and weights (0.1,0.1)
	 * @param comp
	 * @param gridx
	 * @param gridy
	 */
	private void addSubLineComboBoxOrButton(Component comp, int gridx, 
			int gridy) {
		addToMainFrame(comp, gridx, gridy, 1, 1, GridBagConstraints.HORIZONTAL,
				1, 1, under, GridBagConstraints.PAGE_START, 0.1, 0.1);
	}
	
	
	/**
	 * auxiliary method that provides the addToMainFrame method for "free" 
	 * labels and buttons with fixed "gridsize" (1,1) filling (horizontal), 
	 * internal (1,1) and external padding (1,1,1,1), anchor (center) and 
	 * weights (0.1,0.1) 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 */
	private void addFreeLabelOrButton(Component comp, int gridx, int gridy) {
		addToMainFrame(comp, gridx, gridy, 1, 1, GridBagConstraints.HORIZONTAL,
				1, 1, independent, GridBagConstraints.CENTER, 0.1, 0.1);
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
//		// 
//		addKeyListener(new KeyListener() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				// 
//				obddController.generalShortcuts(e);
//			}
//			@Override
//			public void keyReleased(KeyEvent e) {}
//			@Override
//			public void keyTyped(KeyEvent e) {}
//		});
		// adding the text fields' key listener(s)
		obddNameField.addKeyListener(enterToGenerate);
		varOrdField.addKeyListener(enterToGenerate);
		formulaField.addKeyListener(enterToGenerate);
		// adding a listener to the generate button
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's OBDD from formula method
				obddController.obddFromFormula();
			}
		});
		
		// adding a listener to the OBDD showing button
		showObddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's load method for the OBDD name 
				// selected in the OBDD list
				obddController.loadObdd();
			}
		});
		// adding a listener to the OBDD applying button
		applyObddsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// calling the OBDD controller's simple apply method
				obddController.simpleApply();
			}
		});

		// adding a listener to the OBDD panel
		obddPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// calling the OBDD controller's click on OBDD panel method
				obddController.clickOnObddPanel(e.getPoint());
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				// calling the OBDD controller's press on OBDD panel method
				obddController.pressOnObddPanel(e.getPoint());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// calling the OBDD controller's release on OBDD panel method
				obddController.releaseOnObddPanel(e.getPoint());
			}
		});
		
		// adding the GUI's last column's buttons' listeners
		for (int i = 0; i < lastColumnButtons.length; i++) {
			lastColumnButtons[i].addActionListener(
					lastColumnButtonListeners[i]);
		}
	}
	
	
	/**
	 * auxiliary method that adds tool tips and/or text prompts to some of the 
	 * GUI's components
	 */
	private void addToolTipsAndPrompts() {
		new TextPrompt("e.g. \"BDD 1\" or \"x1-and-x2\"", obddNameField);
		varOrdField.setToolTipText("<html>The BDD's variable ordering in "
				+ "descending order<br>Name each variable with an X followed"
				+ " by a positive integer.<br>Separate the variables using "
				+ "commas (,) or the greater than symbol (>).</html>");
		new TextPrompt("e.g. \"x1,x2,x3\" or \"X2>X1\"", varOrdField);
		formulaField.setToolTipText("<html>Use only<br>the contradictory "
				+ "formula (0),<br>the tautological formula (1),<br>"
				+ "variables (an X followed by a positive integer),<br>"
				+ "negation (- as a prefix),<br>conjunction (* as infix), "
				+ "and<br>disjunction (+ as infix) for formulas.</html>");
		new TextPrompt("e.g. \"X1 * X2\" or \"x2 + -(x1*0)\"", formulaField);
		generateButton.setToolTipText("<html>Generates the BDD of the chosen "
				+ "type with the given name<br>from the given variable "
				+ "ordering and formula.</html>");
		obddScrollPane.setToolTipText("All created BDDs");
		showObddButton.setToolTipText
				("Shows the selected BDD in the BDD panel.");
		applyObddsButton.setToolTipText("<html>Asks for a binary operation and"
				+ " applies it<br>on the displayed BDD and the selected one."
				+ "</html>");
		// setting the GUI's last column's buttons' tool tips
		for (int i = 0; i < lastColumnButtons.length; i++) {
			lastColumnButtons[i].setToolTipText(lastColumnButtonToolTips[i]);
		}
	}
}
