package com.JayPi4c.RobbiSimulator.controller;

/**
 * This class stores the state of the currently selected territory button state.
 * The state should be modified by the editor tool buttons.
 * 
 * If the TerritoryEventHandler does register a click, the eventHandler gets the
 * current state from this class
 * 
 * @author Jonas Pohl
 *
 */
public class ButtonState {
	/**
	 * Constant for the ButtonState NONE.
	 */
	public static final int NONE = -1;
	/**
	 * Constant for the ButtonState ROBBI.
	 */
	public static final int ROBBI = 0;

	/**
	 * Constant for the ButtonState HOLLOW.
	 */
	public static final int HOLLOW = 1;
	/**
	 * Constant for the ButtonState PILE_OF_SCRAP.
	 */
	public static final int PILE_OF_SCRAP = 2;
	/**
	 * Constant for the ButtonState STOCKPILE.
	 */
	public static final int STOCKPILE = 3;
	/**
	 * Constant for the ButtonState ACCU.
	 */
	public static final int ACCU = 4;
	/**
	 * Constant for the ButtonState SCREW.
	 */
	public static final int SCREW = 5;
	/**
	 * Constant for the ButtonState NUT.
	 */
	public static final int NUT = 6;
	/**
	 * Constant for the ButtonState CLEAR.
	 */
	public static final int CLEAR = 7;

	private int selected;

	/**
	 * Creates a new ButtonState and sets the selected state to NONE
	 */
	public ButtonState() {
		selected = NONE;
	}

	/**
	 * reset the currently selected state to NONE
	 */
	public void deselect() {
		selected = NONE;
	}

	/**
	 * Sets the current state to the given state
	 * 
	 * @param state the state, the saved state should be changed to
	 */
	public void setSelected(int state) {
		this.selected = state;
	}

	/**
	 * Returns the currently selected state
	 * 
	 * @return the currently selected state
	 */
	public int getSelected() {
		return this.selected;
	}
}
