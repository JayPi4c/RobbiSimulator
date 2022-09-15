package com.JayPi4c.RobbiSimulator.model;

/**
 * Exception to be thrown if an item is picked up while the bag is already full.
 * 
 * @author Jonas Pohl
 *
 */
public class BagIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new BagIsFullException with a localized message.
	 */
	public BagIsFullException() {
		super("Exception.BagIsFull");
	}

}
