package com.comp2042.Logic;

import com.comp2042.app.Constants;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Manages the audio system for the game, including background music and sound effects.
 * Implements the Singleton pattern to ensure a single audio controller exists.
 *
 * @author Abdullah Usmani
 */
public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private double musicVolume = Constants.DEFAULT_MUSIC_VOLUME;
    private final double SFX_VOLUME = Constants.SFX_VOLUME;

    // Change Media to AudioClip for better performance on short sounds
    private AudioClip placeSound;
    private AudioClip clearSound;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the background music player and loads sound effects into memory.
     */
    private SoundManager() {
        try {
            // Background Music (Keep as MediaPlayer)
            URL musicResource = getClass().getClassLoader().getResource(Constants.RESOURCE_MUSIC);
            if (musicResource != null) {
                Media sound = new Media(musicResource.toExternalForm());
                musicPlayer = new MediaPlayer(sound);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.setVolume(musicVolume);
                musicPlayer.play();
            }

            // AudioClip pre-loads raw audio data into memory for instant playback
            URL placeResource = getClass().getClassLoader().getResource(Constants.RESOURCE_SOUND_PLACE);
            if (placeResource != null) {
                placeSound = new AudioClip(placeResource.toExternalForm());
            }

            URL clearResource = getClass().getClassLoader().getResource(Constants.RESOURCE_SOUND_CLEAR);
            if (clearResource != null) {
                clearSound = new AudioClip(clearResource.toExternalForm());
            }

        } catch (Exception e) {
            System.out.println("Music file or sound effect not found or could not load.");
        }
    }

    /**
     * Retrieves the single instance of the SoundManager.
     * Creates the instance if it does not already exist.
     *
     * @return The singleton SoundManager instance.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Sets the volume for the background music.
     *
     * @param value The new volume level (0.0 to 1.0).
     */
    public void setVolume(double value) {
        this.musicVolume = value;
        if (musicPlayer != null) {
            musicPlayer.setVolume(value);
        }
    }

    /**
     * Gets the current volume level of the background music.
     *
     * @return The volume level (0.0 to 1.0).
     */
    public double getVolume() {
        return musicVolume;
    }

    /**
     * Plays the sound effect for placing a brick.
     * Uses the predefined SFX volume constant.
     */
    public void playPlaceSound() {
        if (placeSound != null) {
            placeSound.play(SFX_VOLUME);
        }
    }

    /**
     * Plays the sound effect for clearing rows.
     * Uses the predefined SFX volume constant.
     */
    public void playClearSound() {
        if (clearSound != null) {
            clearSound.play(SFX_VOLUME);
        }
    }
}