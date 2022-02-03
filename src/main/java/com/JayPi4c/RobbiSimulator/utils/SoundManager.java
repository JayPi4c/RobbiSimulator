package com.JayPi4c.RobbiSimulator.utils;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manager to load and play sounds.
 * 
 * @author Jonas Pohl
 *
 */
public class SoundManager {

	private static Media WARNING_SOUND = null;

	/**
	 * Plays a warning sounds. If the sound is not loaded yet, the sound will be
	 * loaded and stored for further plays.
	 */
	public static void playWarnSound() {
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

}
