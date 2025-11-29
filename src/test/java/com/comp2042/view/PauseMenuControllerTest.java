package com.comp2042.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class PauseMenuControllerTest {

    private PauseMenuController pauseController;
    private StubGuiController stubGameController;
    private Text mockHighScoreText;

    // Stub for GuiController to verify method calls
    static class StubGuiController extends GuiController {
        boolean closePauseMenuCalled = false;

        @Override
        public void closePauseMenu() {
            closePauseMenuCalled = true;
        }

        @Override
        public String getHighScoreText() {
            return "9999";
        }
    }

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() throws Exception {
        pauseController = new PauseMenuController();
        stubGameController = new StubGuiController();
        pauseController.setGameController(stubGameController);

        mockHighScoreText = new Text();
        mockHighScoreText.setVisible(false);

        Field field = PauseMenuController.class.getDeclaredField("highScoreText");
        field.setAccessible(true);
        field.set(pauseController, mockHighScoreText);
    }

    @Test
    void testHandleResume() {
        pauseController.handleResume(new ActionEvent());
        assertTrue(stubGameController.closePauseMenuCalled, "Resume should call closePauseMenu on game controller");
    }

    @Test
    void testHandleHighScore() {
        // Initial State
        assertFalse(mockHighScoreText.isVisible());

        // First Click
        pauseController.handleHighScore(new ActionEvent());

        assertTrue(mockHighScoreText.isVisible(), "Text should be visible");
        assertEquals("High Score: 9999", mockHighScoreText.getText(), "Text should display correct score");

        // Second Click
        pauseController.handleHighScore(new ActionEvent());
        assertFalse(mockHighScoreText.isVisible(), "Text should hide");
    }
}