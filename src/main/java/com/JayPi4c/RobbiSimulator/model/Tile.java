package com.JayPi4c.RobbiSimulator.model;

import java.io.Serializable;

/**
 * The default Tile for the territory.
 * 
 * @author Jonas Pohl
 *
 */
public class Tile implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Attribute to store the item whoch is placed on the tile.
	 */
	private Item item = null;

	/**
	 * 
	 * Getter for the item that is stored on the tile.
	 * 
	 * @return the item stored on the tile
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Setter for the item that is stored on the tile.
	 * 
	 * @param item the item to be stored on the tile
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Removes the item from the tile and returns it.
	 * 
	 * @return the item stored on the tile
	 */
	public Item pickItem() {
		Item it = item;
		item = null;
		return it;
	}

}