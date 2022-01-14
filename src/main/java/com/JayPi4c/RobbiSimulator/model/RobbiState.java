package com.JayPi4c.RobbiSimulator.model;

/**
 * Robbi State to store all state relevant attributes in order to apply the
 * Memento-Pattern.
 * 
 * @author Jonas Pohl
 *
 */
public class RobbiState {
	private int x;
	private int y;
	private DIRECTION facing;
	private Item item;

	/**
	 * Constructor to create a new RobbiState. Stores the values as a wrapper.
	 * 
	 * @param x      x-ordinate in the territory
	 * @param y      y-ordinate in the territory
	 * @param facing robbis facing
	 * @param item   the item robbi is holding
	 */
	public RobbiState(int x, int y, DIRECTION facing, Item item) {
		this.x = x;
		this.y = y;
		this.facing = facing;
		this.item = item;
	}

	/**
	 * Getter for the x-attribute
	 * 
	 * @return x-ordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for the y-attribute
	 * 
	 * @return y-ordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter for the facing-attribute
	 * 
	 * @return robbis facing
	 */
	public DIRECTION getFacing() {
		return facing;
	}

	/**
	 * Getter for the item-attribute
	 * 
	 * @return the item robbi is holding
	 */
	public Item getItem() {
		return item;
	}

}
