package com.JayPi4c.RobbiSimulator.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class to manage the loaded scene.
 * 
 * @author Jonas Pohl
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SceneManager {
	
	private static final String DARK_MODE_CSS = "/css/dark-theme.css";
	
	/**
	 * The boolean on which elements can bind in order to be updated on dark-mode
	 * change.
	 */
	private static final ObjectProperty<Boolean> darkmode = new SimpleObjectProperty<>(PropertiesLoader.getDarkmode());

	/**
	 * ObjectProperty to allow bindings
	 * 
	 * @return The ObjectProperty
	 */
	public static ObjectProperty<Boolean> darkmodeProperty() {
		return darkmode;
	}

	/**
	 * Getter for the current dark-mode flag.
	 * 
	 * @return the current dark-mode setting
	 */
	public static boolean getDarkmode() {
		return darkmode.get();
	}

	/**
	 * Setter for the current dark-mode flag.
	 * 
	 * @param flag the new value
	 */
	public static void setDarkmode(boolean flag) {
		darkmodeProperty().set(flag);
	}

	/**
	 * Getter for the dark-mode css source.
	 * 
	 * @return the source for the dark-mode css
	 */
	public static String getDarkmodeCss() {
		return DARK_MODE_CSS;
	}

}
