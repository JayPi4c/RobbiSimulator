package com.JayPi4c.RobbiSimulator.model;

public class HollowAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public HollowAheadException() {
		super("Ooops! Robbi can't move forward. There is a hollow ahead.");
	}
}
