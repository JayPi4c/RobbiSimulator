package com.JayPi4c.RobbiSimulator.model;

public class NoPileOfScrapAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public NoPileOfScrapAheadException() {
		super("Oops! Robbi can't find a pile to push.");
	}

}
