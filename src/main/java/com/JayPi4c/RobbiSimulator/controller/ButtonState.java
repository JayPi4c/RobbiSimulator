package com.JayPi4c.RobbiSimulator.controller;

public class ButtonState {

	public static final int NONE = -1;
	public static final int ROBBI = 0;
	public static final int HOLLOW = 1;
	public static final int PILE_OF_SCRAP = 2;
	public static final int STOCKPILE = 3;
	public static final int ACCU = 4;
	public static final int SCREW = 5;
	public static final int NUT = 6;
	public static final int CLEAR = 7;

	private int selected;

	public ButtonState() {
		selected = NONE;
	}

	public void deselect() {
		selected = NONE;
	}

	public void setSelected(int state) {
		this.selected = state;
	}

	public int getSelected() {
		return this.selected;
	}
}
