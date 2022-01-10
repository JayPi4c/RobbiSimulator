package com.JayPi4c.RobbiSimulator.model;

public class TileBlockedException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public TileBlockedException() {
		super("Ooops! Robbi can't push this pile of scrap. The next tile is blocked.");
	}

}
