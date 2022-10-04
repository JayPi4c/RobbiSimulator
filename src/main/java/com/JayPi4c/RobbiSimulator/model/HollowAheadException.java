package com.JayPi4c.RobbiSimulator.model;

/**
 * Exception to be thrown if robbi tries to move into a hollow-
 * 
 * @author Jonas Pohl
 *
 */
public class HollowAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new HollowAheadException with a localized message.
	 */
	public HollowAheadException() {
		super("Exception.HollowAhead");
	}
}
