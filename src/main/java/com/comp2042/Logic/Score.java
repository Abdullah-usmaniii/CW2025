package com.comp2042.Logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.prefs.Preferences;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final Preferences prefs = Preferences.userNodeForPackage(Score.class);

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

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
    }

    public int getHighScore() {
        return highScore.getValue();
    }
}
