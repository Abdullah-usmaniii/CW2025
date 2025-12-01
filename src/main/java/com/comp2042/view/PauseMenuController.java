package com.comp2042.view;

import com.comp2042.app.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class PauseMenuController {

    private GuiController gameController;

    @FXML
    private Text highScoreText;

    public void setGameController(GuiController gameController) {
        this.gameController = gameController;
    }

    @FXML
    public void handleResume(ActionEvent event) {
        if (gameController != null) {
            gameController.closePauseMenu();
        }
    }

    @FXML
    public void handleHighScore(ActionEvent event) {
        if (gameController != null && highScoreText != null) {
            // Fetch logic from GuiController
            String score = gameController.getHighScoreText();
            highScoreText.setText("High Score: " + score);

            // Toggle visibility
            highScoreText.setVisible(!highScoreText.isVisible());
        }
    }

    @FXML
    public void handleMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_TITLE_SCREEN));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            // FIX: Swap root to maintain window size
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}