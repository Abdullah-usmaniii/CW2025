package com.comp2042.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A custom UI component representing the Bomb inventory.
 * Designed to match the dimensions and style of the "Hold Brick" panel.
 * @author Abdullah Usmani
 */
public class BombPanel extends Pane {

    private final Label countLabel;
    private final BorderPane clickableBox;

    /**
     * Constructs the BombPanel with a title and a clickable container.
     */
    public BombPanel() {
        // 1. Container Dimensions
        // Force the pane to stay 120x120 so it doesn't shrink and create dead zones
        this.setPrefSize(120, 120);
        this.setMinSize(120, 120);
        this.setMaxSize(120, 120);

        // Ensure the entire area captures mouse events
        this.setPickOnBounds(true);
        // Adding a transparent background ensures the empty gaps are hit-testable
        this.setStyle("-fx-background-color: transparent;");

        // 2. Title Label ("BOMB")
        Label title = new Label("BOMB");
        title.getStyleClass().add("nextBrickLabel");
        title.setLayoutX(20);
        title.setLayoutY(5);

        // 3. The Clickable Box (BorderPane)
        clickableBox = new BorderPane();
        clickableBox.getStyleClass().add("holdBrick");
        clickableBox.setPrefSize(100, 70);
        clickableBox.setMaxWidth(100);
        clickableBox.setLayoutX(15);
        clickableBox.setLayoutY(25);

        // 4. Content inside the box (Preview + Count)
        Rectangle bombPreview = new Rectangle(20, 20);
        bombPreview.setFill(Color.BLACK);
        bombPreview.setStroke(Color.WHITE);
        bombPreview.setStrokeWidth(1);

        countLabel = new Label("x4");
        countLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Let\\'s go Digital'; -fx-font-size: 18px;");

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(bombPreview, countLabel);

        clickableBox.setCenter(content);

        // 5. Add children to this Pane
        this.getChildren().addAll(title, clickableBox);

        // Visual feedback on Hover
        this.setOnMouseEntered(e -> {
            clickableBox.setStyle("-fx-border-color: red; -fx-border-width: 4px; -fx-cursor: hand;");
        });

        this.setOnMouseExited(e -> {
            clickableBox.setStyle("");
        });
    }

    /**
     * Updates the inventory count text.
     *
     * @param count The number of bombs remaining.
     */
    public void updateCount(int count) {
        countLabel.setText("x" + count);
        if (count <= 0) {
            clickableBox.setOpacity(0.5);
            this.setDisable(true); // Disable the whole panel, not just the box
        } else {
            clickableBox.setOpacity(1.0);
            this.setDisable(false);
        }
    }

    /**
     * Sets the action to perform when the box is clicked.
     *
     * @param action The Runnable logic.
     */
    public void setOnBombClicked(Runnable action) {
        this.setOnMouseClicked(e -> {
            // Check if the panel is disabled
            if (!this.isDisabled()) {
                action.run();
            }
        });
    }
}