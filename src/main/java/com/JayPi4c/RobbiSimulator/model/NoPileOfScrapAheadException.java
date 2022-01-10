package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

public class NoPileOfScrapAheadException extends RobbiException {

	private static final long serialVersionUID = 1L;

	public NoPileOfScrapAheadException() {
		super(Messages.getString("Exception.NoPileOfScrapAhead"));
	}

}
