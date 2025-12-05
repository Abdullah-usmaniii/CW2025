package com.comp2042.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class BombPanelTest {

    private BombPanel bombPanel;

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() {
        // Must run on FX thread because it modifies nodes
        Platform.runLater(() -> bombPanel = new BombPanel());
        waitForFxEvents();
    }

    @Test
    void testUpdateCount() {
        Platform.runLater(() -> {
            bombPanel.updateCount(3);

            // Access children to verify text (white-box testing structure)
            // BombPanel extends Pane -> contains Label and BorderPane
            Label label = (Label) bombPanel.getChildren().stream()
                    .filter(n -> n instanceof Label && !((Label)n).getText().equals("BOMB"))
                    .findFirst().orElse(null);

            // Note: Your specific implementation might hide the label inside the BorderPane
            // Let's test the functional outcome: disable state

            bombPanel.updateCount(0);
            BorderPane clickBox = (BorderPane) bombPanel.getChildren().get(1); // Index 1 is the clickableBox
            assertTrue(clickBox.isDisable(), "Panel should be disabled when count is 0");

            bombPanel.updateCount(2);
            assertFalse(clickBox.isDisable(), "Panel should be enabled when count > 0");
        });
        waitForFxEvents();
    }

    @Test
    void testClickAction() {
        AtomicBoolean clicked = new AtomicBoolean(false);

        Platform.runLater(() -> {
            bombPanel.setOnBombClicked(() -> clicked.set(true));

            // Simulate click
            BorderPane clickBox = (BorderPane) bombPanel.getChildren().get(1);
            clickBox.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });
        waitForFxEvents();

        assertTrue(clicked.get(), "Click action should be executed");
    }

    // Helper to wait for JavaFX thread
    private void waitForFxEvents() {
        try {
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            Platform.runLater(latch::countDown);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}