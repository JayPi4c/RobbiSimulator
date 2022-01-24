package com.JayPi4c.RobbiSimulator.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesLoader {
	private static final Logger logger = LogManager.getLogger(PropertiesLoader.class);
	private static Properties properties;

	private static final String dir = System.getProperty("user.dir");
	private static final String file = "/simulator.properties";

	public static boolean initialize() {
		properties = new Properties();
		try (InputStream in = new FileInputStream(dir + file)) {
			properties.load(in);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean isTutor() {
		return properties.getProperty("role").toString().equalsIgnoreCase("Tutor");
	}

	public static String getTutorhost() {
		return properties.getProperty("tutorhost");
	}

	public static int getTutorport() {
		try {
			return Integer.parseInt(properties.getProperty("tutorport"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

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

	public static boolean finish() {
		properties.put("lang", I18nUtils.getLocale().toString());
		try (FileOutputStream fos = new FileOutputStream(dir + file)) {
			properties.store(fos, null);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
