package com.comp2042.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GamePreviewTest {

    private GamePreview gamePreview;
    private StubGuiController stubGuiController;
    private Text mockHighScoreText;


    static class StubGuiController extends GuiController {
        boolean newGameCalled = false;
        boolean closePreviewCalled = false;

        @Override
        public void newGame() {
            newGameCalled = true;
        }

        @Override
        public void closePreview() {
            closePreviewCalled = true;
        }

        @Override
        public String getHighScoreText() {
            return "5000";
        }
    }

    @BeforeAll
    static void initJfx() {
        // Initialize JavaFX toolkit to prevent exceptions when creating new Text() objects
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized, ignore
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        gamePreview = new GamePreview();
        stubGuiController = new StubGuiController();
        gamePreview.setGuiController(stubGuiController);

        // Manually inject the FXML Text node using reflection
        // This is necessary because @FXML injection doesn't happen in unit tests
        mockHighScoreText = new Text();
        mockHighScoreText.setVisible(false); // Start hidden

        Field textField = GamePreview.class.getDeclaredField("highScoreText");
        textField.setAccessible(true);
        textField.set(gamePreview, mockHighScoreText);
    }

    @Test
    void testHandleNewGame() {
        // Trigger the New Game action
        gamePreview.handleNewGame(new ActionEvent());

        // Assert that the proper controller methods were called
        assertTrue(stubGuiController.closePreviewCalled, "Preview window should be closed");
        assertTrue(stubGuiController.newGameCalled, "New game logic should be triggered");
    }

    @Test
    void testHandleHighScoreUpdatesText() {
        // Trigger the High Score action
        gamePreview.handleHighScore(new ActionEvent());

        // Assert text matches "High Score: " + the value from our stub
        assertEquals("High Score: 5000", mockHighScoreText.getText(),
                "Text should update to display the correct score");
    }

    @Test
    void testHandleHighScoreTogglesVisibility() {
        // 1. Initial state should be hidden
        assertFalse(mockHighScoreText.isVisible(), "Text should initially be hidden");

        // 2. First click -> Should become Visible
        gamePreview.handleHighScore(new ActionEvent());
        assertTrue(mockHighScoreText.isVisible(), "Text should become visible after first click");

        // 3. Second click -> Should become Hidden
        gamePreview.handleHighScore(new ActionEvent());
        assertFalse(mockHighScoreText.isVisible(), "Text should become hidden after second click");
    }

    @Test
    void testNullControllerSafety() {
        // Ensure nothing crashes if the controller isn't set
        GamePreview detachedPreview = new GamePreview();

        assertDoesNotThrow(() -> detachedPreview.handleNewGame(new ActionEvent()));
        assertDoesNotThrow(() -> detachedPreview.handleHighScore(new ActionEvent()));
    }
}