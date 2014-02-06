package view;

import java.awt.*;
import javax.swing.*;

public class MainGui {
	// the main frame (window) itself
	private JFrame mainFrame;
	
	public MainGui () {
		// "creating" the main frame
		mainFrame = new JFrame("Visual BDD");
		// When the frame is closed, the program is exited.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setting the main frame's layout
		mainFrame.getContentPane().setLayout(new GridBagLayout());
		// initializing the GridBagConstraints for the frame
		GridBagConstraints gbc = new GridBagConstraints();
		// a text field for the name of the OBDD
		JTextField bddName = new JTextField();
		TextPrompt bddNameText = new TextPrompt("Name for the BDD", bddName);
		bddNameText.setShow(TextPrompt.Show.FOCUS_LOST);
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainFrame.add(bddName, gbc);
		// a text field for the formula to be represented by the OBDD
		JTextField formula = new JTextField();
		TextPrompt formulaText = 
				new TextPrompt("Formula to be represented", formula);
		formulaText.setShow(TextPrompt.Show.FOCUS_LOST);
		gbc.gridx = 2;
		mainFrame.add(formula, gbc);
		// a text field for the variable ordering
		JTextField varOrd = new JTextField();
		TextPrompt varOrdText = new TextPrompt("Variable Ordering", varOrd);
		varOrdText.setShow(TextPrompt.Show.FOCUS_LOST);
		gbc.gridx = 4;
		mainFrame.add(varOrd, gbc);
		// a drop down menu for the type of OBDD to be generated
		String[] bddTypes = {"Complete OBDD", "QOBDD", "ROBDD"};
		JComboBox<String> bddType = new JComboBox<String>(bddTypes);
		gbc.gridwidth = 1;
		gbc.gridx = 6;
		mainFrame.add(bddType, gbc);
		// a drop down menu for the source of the OBDD to be generated
		String[] bddSources = {"Formula", "Truth Table"};
		JComboBox<String> bddSource = new JComboBox<String>(bddSources);
		gbc.gridx = 7;
		mainFrame.add(bddSource, gbc);
		// a button to generate a new OBDD
		JButton generate = new JButton("Generate");
		gbc.gridx = 8;
		mainFrame.add(generate, gbc);
		
		
		// "packing" the frame
		mainFrame.pack();
		// retrieving the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// setting the main frame's bounds
		mainFrame.setBounds(0, 0, screenSize.width, screenSize.height);
		// making the main frame visible
		mainFrame.setVisible(true);
	}
	
	public void show() {
		mainFrame.show();
	}
}
