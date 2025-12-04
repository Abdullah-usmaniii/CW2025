package com.comp2042.Logic;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerTest {

    private LevelManager levelManager;
    private Score score;
    private AtomicInteger capturedLevel;
    private AtomicReference<Double> capturedSpeed;

    @BeforeAll
    static void initJfx() {
        // Initialize JavaFX toolkit if necessary for properties,
        // though SimpleIntegerProperty usually works without it.
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @BeforeEach
    void setUp() {
        // Initialize capture containers for callbacks
        capturedLevel = new AtomicInteger(0);
        capturedSpeed = new AtomicReference<>(0.0);

        // Initialize Score
        // Note: This relies on Score constructor not failing on Preferences
        score = new Score();

        // Initialize LevelManager with callbacks that capture the values
        levelManager = new LevelManager(
                score,
                newLevel -> capturedLevel.set(newLevel),
                newSpeed -> capturedSpeed.set(newSpeed)
        );
    }

    @Test
    void testInitialState() {
        // Upon initialization, it should trigger an update to Level 1, Speed 1.0
        assertEquals(1, levelManager.getCurrentLevel(), "Initial level should be 1");
        assertEquals(1, capturedLevel.get(), "Callback should receive level 1");
        assertEquals(1.0, capturedSpeed.get(), 0.001, "Callback should receive speed 1.0");
    }

    @Test
    void testProgressionToLevel2() {
        // Score 0 -> 499 should stay Level 1
        score.add(499);
        assertEquals(1, levelManager.getCurrentLevel());

        // Score 500 should trigger Level 2
        score.add(1); // Total 500
        assertEquals(2, levelManager.getCurrentLevel(), "Should be Level 2 at 500 points");
        assertEquals(2, capturedLevel.get());
        assertEquals(1.25, capturedSpeed.get(), 0.001, "Speed should be 1.25 at Level 2");
    }

    @Test
    void testProgressionToLevel3() {
        // Jump to 1000 points
        score.add(1000);
        assertEquals(3, levelManager.getCurrentLevel(), "Should be Level 3 at 1000 points");
        assertEquals(1.5, capturedSpeed.get(), 0.001, "Speed should be 1.5 at Level 3");
    }

    @Test
    void testMaxLevelCap() {
        // Max Level is 5 (starts at 2500 points)
        // Add huge score to exceed max level threshold
        score.add(5000);

        assertEquals(5, levelManager.getCurrentLevel(), "Level should be capped at 5");
        assertEquals(2.0, capturedSpeed.get(), 0.001, "Speed should be capped at 2.0");
    }

    @Test
    void testReset() {
        // Advance to Level 3
        score.add(1000);
        assertEquals(3, levelManager.getCurrentLevel());

        // Perform Reset
        levelManager.reset();

        assertEquals(1, levelManager.getCurrentLevel(), "Should reset to Level 1");
        assertEquals(1, capturedLevel.get(), "Callback should reflect Level 1");
        assertEquals(1.0, capturedSpeed.get(), 0.001, "Callback should reflect Speed 1.0");
    }

    @Test
    void testCallbackExecutionOnScoreChange() {
        // Ensure callbacks are fired exactly when level changes
        // Initial state is 1

        // Add 100 points (Still Level 1)
        // LevelManager checks if (newLevel != currentLevel) before firing callbacks
        // We reset our capture variables to detect if they are fired unnecessarily
        capturedLevel.set(-1);
        score.add(100);

        // Since level didn't change (still 1), callbacks should NOT have fired (or logic depends on impl)
        // Looking at LevelManager code: if (newLevel != currentLevel) -> update.
        // So they should strictly NOT fire if level stays same.
        assertEquals(-1, capturedLevel.get(), "Callback should not fire if level does not change");

        // Add 400 points (Total 500 -> Level 2)
        score.add(400);
        assertEquals(2, capturedLevel.get(), "Callback should fire when level changes to 2");
    }
}