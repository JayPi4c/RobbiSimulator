package com.JayPi4c.RobbiSimulator.model;

/**
 * Enumeration to hold all cardinal-points to store robbi's current facing.
 *
 * @author Jonas Pohl
 */
public enum DIRECTION {

    /**
     * Direction if robbi is looking up.
     */
    NORTH,
    /**
     * Direction if robbi is looking left.
     */
    WEST,
    /**
     * Direction if robbi is looking down.
     */
    SOUTH,
    /**
     * Direction if robbi is looking right.
     */
    EAST;

    /**
     * moves through the direction counter-clockwise
     *
     * @return the next direction after a counter-clockwise 90 degrees turn.
     */
    public DIRECTION next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    /**
     * Moves through the direction clockwise.
     *
     * @return the next direction after a clockwise 90 degrees turn.
     */
    public DIRECTION previous() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }

}
