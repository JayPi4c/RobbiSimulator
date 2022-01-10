package com.JayPi4c.RobbiSimulator.utils;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides mechanics to allow a change of language at runtime. In
 * order to fullfill this task View-Elements can implement the
 * {@link ILanguageChangeListener} and register themselves to this class via the
 * {@link #registerListener(ILanguageChangeListener)} method. In order to get
 * strings in the correct language, implementations of ILanguageChangeListener
 * must use the {@link #getString(String)} method.
 * 
 * To update the language at runtime all elements inside the view have to update
 * their text. To accomplish this, the Messages class will call to
 * {@link ILanguageChangeListener#onLanguageChanged()} method on every
 * registered ILanguageChangeListener.
 * 
 * 
 * <br>
 * <br>
 * Sollten wir im Verlauf des Java Praktikums eine andere Methodik kennen
 * lernen, um die Sprache des Programms zur Laufzeit zu verändern, bin ich gerne
 * bereit, den gesamten Code an die neue Technik anzupassen. :)
 * 
 * 
 * @author Jonas Pohl
 *
 */
public class Messages {
	private static String BUNDLE_NAME = "lang.messages_de_DE";
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static ArrayList<ILanguageChangeListener> listeners = new ArrayList<>();

	/**
	 * Returns the String according to the key provided as parameter. If the key can
	 * not be mapped to a String, the method will return the key enclosed with two
	 * exclamation marks.
	 * 
	 * @param key The key whose string is needed
	 * @return The String mapped to the key, or "!!key!!"
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "!!" + key + "!!";
		}
	}

	/**
	 * Add a LanguageListener to the list of elements which need to be informed
	 * about a change of language.
	 * 
	 * @param l
	 */
	public static void registerListener(ILanguageChangeListener l) {
		listeners.add(l);
	}

	/**
	 * Remove a LanguageListener from the list.
	 * 
	 * @param l
	 */
	public static void removeListener(ILanguageChangeListener l) {
		listeners.remove(l);
	}

	/**
	 * 
	 * Allows to change the language at runtime. The new language bundle will be
	 * loaded and all registered View elements will be informed using the
	 * ILanguageChangeListener's onLanguageChanged() method.
	 * 
	 * @param bundleName The name of the new language bundle
	 */
	public static void changeBundle(String bundleName) {
		BUNDLE_NAME = bundleName;
		RESOURCE_BUNDLE = ResourceBundle.getBundle(bundleName);
		for (ILanguageChangeListener l : listeners)
			l.onLanguageChanged();
	}
}
