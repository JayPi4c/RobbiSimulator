package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class NoItemException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public NoItemException() {
		super(Messages.getString("Exception.NoItem"));
	}

}
