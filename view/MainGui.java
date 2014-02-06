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
	private JTextField bddNameField;
	/**
	 * text field for the formula to be represented by the OBDD
	 */
	private JTextField formulaField;
	/**
	 * text field for the variable ordering
	 */
	private JTextField varOrdField;
	/**
	 * drop down menu for the type of OBDD to be generated
	 */
	private JComboBox<String> bddTypeCB;
	/**
	 * drop down menu for the source of the OBDD to be generated from
	 */
	private JComboBox<String> bddSourceCB;
	/**
	 * button to generate a new OBDD
	 */
	private JButton generateButton;
	
	/**
	 * undo button
	 */
	private JButton undoButton;
	/**
	 * button for finding two equivalent OBDD nodes
	 */
	private JButton findEquivButton;
	/**
	 * button for merging two equivalent OBDD nodes
	 */
	private JButton mergeEquivButton;
	/**
	 * button for finding a redundant OBDD node
	 */
	private JButton findRedButton;
	/**
	 * button for removing a redundant node
	 */
	private JButton removeRedButton;
	/**
	 * button for converting the current OBDD into a QOBDD
	 */
	private JButton toQOBDDButton;
	/**
	 * button for converting the current OBDD into an ROBDD
	 */
	private JButton toROBDDButton;
	/**
	 * button for retrieving the Formula represented by the OBDD
	 */
	private JButton formulaButton;
	/**
	 * button for retrieving the OBDD's entire truth table
	 */
	private JButton entireTTButton;
	/**
	 * button for retrieving the OBDD's limited truth table
	 */
	private JButton limitedTTButton;
	
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
	private JScrollPane bddScrollPane;
	/**
	 * button for showing a particular OBDD
	 */
	private JButton showButton;
	/**
	 * button for deleting a particular OBDD
	 */
	private JButton deleteButton;
	
	
	
	public MainGui () {
		// retrieving the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// setting the title
		setTitle("VisualBDD");
		// When the frame is closed, the program is exited.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// a panel for the top of window
		JPanel topPane = new JPanel();
		topPane.setLayout(new FlowLayout());
		// creating the OBDD type menu
		String[] bddTypes = {"Complete OBDD", "QOBDD", "ROBDD"};
		bddTypeCB = new JComboBox<String>(bddTypes);
		// creating the OBDD source menu
		String[] bddSources = {"Formula", "Truth Table"};
		bddSourceCB = new JComboBox<String>(bddSources);
		// creating the generate button
		generateButton = new JButton("Generate");
		// calculating the remaining width for the three text fields
		int remainingWidth = screenSize.width - (bddTypeCB.getWidth() + 
				bddSourceCB.getWidth() + generateButton.getWidth());
		// dividing the remaining width among the text fields
		int bddNameFieldWidth = (int) (remainingWidth / 90);
		int formulaFieldWidth = (int) (remainingWidth / 30);
		int varOrdFieldWidth = (int) (remainingWidth / 45);
		// setting the OBDD name field 
		bddNameField = new JTextField(bddNameFieldWidth);
		TextPrompt bddNameFieldPrompt = 
				new TextPrompt("Name for the BDD", bddNameField);
		bddNameFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
		bddNameField.setMinimumSize(bddNameField.getPreferredSize());
		topPane.add(bddNameField);
		// setting the Formula field
		formulaField = new JTextField(formulaFieldWidth);
		TextPrompt formulaFieldPrompt = 
				new TextPrompt("Formula to be represented", formulaField);
		formulaFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
		formulaField.setMinimumSize(formulaField.getPreferredSize());
		topPane.add(formulaField);
		// setting the variable ordering field
		varOrdField = new JTextField(varOrdFieldWidth);
		TextPrompt varOrdFieldPrompt = 
				new TextPrompt("Variable Ordering", varOrdField);
		varOrdFieldPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
		varOrdField.setMinimumSize(varOrdField.getPreferredSize());
		topPane.add(varOrdField);
		// adding the OBDD type menu
		topPane.add(bddTypeCB);
		// adding the OBDD source menu
		topPane.add(bddSourceCB);
		// adding the generate button
		topPane.add(generateButton);
		// adding the top panel to the main frame
		getContentPane().add(topPane, BorderLayout.PAGE_START);
		
		// a panel for the left side of the window
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new GridLayout(0, 1));
		// setting the undo button
		undoButton = new JButton("Undo");
		leftPane.add(undoButton);
		// setting the find equivalent button
		findEquivButton = new JButton("Find equivalent Nodes");
		leftPane.add(findEquivButton);
		// setting the merge equivalent button
		mergeEquivButton = new JButton("Merge equivalent Nodes");
		leftPane.add(mergeEquivButton);
		// setting the find redundant button
		findRedButton = new JButton("Find redundant Node");
		leftPane.add(findRedButton);
		// setting the remove button
		removeRedButton = new JButton("Remove redundant Node");
		leftPane.add(removeRedButton);
		// setting the to QOBDD button
		toQOBDDButton = new JButton("to QOBDD");
		leftPane.add(toQOBDDButton);
		// setting the toROBDD button
		toROBDDButton = new JButton("to ROBDD");
		leftPane.add(toROBDDButton);
		// setting the formula button
		formulaButton = new JButton("get Formula");
		leftPane.add(formulaButton);
		// setting the entire truth table button
		entireTTButton = new JButton("entire Truth Table");
		leftPane.add(entireTTButton);
		// setting the limited truth table button
		limitedTTButton = new JButton("limited Truth Table");
		leftPane.add(limitedTTButton);
		// adding the left panel to the main frame
		getContentPane().add(leftPane, BorderLayout.LINE_START);
		
		// a panel for the right side of the window
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new GridLayout(0,1));
		// setting the truth table scroll panel
		ttScrollPane = new JScrollPane();
		rightPane.add(ttScrollPane);
		// setting the truth table showing button
		ttButton = new JButton("Truth Table Window");
		rightPane.add(ttButton);
		// setting the OBDD scroll panel
		bddScrollPane = new JScrollPane();
		rightPane.add(bddScrollPane);
		// a panel for the show and delete buttons
		JPanel rightBottomPane = new JPanel();
		rightBottomPane.setLayout(new FlowLayout());
		// setting the show button
		showButton = new JButton("Show");
		rightBottomPane.add(showButton);
		// setting the delete button
		deleteButton = new JButton("Delete");
		rightBottomPane.add(deleteButton);
		// adding the right bottom panel to the right panel
		rightPane.add(rightBottomPane);
		// adding the right panel to the main frame
		getContentPane().add(rightPane, BorderLayout.LINE_END);
		
		
//		// setting the main frame's layout
//		mainFrame.getContentPane().setLayout(new GridBagLayout());
//		// GridBagConstraints for the frame
//		GridBagConstraints gbc = new GridBagConstraints();
//		// a text field for the name of the OBDD
//		JTextField bddName = new JTextField();
//		TextPrompt bddNameText = new TextPrompt("Name for the BDD", bddName);
//		bddNameText.setShow(TextPrompt.Show.FOCUS_LOST);
//		bddName.setMinimumSize(bddName.getPreferredSize());
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		mainFrame.getContentPane().add(bddName, gbc);
//		// a text field for the formula to be represented by the OBDD
//		JTextField formula = new JTextField();
//		TextPrompt formulaText = 
//				new TextPrompt("Formula to be represented", formula);
//		formulaText.setShow(TextPrompt.Show.FOCUS_LOST);
//		formula.setMinimumSize(formula.getPreferredSize());
//		gbc.gridx = 1;
//		mainFrame.getContentPane().add(formula, gbc);
//		// a text field for the variable ordering
//		JTextField varOrd = new JTextField();
//		TextPrompt varOrdText = new TextPrompt("Variable Ordering", varOrd);
//		varOrdText.setShow(TextPrompt.Show.FOCUS_LOST);
//		varOrd.setMinimumSize(varOrd.getPreferredSize());
//		gbc.gridx = 2;
//		mainFrame.getContentPane().add(varOrd, gbc);
//		// a drop down menu for the type of OBDD to be generated
//		String[] bddTypes = {"Complete OBDD", "QOBDD", "ROBDD"};
//		JComboBox<String> bddType = new JComboBox<String>(bddTypes);
//		gbc.fill = GridBagConstraints.NONE;
//		gbc.gridx = 3;
//		mainFrame.getContentPane().add(bddType, gbc);
//		// a drop down menu for the source of the OBDD to be generated
//		String[] bddSources = {"Formula", "Truth Table"};
//		JComboBox<String> bddSource = new JComboBox<String>(bddSources);
//		gbc.gridx = 4;
//		mainFrame.getContentPane().add(bddSource, gbc);
//		// a button to generate a new OBDD
//		JButton generate = new JButton("Generate");
//		gbc.gridx = 5;
//		mainFrame.getContentPane().add(generate, gbc);
		
		
//		// "packing" the frame
//		mainFrame.pack();
		// setting the main frame's bounds
		setBounds(0, 0, screenSize.width, screenSize.height);
		// making the main frame visible
		setVisible(true);
	}
}
