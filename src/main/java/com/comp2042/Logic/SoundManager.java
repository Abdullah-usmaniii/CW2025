package com.comp2042.Logic;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;
    private double volume = 0.5;

    private SoundManager() {
        try {
            // Ensure you have a 'music.mp3' in src/main/resources/
            URL resource = getClass().getClassLoader().getResource("music.mp3");
            if (resource != null) {
                Media sound = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(volume);
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.out.println("Music file not found or could not load.");
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void setVolume(double value) {
        this.volume = value;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(value);
        }
    }

    public double getVolume() {
        return volume;
    }
}