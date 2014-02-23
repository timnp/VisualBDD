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
	private MainGui mainGui;
	
	
	
	/**
	 * constructor for a MainGuiController
	 */
	public MainGuiController(MainGui mainGui) {
		// the controller initializes its own GUI
		this.mainGui = mainGui;
	}

}
