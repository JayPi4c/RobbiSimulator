package com.JayPi4c.RobbiSimulator.model;

/**
 * Exception to be thrown if an item is placed down while there is no item in
 * the bag.
 * 
 * @author Jonas Pohl
 *
 */
public class BagIsEmptyException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new BagIsEmptyException with a localized message.
	 */
	public BagIsEmptyException() {
		super("Exception.BagIsEmpty");
	}

}
