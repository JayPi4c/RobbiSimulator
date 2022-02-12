package com.JayPi4c.RobbiSimulator.utils;

import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Utility-Class to support internationalization.
 * 
 * @author Jonas Pohl
 *
 */
public class I18nUtils {

	/**
	 * The resource bundle on which elements can bind in order to be updated on
	 * language change.
	 */
	private static final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

	/**
	 * Private constructor to hide the implicit one.
	 */
	private I18nUtils() {

	}

	/**
	 * ObjectProperty to allow bindings
	 * 
	 * @return The ObjectProperty
	 */
	public static ObjectProperty<ResourceBundle> bundleProperty() {
		return bundle;
	}

	/**
	 * Getter for the current locale.
	 * 
	 * @return the current locale
	 */
	public static ResourceBundle getBundle() {
		return bundle.get();
	}

	/**
	 * Setter for the current resourceBundle.
	 * 
	 * @param bundle the new bundle
	 */
	public static void setBundle(ResourceBundle bundle) {
		bundleProperty().set(bundle);
	}

	/**
	 * Returns the String mapped to the provided key in the current locale.
	 * 
	 * @param key the key to be mapped
	 * @return the localized String for the key
	 */
	public static String i18n(String key) {
		return getBundle().getString(key);
	}
}
