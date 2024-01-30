package com.JayPi4c.RobbiSimulator.model;

import java.io.Serial;

/**
 * Exception to be thrown if robbi attempts to place an item on a tile, that has
 * already an item stored.
 *
 * @author Jonas Pohl
 */
public class TileIsFullException extends RobbiException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new TileIsFullException with localized message.
     */
    public TileIsFullException() {
        super("Exception.TileIsFull");
    }

}
