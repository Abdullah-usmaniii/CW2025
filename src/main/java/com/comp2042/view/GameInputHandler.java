package com.comp2042.view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles keyboard input for the game.
 * Uses the Command Pattern to map inputs to actions.
 */
public class GameInputHandler implements EventHandler<KeyEvent> {

    private final GuiController guiController;
    private final Map<KeyCode, Runnable> controlMap = new HashMap<>();

    /**
     * Constructs the input handler and attaches it to the specified input source.
     *
     * @param inputSource   The Node (usually gamePanel) to listen for key presses on.
     * @param guiController The controller to invoke actions on.
     */
    public GameInputHandler(Node inputSource, GuiController guiController) {
        this.guiController = guiController;
        inputSource.setOnKeyPressed(this);
        initializeControls();
    }

    /**
     * Initializes the key bindings using the Command Pattern.
     * Maps KeyCodes to specific Runnable actions.
     */
    private void initializeControls() {
        // Movement Controls
        controlMap.put(KeyCode.LEFT, guiController::moveLeft);
        controlMap.put(KeyCode.A, guiController::moveLeft);

        controlMap.put(KeyCode.RIGHT, guiController::moveRight);
        controlMap.put(KeyCode.D, guiController::moveRight);

        controlMap.put(KeyCode.UP, guiController::rotate);
        controlMap.put(KeyCode.W, guiController::rotate);

        controlMap.put(KeyCode.DOWN, guiController::moveDownUser);
        controlMap.put(KeyCode.S, guiController::moveDownUser);

        // Action Controls
        controlMap.put(KeyCode.SPACE, guiController::hardDrop);
        controlMap.put(KeyCode.TAB, guiController::holdBrick);
        controlMap.put(KeyCode.SHIFT, guiController::holdBrick);
    }

    /**
     * Processes key presses. Checks for global keys (Pause) and game-specific keys.
     *
     * @param keyEvent The key event triggered by the user.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        // 1. Handle Pause Toggling (Global Priority)
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            guiController.togglePause();
            keyEvent.consume();
            return;
        }

        // 2. Handle Game Controls
        // Commands are only executed if the game is active (not paused, not game over)
        if (!guiController.isPaused() && !guiController.isGameOver()) {
            Runnable action = controlMap.get(keyEvent.getCode());
            if (action != null) {
                action.run();
                keyEvent.consume();
            }
        }
    }
}