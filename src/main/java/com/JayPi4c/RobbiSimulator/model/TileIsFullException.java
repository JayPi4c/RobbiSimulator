package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class TileIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public TileIsFullException() {
		super(Messages.getString("Exception.TileIsFull"));
	}

}
