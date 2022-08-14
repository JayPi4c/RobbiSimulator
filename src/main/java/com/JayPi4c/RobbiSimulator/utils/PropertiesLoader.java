package com.JayPi4c.RobbiSimulator.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility-class for Properties.
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesLoader {

	private static Properties properties;

	private static final String DIR = System.getProperty("user.dir");
	private static final String FILE = "/simulator.properties";
	private static final String COMMENTS = "role=student OR tutor";

	/**
	 * Loads the properties and stores them in an Object.
	 * 
	 * If the simulator.properties file can't be found, default values will be
	 * loaded.
	 * 
	 * @return true if the initialization was successful, false otherwise
	 */
	public static boolean initialize() {
		properties = new Properties();
		try (InputStream in = new FileInputStream(DIR + FILE)) {
			properties.load(in);
			return true;
		} catch (IOException e) {
			loadDefaultProperties();
			return false;
		}
	}

	/**
	 * Getter for the role
	 * 
	 * @return true if the role is set to tutor, false otherwise
	 */
	public static boolean isTutor() {
		try {
			return properties.getProperty("role").equalsIgnoreCase("tutor");
		} catch (NullPointerException e) {
			properties.put("role", "student");
			return false;
		}
	}

	/**
	 * Getter for the sounds property.
	 * 
	 * @return true if the sounds property is set to true
	 */
	public static boolean getSounds() {
		return Boolean.parseBoolean(properties.getProperty("sounds"));
	}

	/**
	 * Getter for the sounds property.
	 * 
	 * @return true if the sounds property is set to true
	 */
	public static boolean getDarkmode() {
		return Boolean.parseBoolean(properties.getProperty("darkmode"));
	}

	/**
	 * Getter for the tutorhost.
	 * 
	 * @return the tutorhost stored in the properties file, localhost if no
	 *         propterty is found
	 */
	public static String getTutorhost() {
		String key = "tutorhost";
		String host = properties.getProperty(key);
		if (host == null) {
			host = "localhost";
			properties.put(key, host);
		}
		return host;
	}

	/**
	 * Getter for the tutorport.
	 * 
	 * @return the tutorport stored in the properties file
	 */
	public static int getTutorport() {
		try {
			return Integer.parseInt(properties.getProperty("tutorport"));
		} catch (NumberFormatException | NullPointerException e) {
			properties.put("tutorport", "3579");
			return 3579;
		}
	}

	/**
	 * Getter for the locale.
	 * 
	 * @return the locale stored in the properties file
	 */
	public static Locale getLocale() {
		try {
			String[] parts = properties.getProperty("lang").split("_");
			return new Locale(parts[0], parts[1]);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			logger.debug("Failed to load locale from properties");
			properties.put("lang", Locale.GERMANY.toString());
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
		properties.put("sounds", Boolean.toString(SoundManager.getSound()));
		properties.put("darkmode", Boolean.toString(SceneManager.getDarkmode()));
		try (FileOutputStream fos = new FileOutputStream(DIR + FILE)) {
			properties.store(fos, COMMENTS);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Loads initial values in the properties object. This is needed if the
	 * application failed to load properties from the file.
	 */
	private static void loadDefaultProperties() {
		properties.put("lang", Locale.GERMANY.toString());
		properties.put("role", "student");
		properties.put("tutorport", "3579");
		properties.put("tutorhost", "localhost");
		properties.put("sounds", Boolean.toString(false));
		properties.put("darkmode", Boolean.toString(false));
	}

}
