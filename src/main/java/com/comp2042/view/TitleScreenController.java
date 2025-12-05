/**
 * src/main/java/com/comp2042/view/TitleScreenController.java
 */
package com.comp2042.view;

import com.comp2042.Logic.SoundManager;
import com.comp2042.app.Constants;
import com.comp2042.app.GameMode;
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

/**
 * Controller for the main Title Screen.
 * Manages navigation to the game, settings, instructions, and game mode selection.
 */
public class TitleScreenController {

    @FXML private StackPane rootPane;
    @FXML private VBox settingsPanel;
    @FXML private Slider volumeSlider;

    private Parent instructionsOverlay;
    private Parent modeSelectionOverlay; // Added to hold the new window

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

    /**
     * Handles the "Start Game" button click.
     * Displays the Game Mode Selection window instead of launching immediately.
     */
    @FXML
    public void handleStartGame(ActionEvent event) {
        if (modeSelectionOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameModeSelection.fxml"));
                modeSelectionOverlay = loader.load();

                // Inject this controller into the selection controller
                GameModeSelectionController controller = loader.getController();
                controller.setTitleScreenController(this);

                rootPane.getChildren().add(modeSelectionOverlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!rootPane.getChildren().contains(modeSelectionOverlay)) {
                rootPane.getChildren().add(modeSelectionOverlay);
            }
        }
    }

    /**
     * Launches the game with the specific Game Mode.
     * Called by the GameModeSelectionController.
     *
     * @param event The event source (button click) used to find the stage.
     * @param mode  The selected game mode (CLASSIC or DIG).
     */
    public void launchGame(ActionEvent event, GameMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_GAME_LAYOUT));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Initialize logic with selected mode
            GuiController controller = loader.getController();

            // Pass the mode to GameController
            com.comp2042.app.GameController gameLogic = new com.comp2042.app.GameController(controller, mode);

            controller.bindScore(gameLogic.getScore().scoreProperty());
            controller.bindHighScore(gameLogic.getScore().highScoreProperty());

            currentScene.setRoot(root);
            controller.newGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the Game Mode Selection overlay.
     */
    public void closeModeSelection() {
        if (rootPane != null && modeSelectionOverlay != null) {
            rootPane.getChildren().remove(modeSelectionOverlay);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_INSTRUCTIONS));
                instructionsOverlay = loader.load();
                InstructionsController controller = loader.getController();
                controller.setTitleScreenController(this);
                rootPane.getChildren().add(instructionsOverlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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