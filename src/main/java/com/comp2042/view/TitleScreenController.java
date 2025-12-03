package com.comp2042.view;

import com.comp2042.Logic.SoundManager;
import com.comp2042.app.Constants;
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
 * Manages navigation to the game, settings, instructions, and exiting the application.
 */
public class TitleScreenController {

    @FXML private StackPane rootPane; // Reference to the root stack pane
    @FXML private VBox settingsPanel;
    @FXML private Slider volumeSlider;

    private Parent instructionsOverlay;

    /**
     * Initializes the controller.
     * Sets up the volume slider with the current volume from SoundManager.
     */
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
     * Loads the Game Layout FXML, initializes the game logic, and transitions the scene.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    public void handleStartGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_GAME_LAYOUT));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Setup controller logic
            GuiController controller = loader.getController();
            com.comp2042.app.GameController gameLogic = new com.comp2042.app.GameController(controller);
            controller.bindScore(gameLogic.getScore().scoreProperty());
            controller.bindHighScore(gameLogic.getScore().highScoreProperty());

            currentScene.setRoot(root);

            controller.newGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Settings" button click.
     * Toggles the visibility of the settings panel (e.g., volume control).
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    public void handleSettings(ActionEvent event) {
        boolean isVisible = settingsPanel.isVisible();
        settingsPanel.setVisible(!isVisible);
        settingsPanel.setManaged(!isVisible);
    }

    /**
     * Handles the "Instructions" button click.
     * Loads and displays the instructions overlay.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    public void handleInstructions(ActionEvent event) {
        if (instructionsOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_INSTRUCTIONS));
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

    /**
     * Handles the "Exit" button click.
     * Terminates the application.
     *
     * @param event The ActionEvent triggered by the button.
     */
    @FXML
    public void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Closes the instructions overlay by removing it from the root pane.
     */
    public void closeInstructions() {
        if (rootPane != null && instructionsOverlay != null) {
            rootPane.getChildren().remove(instructionsOverlay);
        }
    }
}