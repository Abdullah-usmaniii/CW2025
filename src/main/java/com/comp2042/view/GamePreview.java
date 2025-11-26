package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GamePreview {

    @FXML private Button newGameButton;
    @FXML private Button highScoreButton;
    @FXML private Text highScoreText;

    private GuiController guiController;

    // No longer needs parent node, GuiController tracks it
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    @FXML
    public void handleNewGame(ActionEvent event) {
        if (guiController != null) {
            // Close preview first, then start new game
            guiController.closePreview();
            guiController.newGame();
        }
    }

    @FXML
    public void handleHighScore(ActionEvent event) {
        if (guiController != null) {
            String score = guiController.getHighScoreText();
            highScoreText.setText("High Score: " + score);
            highScoreText.setVisible(!highScoreText.isVisible());
        }
    }
}