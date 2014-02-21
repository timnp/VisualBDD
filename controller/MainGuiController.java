package controller;

import view.MainGui;

/**
 * 
 * @author TimNP
 *
 */
public class MainGuiController {
	/**
	 * the MainGui "controlled" by the controller
	 */
	private MainGui gui;
	
	
	
	/**
	 * constructor for a MainGuiController
	 */
	public MainGuiController() {
		// the controller initializes its own GUI
		gui = new MainGui();
	}

}
