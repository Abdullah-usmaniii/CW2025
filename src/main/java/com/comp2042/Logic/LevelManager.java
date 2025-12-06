package com.comp2042.Logic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.function.Consumer;
import com.comp2042.app.Constants;

/**
 * Manages game levels based on the player's score.
 * Updates the game speed as levels progress by observing the score property.
 *
 * @author Abdullah Usmani
 */
public class LevelManager implements ChangeListener<Number> {

    private int currentLevel = 1;
    private final Consumer<Integer> levelUpdateCallback;
    private final Consumer<Double> speedUpdateCallback;

    /**
     * Constructs a LevelManager to observe the score and trigger updates.
     *
     * @param score               The Score object to observe.
     * @param levelUpdateCallback A consumer that accepts the new level integer.
     * @param speedUpdateCallback A consumer that accepts the new speed rate multiplier.
     */
    public LevelManager(Score score, Consumer<Integer> levelUpdateCallback, Consumer<Double> speedUpdateCallback) {
        this.levelUpdateCallback = levelUpdateCallback;
        this.speedUpdateCallback = speedUpdateCallback;
        score.scoreProperty().addListener(this);

        // Initial update
        updateLevelAndSpeed();
    }

    /**
     * Responds to changes in the observed score property.
     * Called automatically whenever the score changes.
     *
     * @param observable The observable value being changed.
     * @param oldValue   The old score value.
     * @param newValue   The new score value.
     */
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int score = newValue.intValue();
        calculateLevel(score);
    }

    /**
     * Calculates the current level based on the total score.
     * Limits the level to the maximum defined in Constants.
     *
     * @param score The current score.
     */
    private void calculateLevel(int score) {
        // Level 1: 0-499, Level 2: 500-999, etc.
        int newLevel = (score / Constants.SCORE_PER_LEVEL) + 1;

        // Cap at Max Level
        if (newLevel > Constants.MAX_LEVEL) {
            newLevel = Constants.MAX_LEVEL;
        }

        if (newLevel != currentLevel) {
            currentLevel = newLevel;
            updateLevelAndSpeed();
        }
    }

    /**
     * Updates the level and speed via the registered callbacks.
     * Speed increases by a fixed rate per level.
     */
    private void updateLevelAndSpeed() {
        // Base rate is 1.0. Increase by 0.25 per level.
        double rate = 1.0 + ((currentLevel - 1) * Constants.SPEED_INCREASE_PER_LEVEL);

        if (levelUpdateCallback != null) {
            levelUpdateCallback.accept(currentLevel);
        }
        if (speedUpdateCallback != null) {
            speedUpdateCallback.accept(rate);
        }
    }

    /**
     * Resets the level manager to the initial state (Level 1).
     */
    public void reset() {
        currentLevel = 1;
        updateLevelAndSpeed();
    }

    /**
     * Gets the current level number.
     *
     * @return The current level (starts at 1).
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
}