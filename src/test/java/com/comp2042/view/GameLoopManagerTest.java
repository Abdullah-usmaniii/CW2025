package com.comp2042.view;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLoopManagerTest {

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @Test
    void testLifecycleMethods() {
        Runnable dummyAction = () -> {};
        GameLoopManager loopManager = new GameLoopManager(dummyAction);

        // Verify methods don't throw exceptions
        assertDoesNotThrow(loopManager::play);
        assertDoesNotThrow(loopManager::pause);
        assertDoesNotThrow(loopManager::stop);
    }

    @Test
    void testSetRate() {
        Runnable dummyAction = () -> {};
        GameLoopManager loopManager = new GameLoopManager(dummyAction);

        assertDoesNotThrow(() -> loopManager.setRate(1.5));
    }
}