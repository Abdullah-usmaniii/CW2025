package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A custom UI component representing the "Game Over" screen.
 * It displays a "Game Over" message and provides buttons to restart the game or exit.
 */
public class GameOverPanel extends BorderPane {

    private final Button newGameButton;
    private final Button exitButton;

    /**
     * Constructs the Game Over panel.
     * Initializes the layout, applies styles, and sets up the "Replay" and "Exit" buttons.
     */
    public GameOverPanel() {
        // Game Over Label
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // New Game Button
        newGameButton = new Button("Replay");
        newGameButton.getStyleClass().add("greenButton");

        // Exit Button
        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("redButton");

        // Layout for Buttons (Horizontal)
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(newGameButton, exitButton);

        // Main Layout (Label on top, Buttons below)
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(gameOverLabel, buttonBox);

        setCenter(content);
    }

    /**
     * Sets the action to be triggered when the "New Game" (Replay) button is clicked.
     *
     * @param event The event handler to execute for starting a new game.
     */
    public void setNewGameAction(EventHandler<ActionEvent> event) {

        newGameButton.setOnAction(event);
    }

    /**
     * Sets the action to be triggered when the "Exit" button is clicked.
     *
     * @param event The event handler to execute for exiting the game.
     */
    public void setExitAction(EventHandler<ActionEvent> event) {

        exitButton.setOnAction(event);
    }
}
