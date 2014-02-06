package view;

import java.awt.*;

import javax.swing.*;

public class MainGui {
	// the main frame (window) itself
	private JFrame mainFrame;
	
	public MainGui () {
		// retrieving the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// "creating" the main frame
		mainFrame = new JFrame("Visual BDD");
		// When the frame is closed, the program is exited.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// a panel for the top of window
		JPanel topPane = new JPanel();
		topPane.setLayout(new FlowLayout());
		// a drop down menu for the type of OBDD to be generated
		String[] bddTypes = {"Complete OBDD", "QOBDD", "ROBDD"};
		JComboBox<String> bddType = new JComboBox<String>(bddTypes);
		// a button to generate a new OBDD
		JButton generate = new JButton("Generate");
		// calculating the remaining width for the three text fields
		int remainingWidth = 
				screenSize.width - (bddType.getWidth() + generate.getWidth());
		// dividing the remaining width among the text fields
		int bddNameWidth = (int) (remainingWidth / 90);
		int formulaWidth = (int) (remainingWidth / 30);
		int varOrdWidth = (int) (remainingWidth / 45);
		// a text field for the name of the OBDD 
		JTextField bddName = new JTextField(bddNameWidth);
		TextPrompt bddNameText = new TextPrompt("Name for the BDD", bddName);
		bddNameText.setShow(TextPrompt.Show.FOCUS_LOST);
		bddName.setMinimumSize(bddName.getPreferredSize());
		topPane.add(bddName, BorderLayout.PAGE_START);
		// a text field for the formula to be represented by the OBDD
		JTextField formula = new JTextField(formulaWidth);
		TextPrompt formulaText = 
				new TextPrompt("Formula to be represented", formula);
		formulaText.setShow(TextPrompt.Show.FOCUS_LOST);
		formula.setMinimumSize(formula.getPreferredSize());
		topPane.add(formula, BorderLayout.PAGE_START);
		// a text field for the variable ordering
		JTextField varOrd = new JTextField(varOrdWidth);
		TextPrompt varOrdText = new TextPrompt("Variable Ordering", varOrd);
		varOrdText.setShow(TextPrompt.Show.FOCUS_LOST);
		varOrd.setMinimumSize(varOrd.getPreferredSize());
		topPane.add(varOrd, BorderLayout.PAGE_START);
		// adding the OBDD type menu
		topPane.add(bddType, BorderLayout.PAGE_START);
		// adding the generate button
		topPane.add(generate, BorderLayout.PAGE_START);
		// adding the top panel to the main frame
		mainFrame.getContentPane().add(topPane, BorderLayout.PAGE_START);
		
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
		mainFrame.setBounds(0, 0, screenSize.width, screenSize.height);
		// making the main frame visible
		mainFrame.setVisible(true);
	}
	
	public void show() {
		mainFrame.show();
	}
}
