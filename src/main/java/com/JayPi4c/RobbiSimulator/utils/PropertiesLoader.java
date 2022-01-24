package com.JayPi4c.RobbiSimulator.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	private static Properties properties;

	public static void initialize() {
		properties = new Properties();
		String dir = System.getProperty("user.dir");
		try (InputStream in = new FileInputStream(dir + "/simulator.properties")) {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
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

}
