package com.comp2042.Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;

public class GhostBricksTest {

    private SimpleBoard board;
    private static final int BOARD_WIDTH = 25;
    private static final int BOARD_HEIGHT = 10;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(BOARD_WIDTH, BOARD_HEIGHT);
    }

    @Test
    void testGhostPositionOnEmptyBoard() {
        // Create a new brick on empty board
        board.createNewBrick();
        ViewData viewData = board.getViewData();

        // Ghost should be at the bottom of the board
        // Since the brick starts at y=1, ghost should be much lower
        assertTrue(viewData.getGhostYPosition() > viewData.getyPosition(),
                "Ghost Y position should be lower than current brick position");
    }

    @Test
    void testGhostPositionWithObstacles() {
        // Create a brick and place it at bottom to create an obstacle
        board.createNewBrick();

        // Simulate moving brick to bottom
        while (board.moveBrickDown()) {
            // Keep moving down
        }
        board.mergeBrickToBackground();

        // Create new brick
        board.createNewBrick();
        ViewData viewData = board.getViewData();

        // Ghost position should be above the obstacle
        assertTrue(viewData.getGhostYPosition() >= 0,
                "Ghost Y position should be valid (>= 0)");
        assertTrue(viewData.getGhostYPosition() < BOARD_WIDTH,
                "Ghost Y position should be within board bounds");
    }

    @Test
    void testGhostPositionAfterHorizontalMovement() {
        board.createNewBrick();
        ViewData initialViewData = board.getViewData();
        int initialGhostY = initialViewData.getGhostYPosition();

        // Move brick left
        board.moveBrickLeft();
        ViewData leftViewData = board.getViewData();

        // Move brick right from original position
        board.moveBrickRight();
        board.moveBrickRight();
        ViewData rightViewData = board.getViewData();

        // Ghost Y position might be the same or different depending on obstacles
        // But should always be valid
        assertTrue(leftViewData.getGhostYPosition() >= 0,
                "Ghost Y after left movement should be valid");
        assertTrue(rightViewData.getGhostYPosition() >= 0,
                "Ghost Y after right movement should be valid");
    }

    @Test
    void testGhostPositionAfterRotation() {
        board.createNewBrick();
        ViewData beforeRotation = board.getViewData();

        // Rotate the brick
        board.rotateLeftBrick();
        ViewData afterRotation = board.getViewData();

        // Both ghost positions should be valid
        assertTrue(beforeRotation.getGhostYPosition() >= 0,
                "Ghost Y before rotation should be valid");
        assertTrue(afterRotation.getGhostYPosition() >= 0,
                "Ghost Y after rotation should be valid");

        // Ghost positions might be different due to different brick shape
        assertTrue(beforeRotation.getGhostYPosition() < BOARD_WIDTH,
                "Ghost Y before rotation should be within bounds");
        assertTrue(afterRotation.getGhostYPosition() < BOARD_WIDTH,
                "Ghost Y after rotation should be within bounds");
    }

    @Test
    void testGhostPositionWhenBrickAtBottom() {
        board.createNewBrick();

        // Move brick as far down as possible
        while (board.moveBrickDown()) {
            // Keep moving
        }

        ViewData viewData = board.getViewData();

        // When brick can't move down anymore, ghost should be at same position
        assertEquals(viewData.getyPosition(), viewData.getGhostYPosition(),
                "Ghost position should equal current position when at bottom");
    }

    @Test
    void testViewDataIntegration() {
        board.createNewBrick();
        ViewData viewData = board.getViewData();

        // Test that ViewData contains all necessary information
        assertNotNull(viewData, "ViewData should not be null");
        assertNotNull(viewData.getBrickData(), "Brick data should not be null");
        assertNotNull(viewData.getNextBrickData(), "Next brick data should not be null");

        // Test coordinate validity
        assertTrue(viewData.getxPosition() >= 0, "X position should be non-negative");
        assertTrue(viewData.getyPosition() >= 0, "Y position should be non-negative");
        assertTrue(viewData.getGhostYPosition() >= 0, "Ghost Y position should be non-negative");

        // Ghost should be at or below current position
        assertTrue(viewData.getGhostYPosition() >= viewData.getyPosition(),
                "Ghost position should be at or below current position");
    }

    @Test
    void testGhostPositionConsistency() {
        board.createNewBrick();

        // Get ghost position multiple times without changing game state
        ViewData viewData1 = board.getViewData();
        ViewData viewData2 = board.getViewData();

        assertEquals(viewData1.getGhostYPosition(), viewData2.getGhostYPosition(),
                "Ghost position should be consistent for same game state");
    }

    @Test
    void testGhostPositionWithComplexBoard() {
        // Create a more complex board state with partial rows
        board.createNewBrick();

        // Move and merge first brick
        while (board.moveBrickDown()) { }
        board.mergeBrickToBackground();

        // Create and move second brick to different position
        board.createNewBrick();
        board.moveBrickLeft();
        board.moveBrickLeft();
        while (board.moveBrickDown()) { }
        board.mergeBrickToBackground();

        // Create third brick and test ghost position
        board.createNewBrick();
        ViewData viewData = board.getViewData();

        // Ghost position should be calculated correctly with obstacles
        assertTrue(viewData.getGhostYPosition() >= viewData.getyPosition(),
                "Ghost should be at or below current position even with complex board");
        assertTrue(viewData.getGhostYPosition() < BOARD_WIDTH,
                "Ghost position should be within board bounds");
    }

    @Test
    void testNewGameResetsGhostCalculation() {
        // Create initial state
        board.createNewBrick();
        ViewData beforeNewGame = board.getViewData();

        // Start new game
        board.newGame();
        ViewData afterNewGame = board.getViewData();

        // Both should have valid ghost positions
        assertTrue(beforeNewGame.getGhostYPosition() >= 0,
                "Ghost position before new game should be valid");
        assertTrue(afterNewGame.getGhostYPosition() >= 0,
                "Ghost position after new game should be valid");

        // After new game, ghost should be calculated on empty board
        assertTrue(afterNewGame.getGhostYPosition() > afterNewGame.getyPosition(),
                "Ghost should be significantly below current position on empty board");
    }
}

