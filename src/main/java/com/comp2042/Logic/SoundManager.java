package com.comp2042.Logic;

import javafx.scene.media.AudioClip; // Use AudioClip for SFX
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer musicPlayer; // Keep MediaPlayer for long Background Music
    private double musicVolume = 0.5;
    private final double SFX_VOLUME = 0.7;

    // Change Media to AudioClip for better performance on short sounds
    private AudioClip placeSound;
    private AudioClip clearSound;

    private SoundManager() {
        try {
            // Background Music (Keep as MediaPlayer)
            URL musicResource = getClass().getClassLoader().getResource("music.mp3");
            if (musicResource != null) {
                Media sound = new Media(musicResource.toExternalForm());
                musicPlayer = new MediaPlayer(sound);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.setVolume(musicVolume);
                musicPlayer.play();
            }

            // Sound Effects (Change to AudioClip)
            // AudioClip pre-loads raw audio data into memory for instant playback
            URL placeResource = getClass().getClassLoader().getResource("place.mp3");
            if (placeResource != null) {
                placeSound = new AudioClip(placeResource.toExternalForm());
            }

            URL clearResource = getClass().getClassLoader().getResource("clear.mp3");
            if (clearResource != null) {
                clearSound = new AudioClip(clearResource.toExternalForm());
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

    public void setVolume(double value) {
        this.musicVolume = value;
        if (musicPlayer != null) {
            musicPlayer.setVolume(value);
        }
    }

    public double getVolume() {
        return musicVolume;
    }

    // Updated methods to use AudioClip
    public void playPlaceSound() {
        if (placeSound != null) {
            placeSound.play(SFX_VOLUME); // Play with specific volume, no need to dispose
        }
    }

    public void playClearSound() {
        if (clearSound != null) {
            clearSound.play(SFX_VOLUME);
        }
    }
}