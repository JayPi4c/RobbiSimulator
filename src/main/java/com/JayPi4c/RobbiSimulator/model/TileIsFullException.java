package com.JayPi4c.RobbiSimulator.model;

public class TileIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public TileIsFullException() {
		super("Ooops! There is already an item on the tile. Robbi can't place anything here.");
	}

}
