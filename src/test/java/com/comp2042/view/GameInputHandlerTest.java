package com.comp2042.view;

import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInputHandlerTest {

    // Stub class to capture method calls
    static class StubGuiController extends GuiController {
        boolean leftCalled = false;
        boolean rightCalled = false;
        boolean rotateCalled = false;
        boolean downCalled = false;
        boolean hardDropCalled = false;
        boolean holdCalled = false;
        boolean pauseCalled = false;

        @Override
        public void moveLeft() { leftCalled = true; }
        @Override
        public void moveRight() { rightCalled = true; }
        @Override
        public void rotate() { rotateCalled = true; }
        @Override
        public void moveDownUser() { downCalled = true; }
        @Override
        public void hardDrop() { hardDropCalled = true; }
        @Override
        public void holdBrick() { holdCalled = true; }
        @Override
        public void togglePause() { pauseCalled = true; }

        // Override state checks to allow inputs to pass through
        @Override
        public boolean isPaused() { return false; }
        @Override
        public boolean isGameOver() { return false; }
        @Override
        public boolean isCountingDown() { return false; }
    }

    private GameInputHandler inputHandler;
    private StubGuiController stubController;
    private Pane eventSource;

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() {
        stubController = new StubGuiController();
        eventSource = new Pane();
        inputHandler = new GameInputHandler(eventSource, stubController);
    }

    private void fireKey(KeyCode code) {
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                false, false, false, false);
        inputHandler.handle(event);
    }

    @Test
    void testMovementKeys() {
        fireKey(KeyCode.LEFT);
        assertTrue(stubController.leftCalled, "Left key should trigger moveLeft");

        fireKey(KeyCode.RIGHT);
        assertTrue(stubController.rightCalled, "Right key should trigger moveRight");

        fireKey(KeyCode.UP);
        assertTrue(stubController.rotateCalled, "Up key should trigger rotate");

        fireKey(KeyCode.DOWN);
        assertTrue(stubController.downCalled, "Down key should trigger moveDownUser");
    }

    @Test
    void testActionKeys() {
        fireKey(KeyCode.SPACE);
        assertTrue(stubController.hardDropCalled, "Space key should trigger hardDrop");

        fireKey(KeyCode.TAB);
        assertTrue(stubController.holdCalled, "Tab key should trigger holdBrick");
    }

    @Test
    void testPauseKey() {
        fireKey(KeyCode.ESCAPE);
        assertTrue(stubController.pauseCalled, "Escape key should trigger togglePause");
    }
}