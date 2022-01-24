package com.JayPi4c.RobbiSimulator.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class I18nUtils {
	private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.GERMANY);

	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	public static Locale getLocale() {
		return locale.get();
	}

	public static void setLocale(Locale locale) {
		Locale.setDefault(locale);
		localeProperty().set(locale);
	}

	public static String i18n(String key) {
		return ResourceBundle.getBundle("lang.messages", getLocale()).getString(key);
	}
}
