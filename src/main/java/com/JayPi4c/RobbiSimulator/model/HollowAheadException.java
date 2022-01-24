package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;

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
		super(I18nUtils.i18n("Exception.HollowAhead"));
	}
}
