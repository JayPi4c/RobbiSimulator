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

		System.out.println(properties.get("role"));
	}

	public static boolean isTutor() {
		return properties.get("role").toString().equalsIgnoreCase("Tutor");
	}

}
