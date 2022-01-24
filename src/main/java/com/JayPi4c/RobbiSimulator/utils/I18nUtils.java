package com.JayPi4c.RobbiSimulator.utils;

import java.util.Locale;
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

	private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.GERMANY);

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
	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	/**
	 * Getter for the current locale.
	 * 
	 * @return the current locale
	 */
	public static Locale getLocale() {
		return locale.get();
	}

	/**
	 * Setter for the current locale.
	 * 
	 * @param locale the new locale
	 */
	public static void setLocale(Locale locale) {
		Locale.setDefault(locale);
		localeProperty().set(locale);
	}

	/**
	 * Returns the String mapped to the provided key in the current locale.
	 * 
	 * @param key the key to be mapped
	 * @return the localized String for the key
	 */
	public static String i18n(String key) {
		return ResourceBundle.getBundle("lang.messages", getLocale()).getString(key);
	}
}
