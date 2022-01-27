package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a Hollow tile.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
public class Hollow extends Tile {

	private static final long serialVersionUID = 1L;

	/**
	 * Getter for the item placed on the tile.
	 * 
	 * @return the item placed on this tile. No item can be placed here, therefore
	 *         the item returned is null
	 */
	@Override
	public Item getItem() {
		return null;
	}

	/**
	 * Setter to place an item in the hollow. Since the gamelogic forbids this
	 * action, the item given to the hollow will not be saved and dropped instead.
	 * 
	 * @param item item to be placed in the hollow.
	 */
	@Override
	public void setItem(Item item) {
		// Robbi can't reach a hollow tile. Items will be dropped if it is placed into a
		// hollow
	}

}
