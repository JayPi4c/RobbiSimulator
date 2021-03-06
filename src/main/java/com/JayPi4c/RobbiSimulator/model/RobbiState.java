package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Robbi State to store all state relevant attributes in order to apply the
 * Memento-Pattern.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
public class RobbiState {
	@XmlElement
	private int x;
	@XmlElement
	private int y;
	@XmlElement
	private DIRECTION facing;
	@XmlAnyElement
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
	 * This constructor is needed for JAXB to work.
	 */
	@SuppressWarnings("unused")
	private RobbiState() {
		// needed for JAXB
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
