package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the Instructions screen.
 * Handles user interactions within the instructions view, primarily navigating back to the title screen.
 * @author Abdullah Usmani
 */
public class InstructionsController {

    private TitleScreenController titleScreenController;

    /**
     * Injects the main TitleScreenController into this controller.
     * This allows the instructions screen to communicate back to the title screen (e.g., to close itself).
     *
     * @param titleScreenController The instance of the main title screen controller.
     */
    public void setTitleScreenController(TitleScreenController titleScreenController) {
        this.titleScreenController = titleScreenController;
    }

    /**
     * Handles the "Back" button click event.
     * Closes the instructions overlay and returns to the title screen.
     *
     * @param event The ActionEvent triggered by clicking the back button.
     */
    @FXML
    public void handleBack(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.closeInstructions();
        }
    }
}