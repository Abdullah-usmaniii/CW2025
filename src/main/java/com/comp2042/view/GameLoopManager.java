package com.comp2042.view;

import com.comp2042.app.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game loop (gravity) using a JavaFX Timeline.
 * Responsible for triggering the periodic "move down" event.
 */
public class GameLoopManager {

    private Timeline timeLine;
    private final Runnable tickAction;

    /**
     * Constructs the GameLoopManager.
     *
     * @param tickAction A Runnable representing the action to perform on every tick (e.g., gravity).
     */
    public GameLoopManager(Runnable tickAction) {
        this.tickAction = tickAction;
        initTimeline();
    }

    /**
     * Initializes the Timeline with a 400ms interval.
     */
    private void initTimeline() {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(Constants.GAME_SPEED_MILLIS),
                ae -> tickAction.run()
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the game loop.
     */
    public void play() {
        if (timeLine != null) {
            timeLine.play();
        }
    }

    /**
     * Pauses the game loop.
     */
    public void pause() {
        if (timeLine != null) {
            timeLine.pause();
        }
    }

    /**
     * Stops the game loop completely.
     */
    public void stop() {
        if (timeLine != null) {
            timeLine.stop();
        }
    }
    /**
     * Sets the speed multiplier for the game loop.
     * @param rate 1.0 is normal speed, 2.0 is double speed.
     */
    public void setRate(double rate) {
        if (timeLine != null) {
            timeLine.setRate(rate);
        }
    }
}