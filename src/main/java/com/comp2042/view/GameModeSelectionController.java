package com.comp2042.view;

import com.comp2042.app.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the Game Mode Selection screen.
 * Allows the user to choose between "Classic" and "Dig" modes.
 */
public class GameModeSelectionController {

    private TitleScreenController titleScreenController;

    /**
     * Injects the TitleScreenController to allow callbacks for launching the game.
     * @param titleScreenController The main title screen controller.
     */
    public void setTitleScreenController(TitleScreenController titleScreenController) {
        this.titleScreenController = titleScreenController;
    }

    /**
     * Handles the "Classic Mode" button click.
     * Launches the standard game.
     */
    @FXML
    public void handleClassic(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.launchGame(event, GameMode.CLASSIC);
        }
    }

    /**
     * Handles the "Dig Mode" button click.
     * Launches the game with Dig rules (garbage generation).
     */
    @FXML
    public void handleDig(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.launchGame(event, GameMode.DIG);
        }
    }

    /**
     * Handles the "Back" button click.
     * Closes this overlay and returns to the main title menu.
     */
    @FXML
    public void handleBack(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.closeModeSelection();
        }
    }
}