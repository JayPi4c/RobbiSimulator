package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.controller.I18nUtils;

/**
 * Exception to be thrown if robbi attempts to push a pile of scrap while there
 * is no pile of scrap ahead of robbi.
 * 
 * @author Jonas Pohl
 *
 */
public class NoPileOfScrapAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new NoPileOfScrapAheadException with localized message.
	 */
	public NoPileOfScrapAheadException() {
		super(I18nUtils.i18n("Exception.NoPileOfScrapAhead"));
	}

}
