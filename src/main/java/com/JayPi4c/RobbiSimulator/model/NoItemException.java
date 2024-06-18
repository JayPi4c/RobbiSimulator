package com.JayPi4c.RobbiSimulator.model;

import java.io.Serial;

/**
 * Exception to be thrown if robbi tries to pick up an item from a tile, which
 * has no item provied.
 *
 * @author Jonas Pohl
 */
public class NoItemException extends RobbiException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new NoItemException with a localized message.
     */
    public NoItemException() {
        super("Exception.NoItem");
    }

}
