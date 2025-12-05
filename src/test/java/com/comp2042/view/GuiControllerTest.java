package com.comp2042.view;

import com.comp2042.app.GameMode;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GuiControllerTest {

    private GuiController controller;

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new GuiController();

        // Inject mock FXML fields
        setPrivateField(controller, "rootPane", new StackPane(new Pane())); // Needs child for Bomb button injection
        setPrivateField(controller, "gamePanel", new GridPane());
        setPrivateField(controller, "brickPanel", new GridPane());
        setPrivateField(controller, "ghostPanel", new GridPane());
        setPrivateField(controller, "nextBrick", new GridPane());
        setPrivateField(controller, "holdBrick", new GridPane());
        setPrivateField(controller, "groupNotification", new Group());
        setPrivateField(controller, "scoreValue", new Text());
        setPrivateField(controller, "levelValue", new Text());
        setPrivateField(controller, "speedValue", new Text());
        setPrivateField(controller, "gameOverPanel", new GameOverPanel());
        setPrivateField(controller, "countdownText", new Text());

        // Run initialize to set up renderer etc.
        Platform.runLater(() -> controller.initialize(null, null));
        waitForFxEvents();
    }

    @Test
    void testSetGameModeBombCreatesButton() {
        Platform.runLater(() -> {
            controller.setGameMode(GameMode.BOMB);
            assertNotNull(controller.getBombButton(), "Bomb button should be created in BOMB mode");
        });
        waitForFxEvents();
    }

    @Test
    void testSetGameModeClassicNoButton() {
        Platform.runLater(() -> {
            controller.setGameMode(GameMode.CLASSIC);
            assertNull(controller.getBombButton(), "Bomb button should NOT be created in CLASSIC mode");
        });
        waitForFxEvents();
    }

    @Test
    void testUpdateBombButtonState() {
        Platform.runLater(() -> {
            controller.setGameMode(GameMode.BOMB);

            controller.updateBombButtonState(0);
            assertTrue(controller.getBombButton().isDisable(), "Button should be disabled if inventory is 0");
            assertEquals("EMPTY", controller.getBombButton().getText());

            controller.updateBombButtonState(2);
            assertFalse(controller.getBombButton().isDisable(), "Button should be enabled if inventory > 0");
        });
        waitForFxEvents();
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void waitForFxEvents() {
        try {
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            Platform.runLater(latch::countDown);
            latch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}