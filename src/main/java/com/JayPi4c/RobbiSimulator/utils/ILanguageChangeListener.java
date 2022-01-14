package com.JayPi4c.RobbiSimulator.utils;

/**
 * 
 * Listener to be implemented by all classes that need to be informed about a
 * language change in order to update their GUI.
 * 
 * @author Jonas Pohl
 *
 */
public interface ILanguageChangeListener {
	/**
	 * Is called whenever the Messages class has updated its resource file
	 */
	public void onLanguageChanged();
}
