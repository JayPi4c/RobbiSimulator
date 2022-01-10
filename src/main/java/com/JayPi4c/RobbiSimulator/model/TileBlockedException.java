package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class TileBlockedException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public TileBlockedException() {
		super(Messages.getString("Exception.TileBlocked"));
	}

}
