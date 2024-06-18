package com.JayPi4c.RobbiSimulator.model;

import java.io.Serial;

/**
 * Exception to be thrown if robbi attempts to push a pile of scrap while there
 * is no pile of scrap ahead of robbi.
 *
 * @author Jonas Pohl
 */
public class NoPileOfScrapAheadException extends RobbiException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new NoPileOfScrapAheadException with localized message.
     */
    public NoPileOfScrapAheadException() {
        super("Exception.NoPileOfScrapAhead");
    }

}
