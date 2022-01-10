package com.JayPi4c.RobbiSimulator.model;

import java.util.ArrayList;

public class Stockpile extends Tile {

	ArrayList<Item> items;

	public Stockpile() {
		items = new ArrayList<Item>();
	}

	@Override
	public void setItem(Item item) {
		items.add(item);
	}

	public Item getItem() {
		return items.get(items.size() - 1);
	}

	@Override
	public Item pickItem() {
		return items.remove(items.size() - 1);
	}

}
