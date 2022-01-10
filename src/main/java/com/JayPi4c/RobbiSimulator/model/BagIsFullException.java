package com.JayPi4c.RobbiSimulator.model;

public class BagIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public BagIsFullException() {
		super("Ooops! Robbi can't pick up an item. His bag is full.");
	}

}
