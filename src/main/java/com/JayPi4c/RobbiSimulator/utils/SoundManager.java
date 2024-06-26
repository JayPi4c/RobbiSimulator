package com.JayPi4c.RobbiSimulator.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;

/**
 * Manager to load and play sounds.
 *
 * @author Jonas Pohl
 */
@Slf4j
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
                logger.error("Could not load sound file.", e);
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
