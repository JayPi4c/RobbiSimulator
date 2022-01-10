package com.JayPi4c.RobbiSimulator.model;

public class Hollow extends Tile {

	@Override
	public Item getItem() {
		return null;
	}

	@Override
	public void setItem(Item item) {
		// no item can be placed in a hollow. placed item will be dropped
	}

}
