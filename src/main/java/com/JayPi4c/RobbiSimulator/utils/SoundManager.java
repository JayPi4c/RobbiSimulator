package com.JayPi4c.RobbiSimulator.utils;

import java.net.URISyntaxException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Manager to load and play sounds.
 * 
 * @author Jonas Pohl
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SoundManager {

	/**
	 * The boolean on which elements can bind in order to be updated if sound is
	 * deactivated /activated
	 */
	private static final ObjectProperty<Boolean> sound = new SimpleObjectProperty<>(PropertiesLoader.getSounds());

	private static Media warningSound = null;

	/**
	 * Plays a warning sounds. If the sound is not loaded yet, the sound will be
	 * loaded and stored for further plays.
	 */
	public static void playWarnSound() {
		if (Boolean.FALSE.equals(sound.get()))
			return;
		if (warningSound == null) {
			try {
				warningSound = new Media(
						SoundManager.class.getResource("/sounds/warning-sound.mp3").toURI().toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		MediaPlayer mediaPlayer = new MediaPlayer(warningSound);
		mediaPlayer.play();
	}

	/**
	 * ObjectProperty to allow bindings
	 * 
	 * @return The ObjectProperty
	 */
	public static ObjectProperty<Boolean> soundProperty() {
		return sound;
	}

	/**
	 * Getter for the current sound flag.
	 * 
	 * @return the current sound setting
	 */
	public static boolean getSound() {
		return sound.get();
	}

	/**
	 * Setter for the current sound flag.
	 * 
	 * @param flag the new value
	 */
	public static void setSound(boolean flag) {
		soundProperty().set(flag);
	}

}
