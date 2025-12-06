package com.comp2042.Logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.prefs.Preferences;

/**
 * Manages the player's current score and high score persistence.
 * Uses JavaFX properties to allow the UI to observe changes.
 *
 * @author Abdullah Usmani
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final Preferences prefs = Preferences.userNodeForPackage(Score.class);

    /**
     * Constructs a Score object, loading the high score from user preferences.
     */
    public Score() {
        // Load high score from preferences
        highScore.setValue(prefs.getInt("highScore", 0));

        // Add listener to update high score when current score changes
        score.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > highScore.getValue()) {
                highScore.setValue(newVal.intValue());
                // Save high score to preferences
                prefs.putInt("highScore", newVal.intValue());
            }
        });
    }

    /**
     * Gets the observable score property.
     * @return The IntegerProperty for the current score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the observable high score property.
     * @return The IntegerProperty for the high score.
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    /**
     * Adds points to the current score.
     * @param i The amount of points to add.
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the current score to zero.
     */
    public void reset() {
        score.setValue(0);
    }

    /**
     * Gets the current high score value.
     * @return The integer value of the high score.
     */
    public int getHighScore() {
        return highScore.getValue();
    }
}