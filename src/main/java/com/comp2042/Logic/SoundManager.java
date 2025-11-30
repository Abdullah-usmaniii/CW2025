package com.comp2042.Logic;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private double musicVolume = 0.5; // Controls the background music volume

    // Fixed volume level for sound effects, typically set high or to a default value
    private final double SFX_VOLUME = 0.7;

    // Media fields for sound effects
    private Media placeSound;
    private Media clearSound;

    private SoundManager() {
        try {
            // Background Music Setup
            URL musicResource = getClass().getClassLoader().getResource("music.mp3");
            if (musicResource != null) {
                Media sound = new Media(musicResource.toExternalForm());
                musicPlayer = new MediaPlayer(sound);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.setVolume(musicVolume); // Uses the adjustable volume
                musicPlayer.play();
            }

            // Load Sound Effects
            URL placeResource = getClass().getClassLoader().getResource("place.mp3");
            if (placeResource != null) {
                placeSound = new Media(placeResource.toExternalForm());
            }

            URL clearResource = getClass().getClassLoader().getResource("clear.mp3");
            if (clearResource != null) {
                clearSound = new Media(clearResource.toExternalForm());
            }

        } catch (Exception e) {
            System.out.println("Music file or sound effect not found or could not load.");
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }


    //  Sets the volume for the background music only.

    public void setVolume(double value) {
        this.musicVolume = value;
        if (musicPlayer != null) {
            musicPlayer.setVolume(value);
        }
    }

    //Gets the current background music volume.

    public double getVolume() {
        return musicVolume;
    }

    // New method to play a sound effect
    private void playEffect(Media soundMedia) {
        if (soundMedia != null) {
            MediaPlayer effectPlayer = new MediaPlayer(soundMedia);
            effectPlayer.setVolume(SFX_VOLUME);
            effectPlayer.play();
            // Dispose player after it finishes to free resources
            effectPlayer.setOnEndOfMedia(() -> effectPlayer.dispose());
        }
    }

    // Public methods to trigger place and clear sound effects
    public void playPlaceSound() {
        playEffect(placeSound);
    }

    public void playClearSound() {
        playEffect(clearSound);
    }
}