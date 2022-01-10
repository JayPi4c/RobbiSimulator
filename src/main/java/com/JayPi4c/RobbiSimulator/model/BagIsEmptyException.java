package com.JayPi4c.RobbiSimulator.model;

public class BagIsEmptyException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public BagIsEmptyException() {
		super("Ooops! Robbi can't place its item. The bag is empty.");
	}

}
