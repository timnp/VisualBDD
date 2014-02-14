package view;

import java.awt.*;

import javax.swing.*;

public class MainGui extends JFrame {
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
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
	/**
	 * drop down menu for the source of the OBDD to be generated from
	 */
	private JComboBox<String> obddSourceCB = 
			new JComboBox<String>(new String[]{"Formula", "Truth Table"});
	/**
	 * button to generate a new OBDD
	 */
	private JButton generateButton = new JButton("Generate");
	
	/**
	 * array of the main frame's first column's buttons
	 */
	private JButton[] firstColumnButtons = {
			new JButton("Undo"),
			new JButton("Find two Equivalent Nodes"),
			new JButton("Merge Equivalent Nodes"),
			new JButton("Find a Redundant Node"),
			new JButton("Remove Redundant Node"),
			new JButton("To QOBDD"),
			new JButton("To ROBDD"),
			new JButton("Get Formula")
	};
	
//	/**
//	 * undo button
//	 */
//	private JButton undoButton;
//	/**
//	 * button for finding two equivalent OBDD nodes
//	 */
//	private JButton findEquivButton;
//	/**
//	 * button for merging two equivalent OBDD nodes
//	 */
//	private JButton mergeEquivButton;
//	/**
//	 * button for finding a redundant OBDD node
//	 */
//	private JButton findRedButton;
//	/**
//	 * button for removing a redundant node
//	 */
//	private JButton removeRedButton;
//	/**
//	 * button for converting the current OBDD into a QOBDD
//	 */
//	private JButton toQobddButton;
//	/**
//	 * button for converting the current OBDD into an ROBDD
//	 */
//	private JButton toRobddButton;
//	/**
//	 * button for retrieving the Formula represented by the OBDD
//	 */
//	private JButton formulaButton;
//	/**
//	 * button for retrieving the OBDD's entire truth table
//	 */
//	private JButton entireTTButton;
//	/**
//	 * button for retrieving the OBDD's limited truth table
//	 */
//	private JButton limitedTTButton;
//	/**
//	 * button for exporting the OBDD (as a picture)
//	 */
//	private JButton exportButton;
	
	/**
	 * scroll panel for the truth table
	 */
	private JScrollPane ttScrollPane;
	/**
	 * button for showing the truth table in a new window
	 */
	private JButton ttButton;
	/**
	 * scroll panel for all OBDDs created during this "session"
	 */
	private JScrollPane obddScrollPane;
	/**
	 * button for showing a particular OBDD
	 */
	private JButton showButton;
	/**
	 * button for deleting a particular OBDD
	 */
	private JButton deleteButton;
	
	/**
	 * panel for the OBDD itself
	 */
	private JPanel obddPane = new JPanel();
	
	
	
	/**
	 * constructor for the main GUI
	 */
	public MainGui () {
		// maximizing the frame
		setExtendedState(MAXIMIZED_BOTH);
		// setting the title
		setTitle("VisualOBDD");
		// When the frame is closed, the program is exited.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// setting the layout to GridBagLayout 
		setLayout(new GridBagLayout());
		
		// retrieving the GUI's horizontal and vertical numbers of grids
		int horizontalGrids = 9;
		int verticalGrids = firstColumnButtons.length + 2;
		// calculating the GUI's horizontal and vertical weights of a single 
		// grid
		double horizontalGridWeight = 1 / horizontalGrids;
		double verticalGridWeight = 1 / verticalGrids;
		
		// adding the OBDD name label
		addToMainFrame(new JLabel("OBDD Name"), 0, 0, 1, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(1, 1, 0, 1), 
				GridBagConstraints.PAGE_END, horizontalGridWeight, 
				verticalGridWeight);
		// setting the OBDD name text field
		addToMainFrame(obddNameField, 0, 1, 1, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(0, 1, 1, 1), 
				GridBagConstraints.CENTER, horizontalGridWeight, 
				verticalGridWeight);
		// adding the Formula label
		addToMainFrame(new JLabel("Formula"), 1, 0, 3, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(1, 1, 0, 1), 
				GridBagConstraints.PAGE_END, 3 * horizontalGridWeight, 
				verticalGridWeight);
		// setting the Formula text field
		addToMainFrame(formulaField, 1, 1, 3, 1, GridBagConstraints.HORIZONTAL,
				2, 2, new Insets(0, 1, 1, 1), GridBagConstraints.CENTER, 
				3 * horizontalGridWeight, verticalGridWeight);
		// adding the VariableOrdering label
		addToMainFrame(new JLabel("Variable Ordering"), 4, 0, 2, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(1, 1, 0, 1), 
				GridBagConstraints.PAGE_END, 2 * horizontalGridWeight, 
				verticalGridWeight);
		// setting the VariableOrdering text field
		addToMainFrame(varOrdField, 4, 1, 2, 1, GridBagConstraints.HORIZONTAL, 
				2, 2, new Insets(0, 1, 1, 1), GridBagConstraints.CENTER, 
				2 * horizontalGridWeight, verticalGridWeight);
		// adding the OBDD type label
		addToMainFrame(new JLabel("OBDD Type"), 6, 0, 1, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(1, 1, 0, 1), 
				GridBagConstraints.PAGE_END, horizontalGridWeight, 
				verticalGridWeight);
		// setting the OBDD type drop down menu
		addToMainFrame(obddTypeCB, 6, 1, 1, 1, GridBagConstraints.HORIZONTAL, 
				2, 2, new Insets(0, 1, 1, 1), GridBagConstraints.PAGE_START, 
				horizontalGridWeight, verticalGridWeight);
		// adding the OBDD source label
		addToMainFrame(new JLabel("OBDD Source"), 7, 0, 1, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(1, 1, 0, 1), 
				GridBagConstraints.PAGE_END, horizontalGridWeight, 
				verticalGridWeight);
		// setting the OBDD source drop down menu
		addToMainFrame(obddSourceCB, 7, 1, 1, 1, GridBagConstraints.HORIZONTAL,
				2, 2, new Insets(0, 1, 1, 1), GridBagConstraints.PAGE_START, 
				horizontalGridWeight, verticalGridWeight);
		// setting the generate button
		addToMainFrame(generateButton, 8, 1, 1, 1, 
				GridBagConstraints.HORIZONTAL, 2, 2, new Insets(0, 1, 1, 1), 
				GridBagConstraints.PAGE_START, horizontalGridWeight, 
				verticalGridWeight);
		
		// setting the GUI's first column's buttons
		for (int i = 0; i < firstColumnButtons.length; i++) {
			addToMainFrame(firstColumnButtons[i], 0, i + 2, 1, 1, 
					GridBagConstraints.HORIZONTAL, 2, 2, 
					new Insets(1, 1, 1, 1), GridBagConstraints.CENTER, 
					horizontalGridWeight, verticalGridWeight);
		}
		
		// initializing the OBDD panel
		addToMainFrame(obddPane, 1, 1, 6, 8, GridBagConstraints.BOTH, 2, 2, 
				new Insets(2, 2, 2, 2), GridBagConstraints.NORTH, 
				6 * horizontalGridWeight, 8 * verticalGridWeight);
		
//		// initializing the OBDD panel
//		getContentPane().add(obddPane, BorderLayout.CENTER);
		
//		// the main frame's background color
//		Color background = getBackground();
//		// a panel for the top of window
//		JPanel topPane = new JPanel();
//		topPane.setLayout(new FlowLayout());
//		// creating the OBDD type menu
//		String[] obddTypes = {"Complete OBDD", "QOBDD", "ROBDD"};
//		obddTypeCB = new JComboBox<String>(obddTypes);
//		// creating the OBDD source menu
//		String[] obddSources = {"Formula", "Truth Table"};
//		obddSourceCB = new JComboBox<String>(obddSources);
//		// creating the generate button
//		generateButton = new JButton("Generate");
//		// calculating the remaining width for the three text fields
//		int remainingWidth = screenSize.width - (obddTypeCB.getWidth() + 
//				obddSourceCB.getWidth() + generateButton.getWidth());
//		// dividing the remaining width among the text fields
//		int obddNameFieldWidth = (int) (remainingWidth / 100);
//		int formulaFieldWidth = (int) (remainingWidth / 34);
//		int varOrdFieldWidth = (int) (remainingWidth / 50);
//		// creating the OBDD name panel
//		JPanel obddNamePane = new JPanel();
//		obddNamePane.setLayout(new BoxLayout(obddNamePane, BoxLayout.Y_AXIS));
//		// creating the OBDD name text panel
//		JTextPane obddNameText = new JTextPane();
//		obddNameText.setText("OBDD Name");
//		obddNameText.setEditable(false);
//		obddNameText.setBackground(background);
//		obddNamePane.add(obddNameText);
//		// setting the OBDD name field
//		obddNameField = new JTextField(obddNameFieldWidth);
////		TextPrompt bddNameFieldPrompt = 
////				new TextPrompt("Name for the BDD", bddNameField);
////		bddNameFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
////		bddNameField.setMinimumSize(bddNameField.getPreferredSize());
//		obddNamePane.add(obddNameField);
//		topPane.add(obddNamePane);
//		// creating the Formula panel
//		JPanel formulaPane = new JPanel();
//		formulaPane.setLayout(new BoxLayout(formulaPane, BoxLayout.Y_AXIS));
//		// creating the Formula text panel
//		JTextPane formulaText = new JTextPane();
//		formulaText.setText("Formula to be represented");
//		formulaText.setEditable(false);
//		formulaText.setBackground(background);
//		formulaPane.add(formulaText);
//		// setting the Formula field
//		formulaField = new JTextField(formulaFieldWidth);
////		TextPrompt formulaFieldPrompt = 
////				new TextPrompt("Formula to be represented", formulaField);
////		formulaFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
////		formulaField.setMinimumSize(formulaField.getPreferredSize());
//		formulaPane.add(formulaField);
//		topPane.add(formulaPane);
//		// creating the variable ordering panel
//		JPanel varOrdPane = new JPanel();
//		varOrdPane.setLayout(new BoxLayout(varOrdPane, BoxLayout.Y_AXIS));
//		// creating the variable ordering text panel
//		JTextPane varOrdText = new JTextPane();
//		varOrdText.setText("Variable Ordering");
//		varOrdText.setEditable(false);
//		varOrdText.setBackground(background);
//		varOrdPane.add(varOrdText);
//		// setting the variable ordering field
//		varOrdField = new JTextField(varOrdFieldWidth);
////		TextPrompt varOrdFieldPrompt = 
////				new TextPrompt("Variable Ordering", varOrdField);
////		varOrdFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
////		varOrdField.setMinimumSize(varOrdField.getPreferredSize());
//		varOrdPane.add(varOrdField);
//		topPane.add(varOrdPane);
//		// creating the OBDD type panel
//		JPanel obddTypePane = new JPanel();
//		obddTypePane.setLayout(new BoxLayout(obddTypePane, BoxLayout.Y_AXIS));
//		// creating the OBDD type text panel
//		JTextPane obddTypeText = new JTextPane();
//		obddTypeText.setText("OBDD Type");
//		obddTypeText.setEditable(false);
//		obddTypeText.setBackground(background);
//		obddTypePane.add(obddTypeText);
//		// adding the OBDD type menu
//		obddTypePane.add(obddTypeCB);
//		topPane.add(obddTypePane);
//		// creating the OBDD source panel
//		JPanel obddSourcePane = new JPanel();
//		obddSourcePane.setLayout
//				(new BoxLayout(obddSourcePane, BoxLayout.Y_AXIS));
//		// creating the OBDD source text panel
//		JTextPane obddSourceText = new JTextPane();
//		obddSourceText.setText("OBDD source");
//		obddSourceText.setEditable(false);
//		obddSourceText.setBackground(background);
//		obddSourcePane.add(obddSourceText);
//		// adding the OBDD source menu
//		obddSourcePane.add(obddSourceCB);
//		topPane.add(obddSourcePane);
//		// creating the generate panel
//		JPanel generatePane = new JPanel();
//		generatePane.setLayout(new BoxLayout(generatePane, BoxLayout.Y_AXIS));
//		// creating the empty generate text panel
//		JTextPane generateText = new JTextPane();
//		generateText.setEditable(false);
//		generateText.setBackground(background);
//		generatePane.add(generateText);
//		// adding the generate button
//		generatePane.add(generateButton);
//		topPane.add(generatePane);
//		// adding the top panel to the main frame
//		getContentPane().add(topPane, BorderLayout.PAGE_START);
//		
//		// a panel for the left side of the window
//		JPanel leftPane = new JPanel();
//		leftPane.setLayout(new GridLayout(0, 1));
//		// setting the undo button
//		undoButton = new JButton("Undo");
//		leftPane.add(undoButton);
//		// setting the find equivalent button
//		findEquivButton = new JButton("Find equivalent Nodes");
//		leftPane.add(findEquivButton);
//		// setting the merge equivalent button
//		mergeEquivButton = new JButton("Merge equivalent Nodes");
//		leftPane.add(mergeEquivButton);
//		// setting the find redundant button
//		findRedButton = new JButton("Find redundant Node");
//		leftPane.add(findRedButton);
//		// setting the remove button
//		removeRedButton = new JButton("Remove redundant Node");
//		leftPane.add(removeRedButton);
//		// setting the to QOBDD button
//		toQobddButton = new JButton("to QOBDD");
//		leftPane.add(toQobddButton);
//		// setting the toROBDD button
//		toRobddButton = new JButton("to ROBDD");
//		leftPane.add(toRobddButton);
//		// setting the formula button
//		formulaButton = new JButton("get Formula");
//		leftPane.add(formulaButton);
//		// setting the entire truth table button
//		entireTTButton = new JButton("entire Truth Table");
//		leftPane.add(entireTTButton);
//		// setting the limited truth table button
//		limitedTTButton = new JButton("limited Truth Table");
//		leftPane.add(limitedTTButton);
//		// setting the export button
//		exportButton = new JButton("export OBDD");
//		leftPane.add(exportButton);
//		// adding the left panel to the main frame
//		getContentPane().add(leftPane, BorderLayout.LINE_START);
//		
//		// a panel for the right side of the window
//		JPanel rightPane = new JPanel();
//		rightPane.setLayout(new GridLayout(0,1));
////		rightPane.setLayout(new GridBagLayout());
////		GridBagConstraints rightPaneConstraints = new GridBagConstraints();
//		// setting the truth table scroll panel
//		ttScrollPane = new JScrollPane();
//		rightPane.add(ttScrollPane);
////		rightPaneConstraints.gridwidth = 1;
////		rightPaneConstraints.gridheight = 4;
////		rightPaneConstraints.gridx = 1;
////		rightPaneConstraints.gridy = 1;
////		rightPane.add(ttScrollPane, rightPaneConstraints);
//		// setting the truth table showing button
//		ttButton = new JButton("Truth Table Window");
//		rightPane.add(ttButton);
////		rightPaneConstraints.gridheight = 1;
////		rightPaneConstraints.gridy = 4;
////		rightPane.add(ttButton, rightPaneConstraints);
//		// setting the OBDD scroll panel
//		obddScrollPane = new JScrollPane();
//		rightPane.add(obddScrollPane);
////		rightPaneConstraints.gridheight = 4;
////		rightPaneConstraints.gridy = 5;
////		rightPane.add(bddScrollPane, rightPaneConstraints);
//		// a panel for the show and delete buttons
//		JPanel rightBottomPane = new JPanel();
//		rightBottomPane.setLayout(new FlowLayout());
//		// setting the show button
//		showButton = new JButton("Show");
//		rightBottomPane.add(showButton);
//		// setting the delete button
//		deleteButton = new JButton("Delete");
//		rightBottomPane.add(deleteButton);
//		// adding the right bottom panel to the right panel
//		rightPane.add(rightBottomPane);
////		rightPaneConstraints.gridheight = 1;
////		rightPaneConstraints.gridy = 9;
////		rightPane.add(rightBottomPane, rightPaneConstraints);
//		// adding the right panel to the main frame
//		getContentPane().add(rightPane, BorderLayout.LINE_END);
		
		
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
}
