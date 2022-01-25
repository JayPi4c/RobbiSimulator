package com.JayPi4c.RobbiSimulator.model;

/**
 * Abstract Exception class for almost all exceptions thrown in this
 * application.
 * 
 * @author Jonas Pohl
 *
 */
public abstract class RobbiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new RobbiException with the given message.
	 * 
	 * @param msg a message to provide further details of the exception
	 */
	protected RobbiException(String msg) {
		super(msg);
	}

}
