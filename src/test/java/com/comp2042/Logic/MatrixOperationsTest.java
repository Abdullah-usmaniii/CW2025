package com.comp2042.Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    private int[][] emptyBoard;
    private int[][] boardWithBlocks;
    private int[][] testBrick;

    @BeforeEach
    void setUp() {
        // A 10x10 empty board (representing a portion of the game area)
        emptyBoard = new int[10][10];

        // A board with some blocks already at the bottom
        boardWithBlocks = new int[10][10];
        for (int i = 0; i < 10; i++) {
            boardWithBlocks[9][i] = 1; // Fill the bottom row
        }

        // A simple 2x2 'O' brick
        testBrick = new int[][]{
                {4, 4},
                {4, 4}
        };
    }

    // --- Tests for intersect() ---

    @Test
    void testIntersect_NoCollision() {
        // Brick is in an empty space
        assertFalse(MatrixOperations.intersect(emptyBoard, testBrick, 2, 2),
                "Should not intersect with an empty board in an open area.");
    }

    @Test
    void testIntersect_CollisionWithBlock() {
        // Brick is trying to move into the filled bottom row
        assertTrue(MatrixOperations.intersect(boardWithBlocks, testBrick, 2, 8),
                "Should intersect with existing blocks.");
    }

    @Test
    void testIntersect_CollisionWithLeftWall() {
        // Brick is trying to move off-screen to the left
        assertTrue(MatrixOperations.intersect(emptyBoard, testBrick, -1, 2),
                "Should intersect with the left boundary.");
    }

    @Test
    void testIntersect_CollisionWithRightWall() {
        // Brick is trying to move off-screen to the right (board width is 10)
        assertTrue(MatrixOperations.intersect(emptyBoard, testBrick, 9, 2),
                "Should intersect with the right boundary.");
    }

    @Test
    void testIntersect_CollisionWithBottomWall() {
        // Brick is trying to move off-screen at the bottom (board height is 10)
        assertTrue(MatrixOperations.intersect(emptyBoard, testBrick, 2, 9),
                "Should intersect with the bottom boundary.");
    }

    // --- Tests for copy() ---

    @Test
    void testCopy_CreatesNewInstance() {
        int[][] original = new int[][]{{1, 2}, {3, 4}};
        int[][] copied = MatrixOperations.copy(original);

        assertNotSame(original, copied,
                "The copied array should be a new object, not the same reference.");
    }

    @Test
    void testCopy_HasSameContent() {
        int[][] original = new int[][]{{1, 2}, {3, 4}};
        int[][] copied = MatrixOperations.copy(original);

        assertArrayEquals(original, copied,
                "The copied array should have the same content as the original.");

        // Prove it's a deep copy, not shallow
        copied[0][0] = 99;
        assertNotEquals(original[0][0], copied[0][0],
                "Changing the copy should not change the original.");
    }

    // --- Tests for merge() ---

    @Test
    void testMerge_MergesBrickCorrectly() {
        int[][] merged = MatrixOperations.merge(emptyBoard, testBrick, 3, 3);

        // Check if the brick's "4" values are now in the board
        assertEquals(4, merged[3][3]);
        assertEquals(4, merged[3][4]);
        assertEquals(4, merged[4][3]);
        assertEquals(4, merged[4][4]);
        // Check that an area outside the brick is still empty
        assertEquals(0, merged[0][0]);
    }

    //  Tests for checkRemoving()

    @Test
    void testCheckRemoving_NoRowsToClear() {
        // Create a board with no completely filled rows
        int[][] partialBoard = new int[10][10];
        // Fill bottom row partially (leaving one cell empty)
        for (int i = 0; i < 9; i++) {
            partialBoard[9][i] = 1;
        }
        // partialBoard[9][9] remains 0 (empty)

        ClearRow result = MatrixOperations.checkRemoving(partialBoard);
        assertEquals(0, result.getLinesRemoved(),
                "Should remove 0 lines when no row is completely full.");
        assertEquals(0, result.getScoreBonus(),
                "Score bonus should be 0 for 0 lines.");
    }

    @Test
    void testCheckRemoving_OneRowToClear() {
        // Fill row 8 completely (row 9 is already filled in boardWithBlocks)
        for (int i = 0; i < 10; i++) {
            boardWithBlocks[8][i] = 2; // Use a different number for clarity
        }

        ClearRow result = MatrixOperations.checkRemoving(boardWithBlocks);

        assertEquals(2, result.getLinesRemoved(), "Should remove exactly two lines (rows 8 and 9).");
        assertEquals(200, result.getScoreBonus(), "Score bonus should be 200 for 2 lines (50 * 2 * 2).");

        int[][] newMatrix = result.getNewMatrix();
        // Check that the top two rows are now empty
        assertArrayEquals(new int[10], newMatrix[0], "The new top row should be empty.");
        assertArrayEquals(new int[10], newMatrix[1], "The second row should be empty.");
    }

    @Test
    void testCheckRemoving_ExactlyOneRowToClear() {
        // Create a fresh board with only one completely filled row
        int[][] singleRowBoard = new int[10][10];
        // Fill only the bottom row
        for (int i = 0; i < 10; i++) {
            singleRowBoard[9][i] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(singleRowBoard);

        assertEquals(1, result.getLinesRemoved(), "Should remove exactly one line.");
        assertEquals(50, result.getScoreBonus(), "Score bonus should be 50 for 1 line.");

        int[][] newMatrix = result.getNewMatrix();
        // Check that the top row is now empty (rows shifted up)
        assertArrayEquals(new int[10], newMatrix[0], "The new top row should be empty.");
        // Check that the bottom row is now empty (cleared row was removed)
        assertArrayEquals(new int[10], newMatrix[9], "The bottom row should now be empty.");
    }

    @Test
    void testCheckRemoving_MultipleRowsToClear() {
        // Fill rows 7, 8, and 9
        for (int r = 7; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                boardWithBlocks[r][c] = r; // Fill with non-zero values
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(boardWithBlocks);

        assertEquals(3, result.getLinesRemoved(), "Should remove exactly three lines.");
        // Score bonus = 50 * lines * lines = 50 * 3 * 3 = 450
        assertEquals(450, result.getScoreBonus(), "Score bonus should be 450 for 3 lines.");

        // Check that the top 3 rows are now empty
        assertArrayEquals(new int[10], result.getNewMatrix()[0], "Top row should be empty.");
        assertArrayEquals(new int[10], result.getNewMatrix()[1], "Second row should be empty.");
        assertArrayEquals(new int[10], result.getNewMatrix()[2], "Third row should be empty.");
    }
}