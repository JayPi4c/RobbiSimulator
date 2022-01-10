package com.JayPi4c.RobbiSimulator.model;

public class NoItemException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public NoItemException() {
		super("Ooops! There is no item, robbi could pick up");
	}

}
