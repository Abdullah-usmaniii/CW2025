package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameOverPanel extends BorderPane {

    private final Button newGameButton;
    private final Button exitButton;

    public GameOverPanel() {
        // Game Over Label
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // New Game Button
        newGameButton = new Button("New Game");
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

    public void setNewGameAction(EventHandler<ActionEvent> event) {
        newGameButton.setOnAction(event);
    }

    public void setExitAction(EventHandler<ActionEvent> event) {
        exitButton.setOnAction(event);
    }
}
