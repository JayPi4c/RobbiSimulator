package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.controller.ButtonState;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;

import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class PlaceToggleGroup extends ToggleGroup implements Observer {

	private ButtonState buttonState;

	/**
	 * Die Toggle buttons müssen übergeben werden, damit sie alle schon Teil der
	 * ToggleGroup sind, wenn der ChangeListener erstellt wird.
	 * 
	 * @param buttonState
	 * @param toggles
	 */
	public PlaceToggleGroup(ButtonState buttonState, Toggle... toggles) {

		this.buttonState = buttonState;
		this.buttonState.addObserver(this);

		for (Toggle t : toggles)
			t.setToggleGroup(this);

		selectedToggleProperty().addListener((observer, oldToggle, newToggle) -> {
			buttonState.setSelected(getToggles().indexOf(newToggle));
			buttonState.setChanged();
			buttonState.notifyAllObservers();
		});
	}

	@Override
	public void update(Observable observable) {
		int selected = buttonState.getSelected();
		if (selected == ButtonState.NONE) {
			if (getSelectedToggle() != null)
				getSelectedToggle().setSelected(false);
		} else
			getToggles().get(buttonState.getSelected()).setSelected(true);
	}

}
