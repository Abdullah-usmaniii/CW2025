package com.comp2042.Logic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.function.Consumer;

/**
 * Manages game levels based on the player's score.
 * Updates the game speed as levels progress.
 */
public class LevelManager implements ChangeListener<Number> {

    private int currentLevel = 1;
    private final Consumer<Integer> levelUpdateCallback;
    private final Consumer<Double> speedUpdateCallback;
    private static final int SCORE_PER_LEVEL = 500;
    private static final int MAX_LEVEL = 5;
    private static final double SPEED_INCREMENT = 0.25;

    /**
     * @param score The Score object to observe.
     * @param levelUpdateCallback A consumer that accepts the new level.
     * @param speedUpdateCallback A consumer that accepts the new speed rate multiplier.
     */
    public LevelManager(Score score, Consumer<Integer> levelUpdateCallback, Consumer<Double> speedUpdateCallback) {
        this.levelUpdateCallback = levelUpdateCallback;
        this.speedUpdateCallback = speedUpdateCallback;
        score.scoreProperty().addListener(this);

        // Initial update
        updateLevelAndSpeed();
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int score = newValue.intValue();
        calculateLevel(score);
    }

    private void calculateLevel(int score) {
        // Level 1: 0-499, Level 2: 500-999, etc.
        int newLevel = (score / SCORE_PER_LEVEL) + 1;

        // Cap at Max Level
        if (newLevel > MAX_LEVEL) {
            newLevel = MAX_LEVEL;
        }

        if (newLevel != currentLevel) {
            currentLevel = newLevel;
            updateLevelAndSpeed();
        }
    }

    private void updateLevelAndSpeed() {
        // Base rate is 1.0. Increase by 0.25 per level.
        double rate = 1.0 + ((currentLevel - 1) * SPEED_INCREMENT);

        if (levelUpdateCallback != null) {
            levelUpdateCallback.accept(currentLevel);
        }
        if (speedUpdateCallback != null) {
            speedUpdateCallback.accept(rate);
        }
    }

    public void reset() {
        currentLevel = 1;
        updateLevelAndSpeed();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}