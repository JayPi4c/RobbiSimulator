package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class BagIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public BagIsFullException() {
		super(Messages.getString("Exception.BagIsFull"));
	}

}
