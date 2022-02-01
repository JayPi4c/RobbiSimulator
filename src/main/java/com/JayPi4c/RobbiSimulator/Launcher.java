package com.JayPi4c.RobbiSimulator;

import javax.swing.JOptionPane;

/**
 * In order to start the javaFX application without setting up the javaFX module
 * in the settings, this class calls the main method of the App class. This
 * somehow starts the app without a problem.
 * 
 * @author Jonas Pohl
 *
 */
public class Launcher {

	/**
	 * Main-method to start the whole program.
	 * 
	 * @param args arguments from command line
	 */
	public static void main(String[] args) {
		try {
			App.main(args);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, """
					Failed to load application.
					Further information can be obtained running the
					application in the command line.
					Please consider submitting your error.
								""");
		}
	}

}
