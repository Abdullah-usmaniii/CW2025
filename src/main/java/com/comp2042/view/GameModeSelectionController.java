package com.comp2042.view;

import com.comp2042.app.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the Game Mode Selection screen.
 * Handles user interaction for choosing between Classic, Dig, and Bomb Squad modes.
 */
public class GameModeSelectionController {

    private TitleScreenController titleScreenController;

    /**
     * Injects the main title screen controller to allow navigation callbacks.
     *
     * @param titleScreenController The parent controller.
     */
    public void setTitleScreenController(TitleScreenController titleScreenController) {
        this.titleScreenController = titleScreenController;
    }

    /**
     * Launches the Classic game mode.
     * @param event The button click event.
     */
    @FXML
    public void handleClassic(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.launchGame(event, GameMode.CLASSIC);
        }
    }

    /**
     * Launches the Dig game mode.
     * @param event The button click event.
     */
    @FXML
    public void handleDig(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.launchGame(event, GameMode.DIG);
        }
    }

    /**
     * Launches the Bomb Squad game mode.
     * @param event The button click event.
     */
    @FXML
    public void handleBomb(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.launchGame(event, GameMode.BOMB);
        }
    }

    /**
     * Returns to the main title screen.
     * @param event The button click event.
     */
    @FXML
    public void handleBack(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.closeModeSelection();
        }
    }
}