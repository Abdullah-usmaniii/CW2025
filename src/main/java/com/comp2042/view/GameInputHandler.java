package com.comp2042.view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard input for the game.
 * Translates raw KeyEvents into game actions and delegates them to the GuiController.
 */
public class GameInputHandler implements EventHandler<KeyEvent> {

    private final GuiController guiController;

    /**
     * Constructs the input handler and attaches it to the specified input source.
     *
     * @param inputSource   The Node (usually gamePanel) to listen for key presses on.
     * @param guiController The controller to invoke actions on.
     */
    public GameInputHandler(Node inputSource, GuiController guiController) {
        this.guiController = guiController;
        // Attach this handler to the game panel
        inputSource.setOnKeyPressed(this);
    }

    /**
     * Processes key presses. Checks for global keys (Pause) and game-specific keys.
     *
     * @param keyEvent The key event triggered by the user.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        // 1. Handle Pause Toggling (Always available)
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            guiController.togglePause();
            keyEvent.consume();
            return;
        }

        // 2. Handle Game Controls (Only if playing)
        if (!guiController.isPaused() && !guiController.isGameOver()) {
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                guiController.moveLeft();
                keyEvent.consume();
            }
            else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                guiController.moveRight();
                keyEvent.consume();
            }
            else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                guiController.rotate();
                keyEvent.consume();
            }
            else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                guiController.moveDownUser(); // Differentiates User input from Gravity
                keyEvent.consume();
            }
            else if (keyEvent.getCode() == KeyCode.SPACE) {
                guiController.hardDrop();
                keyEvent.consume();
            }
            else if (keyEvent.getCode() == KeyCode.TAB || keyEvent.getCode() == KeyCode.SHIFT) {
                guiController.holdBrick();
                keyEvent.consume();
            }
        }
    }
}