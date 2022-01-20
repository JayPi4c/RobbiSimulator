package com.JayPi4c.RobbiSimulator.controller.examples;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;

/**
 * This class holds all information needed for an example to be loaded. It
 * provides a method to load a new MainStage with the given Information.
 * 
 * @author Jonas Pohl
 *
 */
public record Example(String pogramName, String code, String territory) {

	/**
	 * Loads a new MainStage with the given information.
	 */
	public void load() {
		ProgramController.createAndShow(pogramName, code, territory);
	}
}
