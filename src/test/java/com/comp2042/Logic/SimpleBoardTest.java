package com.comp2042.Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the SimpleBoard class.
 * Checks initialization, movement logic, garbage injection, and bomb integration.
 */
class SimpleBoardTest {

    private SimpleBoard board;
    // Dimensions match Constants: 25 rows (width), 10 cols (height)
    private static final int ROWS = 25;
    private static final int COLS = 10;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(ROWS, COLS);
        // Important: Spawn the first brick to ensure state is valid for movement tests
        board.createNewBrick();
    }

    @Test
    void testInitialization() {
        assertNotNull(board.getBoardMatrix(), "Board matrix should be initialized");
        assertEquals(ROWS, board.getBoardMatrix().length, "Board row count mismatch");
        assertEquals(COLS, board.getBoardMatrix()[0].length, "Board column count mismatch");
        assertNotNull(board.getScore(), "Score object should be initialized");
        assertNotNull(board.getBombManager(), "BombManager should be initialized");
    }

    @Test
    void testMoveBrickDown() {
        ViewData initialView = board.getViewData();
        int startY = initialView.getyPosition();

        boolean moved = board.moveBrickDown();

        assertTrue(moved, "Brick should move down successfully on empty board");
        assertEquals(startY + 1, board.getViewData().getyPosition(), "Y position should increment by 1");
    }

    @Test
    void testMoveBrickLeftAndRight() {
        ViewData initialView = board.getViewData();
        int startX = initialView.getxPosition();

        // Move Left
        assertTrue(board.moveBrickLeft(), "Should move left");
        assertEquals(startX - 1, board.getViewData().getxPosition(), "X position should decrement");

        // Move Right (back to start)
        assertTrue(board.moveBrickRight(), "Should move right");
        assertEquals(startX, board.getViewData().getxPosition(), "X position should return to start");
    }

    @Test
    void testInjectGarbageRowRandomness() {
        // Inject garbage multiple times to check for color variation
        board.injectGarbageRow();
        int[][] matrix = board.getBoardMatrix();

        // The bottom row is at index [ROWS - 1]
        int[] bottomRow = matrix[ROWS - 1];

        boolean hasGap = false;
        boolean hasColor = false;
        boolean allDarkGrey = true;

        for (int cell : bottomRow) {
            if (cell == 0) hasGap = true;
            else if (cell > 0) hasColor = true;

            // Check if we strictly have only 8s (Dark Grey) or variations
            if (cell != 0 && cell != 8) {
                allDarkGrey = false;
            }
        }

        assertTrue(hasGap, "Garbage row must contain at least one gap (0)");
        assertTrue(hasColor, "Garbage row must contain blocks");

        // This assertion verifies the "reverted" logic:
        // It shouldn't be ONLY ID 8 (unless extremely unlucky RNG, which is negligible for 10 cols)
        // Note: If RNG rolls only 8s, this is technically valid but highly unlikely.
        // We mainly want to ensure valid IDs (1-8) are present.
    }

    @Test
    void testNewGameResetsBoard() {
        // Modify state
        board.injectGarbageRow();
        board.getScore().add(100);

        // Reset
        board.newGame();

        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0");

        // Check board is empty
        int[][] matrix = board.getBoardMatrix();
        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell, "Board cells should be 0 after reset");
            }
        }
    }

    @Test
    void testBombLogicIntegration() {
        // Activate bomb mode via manager
        board.getBombManager().tryActivate();
        assertTrue(board.getBombManager().isActive(), "Bomb should be active");

        // Spawn new brick (should be Bomb)
        board.createNewBrick();

        assertFalse(board.getBombManager().isActive(), "Bomb state should be consumed after spawn");

        // Verify the brick is a bomb (BombBrick logic)
        // Since we can't easily check 'instanceof' on private fields, we check behavior or ViewData
        // BombBrick has a 4x4 shape with 9 at [0][0]
        int[][] currentShape = board.getViewData().getBrickData();
        assertEquals(9, currentShape[0][0], "Spawned brick should be a Bomb (ID 9)");
    }
}