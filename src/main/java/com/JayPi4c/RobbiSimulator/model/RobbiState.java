package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

/**
 * Robbi State to store all state relevant attributes in order to apply the
 * Memento-Pattern.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
@Getter
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

}
