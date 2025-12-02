package com.comp2042.view;

import com.comp2042.Logic.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all graphical rendering for the Tetris game.
 * Responsible for drawing the board, the falling brick, the ghost brick,
 * and the next/hold brick previews.
 */
public class GameRenderer {

    private static final int BRICK_SIZE = 20;

    // Visual Arrays (Encapsulated)
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    // CHANGED: Now a List to support multiple next brick previews
    private final List<Rectangle[][]> nextBrickMatrices = new ArrayList<>();

    private Rectangle[][] holdBrickMatrix;

    // UI Containers
    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private final GridPane nextBrickPanel;
    private final GridPane holdBrickPanel;

    public GameRenderer(GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel,
                        GridPane nextBrickPanel, GridPane holdBrickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.nextBrickPanel = nextBrickPanel;
        this.holdBrickPanel = holdBrickPanel;
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // Clear old nodes
        gamePanel.getChildren().clear();
        brickPanel.getChildren().clear();
        ghostPanel.getChildren().clear();
        nextBrickPanel.getChildren().clear();
        holdBrickPanel.getChildren().clear();

        // 1. Initialize Board Background
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getBoardFillColor(boardMatrix[i][j]));
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // 2. Initialize Current Brick and Ghost
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);

                Rectangle ghostRect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghostRect.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = ghostRect;
                ghostPanel.add(ghostRect, j, i);
            }
        }

        updateBrickPosition(brick);

        // 3. Initialize Side Panels (Pass the LIST here)
        initNextBrick(brick.getNextBrickData());
        initHoldBrick(brick.getHeldBrickData());
    }

    private void updateBrickPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * ghostPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);
    }

    public void refreshBrick(ViewData brick) {
        updateBrickPosition(brick);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);

                Rectangle ghostRect = ghostRectangles[i][j];
                int val = brick.getBrickData()[i][j];
                if (val == 0) {
                    ghostRect.setFill(Color.TRANSPARENT);
                } else {
                    setRectangleData(val, ghostRect);
                    ghostRect.setOpacity(0.6);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Paint color = getBoardFillColor(board[i][j]);
                displayMatrix[i][j].setFill(color);
                displayMatrix[i][j].setArcHeight(9);
                displayMatrix[i][j].setArcWidth(9);
            }
        }
    }

    /**
     * Initializes the "Next Brick" panel with a LIST of incoming bricks.
     * Stacks them vertically.
     */
    public void initNextBrick(List<int[][]> nextBricksData) {
        nextBrickPanel.getChildren().clear();
        nextBrickMatrices.clear();

        int verticalOffset = 0;

        // Loop through each brick in the list
        for (int[][] brickData : nextBricksData) {
            Rectangle[][] matrix = new Rectangle[brickData.length][brickData[0].length];

            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(getFillColor(brickData[i][j]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    matrix[i][j] = rectangle;
                    // Add to grid with vertical offset for stacking
                    nextBrickPanel.add(rectangle, j, i + verticalOffset);
                }
            }
            nextBrickMatrices.add(matrix);
            // Offset for the next brick (height + padding)
            verticalOffset += 5;
        }
    }

    /**
     * Refreshes the "Next Brick" panel using the updated list of bricks.
     */
    public void refreshNextBrick(List<int[][]> nextBricksData) {
        // Ensure sizes match before updating
        if (!nextBrickMatrices.isEmpty() && nextBrickMatrices.size() == nextBricksData.size()) {
            for (int k = 0; k < nextBricksData.size(); k++) {
                int[][] data = nextBricksData.get(k);
                Rectangle[][] matrix = nextBrickMatrices.get(k);

                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++) {
                        matrix[i][j].setFill(getFillColor(data[i][j]));
                    }
                }
            }
        } else {
            // If the number of previews changed, re-initialize
            initNextBrick(nextBricksData);
        }
    }

    public void initHoldBrick(int[][] holdBrickData) {
        holdBrickPanel.getChildren().clear();
        holdBrickMatrix = new Rectangle[holdBrickData.length][holdBrickData[0].length];
        for (int i = 0; i < holdBrickData.length; i++) {
            for (int j = 0; j < holdBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(holdBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                holdBrickMatrix[i][j] = rectangle;
                holdBrickPanel.add(rectangle, j, i);
            }
        }
    }

    public void refreshHoldBrick(int[][] holdBrickData) {
        if (holdBrickMatrix != null) {
            for (int i = 0; i < holdBrickData.length; i++) {
                for (int j = 0; j < holdBrickData[i].length; j++) {
                    holdBrickMatrix[i][j].setFill(getFillColor(holdBrickData[i][j]));
                }
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Paint getBoardFillColor(int i) {
        if (i == 0) {
            return Color.rgb(255, 255, 255, 0.1);
        }
        return getFillColor(i);
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0: returnPaint = Color.TRANSPARENT; break;
            case 1: returnPaint = Color.AQUA; break;
            case 2: returnPaint = Color.BLUEVIOLET; break;
            case 3: returnPaint = Color.DARKGREEN; break;
            case 4: returnPaint = Color.YELLOW; break;
            case 5: returnPaint = Color.RED; break;
            case 6: returnPaint = Color.BEIGE; break;
            case 7: returnPaint = Color.BURLYWOOD; break;
            default: returnPaint = Color.WHITE; break;
        }
        return returnPaint;
    }
}