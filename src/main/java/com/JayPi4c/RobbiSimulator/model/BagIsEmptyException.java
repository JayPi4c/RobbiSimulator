package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class BagIsEmptyException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public BagIsEmptyException() {
		super(Messages.getString("Exception.BagIsEmpty"));
	}

}
