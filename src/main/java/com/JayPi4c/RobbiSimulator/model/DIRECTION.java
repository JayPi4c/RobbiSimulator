package com.JayPi4c.RobbiSimulator.model;

public enum DIRECTION {
	NORTH, WEST, SOUTH, EAST;

	private static DIRECTION[] vals = values();

	public DIRECTION next() {
		return vals[(this.ordinal() + 1) % vals.length];
	}

	public DIRECTION previous() {
		return vals[(this.ordinal() + vals.length - 1) % vals.length];
	}
}
