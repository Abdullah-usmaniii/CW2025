package com.comp2042.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A custom UI component representing the "Game Over" screen.
 * Displays the result, high score to beat, and navigation options.
 * @author Abdullah Usmani
 */
public class GameOverPanel extends BorderPane {

    private final Button newGameButton;
    private final Button mainMenuButton;
    private final Button exitButton;
    private final Text highScoreText;

    public GameOverPanel() {
        // Style the Panel Container
        this.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.9);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 30;"
        );
        this.setMinWidth(300);
        this.setMinHeight(350);

        // Game Over Title
        Text gameOverLabel = new Text("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverTitle");

        // High Score Text
        highScoreText = new Text("High Score: 0");
        highScoreText.getStyleClass().add("highScoreText");

        // Buttons
        newGameButton = new Button("REPLAY");
        newGameButton.getStyleClass().add("greenButton");
        newGameButton.setPrefWidth(200);

        mainMenuButton = new Button("MAIN MENU");
        mainMenuButton.getStyleClass().add("ipad-dark-grey");
        mainMenuButton.setPrefWidth(200);

        exitButton = new Button("EXIT");
        exitButton.getStyleClass().add("redButton");
        exitButton.setPrefWidth(200);

        // Layout
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                gameOverLabel,
                highScoreText,
                newGameButton,
                mainMenuButton,
                exitButton
        );

        setCenter(layout);
    }

    /**
     * Updates the text to show the high score the player needs to beat.
     * @param score The current high score.
     */
    public void setHighScore(int score) {

        highScoreText.setText("HIGH SCORE: " + score);
    }

    public void setNewGameAction(EventHandler<ActionEvent> event) {

        newGameButton.setOnAction(event);
    }

    public void setMainMenuAction(EventHandler<ActionEvent> event) {

        mainMenuButton.setOnAction(event);
    }

    public void setExitAction(EventHandler<ActionEvent> event) {

        exitButton.setOnAction(event);
    }
}