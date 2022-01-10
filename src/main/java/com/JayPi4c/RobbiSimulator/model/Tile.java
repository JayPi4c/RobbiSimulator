package com.JayPi4c.RobbiSimulator.model;

import java.io.Serializable;

public class Tile implements Serializable {

	private static final long serialVersionUID = 1L;

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

}