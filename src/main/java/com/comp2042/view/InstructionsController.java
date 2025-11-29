package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InstructionsController {

    private TitleScreenController titleScreenController;

    // Allow the main controller to inject itself
    public void setTitleScreenController(TitleScreenController titleScreenController) {
        this.titleScreenController = titleScreenController;
    }

    @FXML
    public void handleBack(ActionEvent event) {
        if (titleScreenController != null) {
            titleScreenController.closeInstructions();
        }
    }
}