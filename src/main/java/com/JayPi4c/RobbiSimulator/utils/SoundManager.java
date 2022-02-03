package com.JayPi4c.RobbiSimulator.utils;

import java.net.URISyntaxException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manager to load and play sounds.
 * 
 * @author Jonas Pohl
 *
 */
public class SoundManager {

	private static final ObjectProperty<Boolean> sound = new SimpleObjectProperty<>(PropertiesLoader.getSounds());

	private static Media WARNING_SOUND = null;

	/**
	 * Plays a warning sounds. If the sound is not loaded yet, the sound will be
	 * loaded and stored for further plays.
	 */
	public static void playWarnSound() {
		if (sound.get())
			return;
		if (WARNING_SOUND == null) {
			try {
				WARNING_SOUND = new Media(
						SoundManager.class.getResource("/sounds/warning-sound.mp3").toURI().toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		MediaPlayer mediaPlayer = new MediaPlayer(WARNING_SOUND);
		mediaPlayer.play();
	}

	public static ObjectProperty<Boolean> soundProperty() {
		return sound;
	}

	public static boolean getSound() {
		return sound.get();
	}

	public static void setSound(boolean flag) {
		soundProperty().set(flag);
	}

}
