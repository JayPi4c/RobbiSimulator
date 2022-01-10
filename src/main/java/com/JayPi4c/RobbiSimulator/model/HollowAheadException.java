package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class HollowAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public HollowAheadException() {
		super(Messages.getString("Exception.HollowAhead"));
	}
}
