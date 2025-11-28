package com.comp2042.Logic;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    @BeforeAll
    static void initJfx() {
        // Initialize JavaFX toolkit for MediaPlayer
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @Test
    void testSingletonInstance() {
        SoundManager instance1 = SoundManager.getInstance();
        SoundManager instance2 = SoundManager.getInstance();

        assertNotNull(instance1, "Instance should not be null");
        assertSame(instance1, instance2, "SoundManager should return the same instance (Singleton)");
    }

    @Test
    void testVolumeControl() {
        SoundManager manager = SoundManager.getInstance();

        // Test default
        assertEquals(0.5, manager.getVolume(), 0.001, "Default volume should be 0.5");

        // Test setter
        manager.setVolume(0.8);
        assertEquals(0.8, manager.getVolume(), 0.001, "Volume should update to 0.8");

        // Test lower bound
        manager.setVolume(0.0);
        assertEquals(0.0, manager.getVolume(), 0.001);
    }
}