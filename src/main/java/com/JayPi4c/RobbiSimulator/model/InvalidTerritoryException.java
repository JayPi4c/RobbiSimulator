package com.JayPi4c.RobbiSimulator.model;

import java.io.Serial;

/**
 * Exception to be thrown if the user tries to load an invalid territory.
 *
 * @author Jonas Pohl
 */
public class InvalidTerritoryException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new InvalidTerritoryException with a message.
     */
    public InvalidTerritoryException() {
        super("The loaded territory is invalid.");
    }

}
