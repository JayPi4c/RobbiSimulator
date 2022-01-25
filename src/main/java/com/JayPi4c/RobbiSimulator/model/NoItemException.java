package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;

/**
 * Exception to be thrown if robbi tries to pick up an item from a tile, which
 * has no item provied.
 * 
 * @author Jonas Pohl
 *
 */
public class NoItemException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new NoItemException with a localized message.
	 */
	public NoItemException() {
		super(I18nUtils.i18n("Exception.NoItem"));
	}

}
