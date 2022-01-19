package com.JayPi4c.RobbiSimulator.controller.examples;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;

public record Example(String pogramName, String code, String territory) {
	public void load() {
		ProgramController.createAndShow(pogramName, code, territory);
	}
}
