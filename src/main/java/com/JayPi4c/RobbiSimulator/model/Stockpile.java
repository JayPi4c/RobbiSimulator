package com.JayPi4c.RobbiSimulator.model;

import java.util.ArrayList;

public class Stockpile extends Tile {

	ArrayList<Item> items;
	// TODO Queue more efficient?
	// check if queue would be better, allowing an easier iteration over the
	// elements.

	public Stockpile() {
		items = new ArrayList<>();
	}

	@Override
	public void setItem(Item item) {
		items.add(item);
	}

	public Item getItem() {
		return (items.size() == 0) ? null : items.get(items.size() - 1);
	}

	@Override
	public Item pickItem() {
		return items.remove(items.size() - 1);
	}

	public ArrayList<Item> getAllItems() {
		return items;
	}

}
