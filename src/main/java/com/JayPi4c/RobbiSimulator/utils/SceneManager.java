package com.JayPi4c.RobbiSimulator.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class to manage the loaded scene.
 * 
 * @author Jonas Pohl
 *
 */
public class SceneManager {
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
	 * Getter for the current darkmode flag.
	 * 
	 * @return the current darkmode setting
	 */
	public static boolean getDarkmode() {
		return darkmode.get();
	}

	/**
	 * Setter for the current darkmode flag.
	 * 
	 * @param flag the new value
	 */
	public static void setDarkmode(boolean flag) {
		darkmodeProperty().set(flag);
	}

	/**
	 * Getter for the darkmode css source.
	 * 
	 * @return the source for the darkmode css
	 */
	public static String getDarkmodeCss() {
		return "css/dark-theme.css";
	}

	/**
	 * Private constructor to hide the implicit one.
	 */
	private SceneManager() {
	}
}
