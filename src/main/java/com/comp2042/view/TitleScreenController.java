package com.comp2042.view;

import com.comp2042.Logic.SoundManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class TitleScreenController {

    @FXML private StackPane rootPane; // Reference to the root stack pane
    @FXML private VBox settingsPanel;
    @FXML private Slider volumeSlider;

    private Parent instructionsOverlay;

    @FXML
    public void initialize() {
        SoundManager soundManager = SoundManager.getInstance();
        if (volumeSlider != null) {
            volumeSlider.setValue(soundManager.getVolume());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                soundManager.setVolume(newVal.doubleValue());
            });
        }
        if (settingsPanel != null) {
            settingsPanel.setVisible(false);
            settingsPanel.setManaged(false);
        }
    }

    @FXML
    public void handleStartGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Parent root = loader.load();

            // 1. Get the current Stage and Scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Setup controller logic
            GuiController controller = loader.getController();
            com.comp2042.app.GameController gameLogic = new com.comp2042.app.GameController(controller);
            controller.bindScore(gameLogic.getScore().scoreProperty());
            controller.bindHighScore(gameLogic.getScore().highScoreProperty());

            // 2. FIX: Swap the root instead of setting a new Scene
            // This keeps the window maximized and prevents shrinking
            currentScene.setRoot(root);

            controller.newGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        boolean isVisible = settingsPanel.isVisible();
        settingsPanel.setVisible(!isVisible);
        settingsPanel.setManaged(!isVisible);
    }

    @FXML
    public void handleInstructions(ActionEvent event) {
        if (instructionsOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Instructions.fxml"));
                instructionsOverlay = loader.load();

                // Pass this controller to the instructions controller so it can call closeInstructions()
                InstructionsController controller = loader.getController();
                controller.setTitleScreenController(this);

                rootPane.getChildren().add(instructionsOverlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If already loaded, just add it back if it was removed
            if (!rootPane.getChildren().contains(instructionsOverlay)) {
                rootPane.getChildren().add(instructionsOverlay);
            }
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void closeInstructions() {
        if (rootPane != null && instructionsOverlay != null) {
            rootPane.getChildren().remove(instructionsOverlay);
        }
    }
}