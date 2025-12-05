package com.comp2042.app;

import com.comp2042.Logic.DownData;
import com.comp2042.Logic.ViewData;
import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.view.GuiController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for GameController.
 * Verifies game logic orchestration, event handling, and mode initialization.
 */
class GameControllerTest {

    // 1. Stub Class: Mimics GuiController but does nothing (avoids UI crashes)
    static class StubGuiController extends GuiController {
        boolean initGameViewCalled = false;
        boolean refreshBrickCalled = false;
        boolean refreshBackgroundCalled = false;
        boolean gameOverCalled = false;
        Button mockBombButton = new Button();

        @Override public void setGameSpeed(double rate) {}
        @Override public void updateSpeed(double speed) {}
        @Override public void updateLevel(int level) {}
        @Override public void setEventListener(InputEventListener listener) {}
        @Override public void refreshNextBrick(List<int[][]> data) {}
        @Override public void refreshHoldBrick(int[][] data) {}
        @Override public void updateBombButtonState(int count) {}
        @Override public void showNotification(String text) {}
        @Override public void returnFocusToGame() {}

        @Override
        public void initGameView(int[][] board, ViewData data) {
            initGameViewCalled = true;
        }

        @Override
        public void refreshGameBackground(int[][] board) {
            refreshBackgroundCalled = true;
        }

        @Override
        public void refreshBrick(ViewData data) {
            refreshBrickCalled = true;
        }

        @Override
        public Button getBombButton() {
            return mockBombButton;
        }

        @Override
        public void gameOver() {
            gameOverCalled = true;
        }
    }

    private StubGuiController stubGui;
    private GameController gameController;

    // 2. Initialize JavaFX Toolkit (Required for Button/Properties)
    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        // Initialize Stub on JavaFX thread to avoid thread issues with Button
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            stubGui = new StubGuiController();
            latch.countDown();
        });
        latch.await();
    }


    @Test
    void testConstructorClassicMode() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);

        assertNotNull(gameController.getScore());
        assertTrue(stubGui.initGameViewCalled, "Should initialize game view on start");
        // Ensure bomb button is NOT wired up in Classic mode
        assertNull(stubGui.getBombButton().getOnAction(), "Bomb button should NOT have listener in CLASSIC mode");
    }

    @Test
    void testConstructorBombMode() {
        gameController = new GameController(stubGui, GameMode.BOMB);

        // Ensure bomb button IS wired up in Bomb mode
        assertNotNull(stubGui.getBombButton().getOnAction(), "Bomb button SHOULD have listener in BOMB mode");
    }

    @Test
    void testOnDownEventUserIncreasesScore() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);
        int initialScore = gameController.getScore().scoreProperty().get();

        // Simulate User Down Action
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        DownData result = gameController.onDownEvent(event);

        // Score should increase by 1
        assertEquals(initialScore + 1, gameController.getScore().scoreProperty().get(), "Score should increase by 1 for user soft drop");

        // Ensure data was returned (GuiController calls refresh using this data)
        assertNotNull(result.getViewData(), "Should return view data for the move");
    }

    @Test
    void testOnDownEventThreadDoesNotIncreaseScore() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);
        int initialScore = gameController.getScore().scoreProperty().get();

        // Simulate Thread Down Action
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        gameController.onDownEvent(event);

        // Score should remain same
        assertEquals(initialScore, gameController.getScore().scoreProperty().get(), "Score should NOT increase for gravity drop");
    }

    @Test
    void testMovementEvents() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);

        // Ensure basic movement methods don't crash and return data
        ViewData left = gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        assertNotNull(left);

        ViewData right = gameController.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        assertNotNull(right);

        ViewData rotate = gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        assertNotNull(rotate);
    }

    @Test
    void testHardDrop() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);
        int initialScore = gameController.getScore().scoreProperty().get();

        // Perform Hard Drop
        gameController.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));

        assertTrue(gameController.getScore().scoreProperty().get() > initialScore, "Score should increase after hard drop");
        assertTrue(stubGui.refreshBackgroundCalled, "Background should be refreshed after hard drop lock");
    }

    @Test
    void testCreateNewGame() {
        gameController = new GameController(stubGui, GameMode.CLASSIC);
        gameController.getScore().add(500); // Add some score

        gameController.createNewGame();

        assertEquals(0, gameController.getScore().scoreProperty().get(), "Score should reset to 0");
        assertTrue(stubGui.refreshBackgroundCalled, "Background should refresh");
    }
}