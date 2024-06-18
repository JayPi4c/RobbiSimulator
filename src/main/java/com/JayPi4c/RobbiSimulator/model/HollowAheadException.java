package com.JayPi4c.RobbiSimulator.model;

import java.io.Serial;

/**
 * Exception to be thrown if robbi tries to move into a hollow.
 *
 * @author Jonas Pohl
 */
public class HollowAheadException extends RobbiException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new HollowAheadException with a localized message.
     */
    public HollowAheadException() {
        super("Exception.HollowAhead");
    }
}
