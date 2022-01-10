package com.JayPi4c.RobbiSimulator.model;

public class Tile {
	private Item item = null;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item pickItem() {
		Item it = item;
		item = null;
		return it;
	}

	public void draw() {
		// implement functionality to draw tile
	}

}
