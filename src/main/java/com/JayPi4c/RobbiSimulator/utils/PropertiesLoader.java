package com.JayPi4c.RobbiSimulator.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility-class for Properties.
 * 
 * @author Jonas Pohl
 *
 */
public class PropertiesLoader {
	private static final Logger logger = LogManager.getLogger(PropertiesLoader.class);
	private static Properties properties;

	private static final String DIR = System.getProperty("user.dir");
	private static final String FILE = "/simulator.properties";

	/**
	 * Private constructor to hide the implicit one.
	 */
	private PropertiesLoader() {
	}

	/**
	 * Loads the properties and stores them in an Object.
	 * 
	 * @return true if the initialization was successful, false otherwise
	 */
	public static boolean initialize() {
		properties = new Properties();
		try (InputStream in = new FileInputStream(DIR + FILE)) {
			properties.load(in);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Getter for the role
	 * 
	 * @return true if the role is set to tutor, false otherwise
	 */
	public static boolean isTutor() {
		return properties.getProperty("role").toString().equalsIgnoreCase("Tutor");
	}

	/**
	 * Getter for the tutorhost.
	 * 
	 * @return the tutorhost stored in the properties file
	 */
	public static String getTutorhost() {
		return properties.getProperty("tutorhost");
	}

	/**
	 * Getter for the tutorport.
	 * 
	 * @return the tutorport stored in the properties file
	 */
	public static int getTutorport() {
		try {
			return Integer.parseInt(properties.getProperty("tutorport"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Getter for the locale.
	 * 
	 * @return the locale stored in the properties file
	 */
	public static Locale getLocale() {
		String[] parts = properties.getProperty("lang").split("_");
		try {
			Locale locale = new Locale(parts[0], parts[1]);
			return locale;
		} catch (IndexOutOfBoundsException e) {
			logger.debug("Failed to load locale from properties");
			return Locale.GERMANY;
		}
	}

	/**
	 * Stores all properties back in the properties file.
	 * 
	 * @return true, if the saving was successful, false otherwise
	 */
	public static boolean finish() {
		properties.put("lang", I18nUtils.getLocale().toString());
		try (FileOutputStream fos = new FileOutputStream(DIR + FILE)) {
			properties.store(fos, null);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
