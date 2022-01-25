package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;

/**
 * Exception to be thrown if robbi attempts to move a pile of Scrap while the
 * tile ahead is not suitable to push a pile to.
 * 
 * @author Jonas Pohl
 *
 */
public class TileBlockedException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new TileBlockedException with localized message.
	 */
	public TileBlockedException() {
		super(I18nUtils.i18n("Exception.TileBlocked"));
	}

}
