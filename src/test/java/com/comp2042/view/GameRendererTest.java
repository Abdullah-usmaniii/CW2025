package com.comp2042.view;

import com.comp2042.Logic.ViewData;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameRendererTest {

    private GameRenderer renderer;
    private GridPane gamePanel;
    private GridPane brickPanel;
    private GridPane ghostPanel;
    private GridPane nextBrickPanel;
    private GridPane holdBrickPanel;

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() {
        gamePanel = new GridPane();
        brickPanel = new GridPane();
        ghostPanel = new GridPane();
        nextBrickPanel = new GridPane();
        holdBrickPanel = new GridPane();

        renderer = new GameRenderer(gamePanel, brickPanel, ghostPanel, nextBrickPanel, holdBrickPanel);
    }

    @Test
    void testInitGameViewPopulatesGrids() {
        int[][] dummyBoard = new int[25][10];
        int[][] dummyBrick = {{1}};
        // Construct dummy view data
        ViewData viewData = new ViewData(dummyBrick, 0, 0,
                Collections.singletonList(dummyBrick), 0, dummyBrick);

        Platform.runLater(() -> {
            renderer.initGameView(dummyBoard, viewData);

            // Check if grids have children (rectangles)
            assertFalse(gamePanel.getChildren().isEmpty(), "Game panel should have rectangles");
            assertFalse(brickPanel.getChildren().isEmpty(), "Brick panel should have rectangles");
            assertFalse(nextBrickPanel.getChildren().isEmpty(), "Next brick panel should have rectangles");
        });
    }
}