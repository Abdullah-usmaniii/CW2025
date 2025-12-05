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
 */
public class BombPanel extends Pane {

    private final Label countLabel;
    private final BorderPane clickableBox;

    /**
     * Constructs the BombPanel with a title and a clickable container.
     */
    public BombPanel() {
        // 1. Container Dimensions (Matching the Hold Pane in FXML)
        this.setPrefSize(120, 120);

        // 2. Title Label ("BOMB")
        Label title = new Label("BOMB");
        title.getStyleClass().add("nextBrickLabel"); // Reusing style from CSS
        title.setLayoutX(20);
        title.setLayoutY(5);

        // 3. The Clickable Box (BorderPane)
        clickableBox = new BorderPane();
        clickableBox.getStyleClass().add("holdBrick"); // Reusing border style
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

        // 6. Visual Interaction on the Box
        clickableBox.setOnMouseEntered(e -> clickableBox.setStyle("-fx-border-color: red; -fx-border-width: 4px; -fx-cursor: hand;"));
        clickableBox.setOnMouseExited(e -> clickableBox.setStyle("")); // Reset to CSS default
    }

    /**
     * Updates the inventory count text.
     * @param count The number of bombs remaining.
     */
    public void updateCount(int count) {
        countLabel.setText("x" + count);
        if (count <= 0) {
            clickableBox.setOpacity(0.5);
            clickableBox.setDisable(true);
        } else {
            clickableBox.setOpacity(1.0);
            clickableBox.setDisable(false);
        }
    }

    /**
     * Sets the action to perform when the box is clicked.
     * @param action The Runnable logic.
     */
    public void setOnBombClicked(Runnable action) {
        clickableBox.setOnMouseClicked(e -> {
            if (!clickableBox.isDisabled()) {
                action.run();
            }
        });
    }
}