package com.comp2042.RotationOperations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for NextShapeInfo class.
 * 
 * NextShapeInfo Purpose:
 * - Immutable data container that holds information about a brick's rotation state
 * - Encapsulates a 2D array representing the shape matrix of a Tetris piece
 * - Stores the position index of this shape in the rotation sequence
 * - Provides safe access to shape data by returning copies (prevents external modification)
 * - Used by BrickRotator to communicate rotation information to the game logic
 * 
 * This class ensures data integrity and provides a clean interface for passing
 * shape rotation information between different parts of the Tetris game system.
 * The immutable design prevents accidental modifications that could corrupt game state.
 */
class NextShapeInfoTest {

    @Test
    void testConstructorAndGetters() {
        // Test basic constructor and getter functionality
        int[][] testShape = {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}};
        int testPosition = 2;

        NextShapeInfo shapeInfo = new NextShapeInfo(testShape, testPosition);

        // Verify that the shape and position are stored correctly
        assertArrayEquals(testShape, shapeInfo.getShape());
        assertEquals(testPosition, shapeInfo.getPosition());
    }

    @Test
    void testGetShapeReturnsCopy() {
        // Test that getShape() returns a copy, not the original reference
        int[][] originalShape = {{1, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        NextShapeInfo shapeInfo = new NextShapeInfo(originalShape, 1);

        int[][] returnedShape = shapeInfo.getShape();

        // Modify the returned shape
        returnedShape[0][0] = 999;

        // Original shape accessed through getShape() should remain unchanged
        int[][] freshCopy = shapeInfo.getShape();
        assertEquals(1, freshCopy[0][0]); // Should still be 1, not 999
        assertNotSame(originalShape, returnedShape); // Should be different objects
    }

    @Test
    void testImmutability() {
        // Test that the NextShapeInfo object is effectively immutable
        int[][] testShape = {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}};
        NextShapeInfo shapeInfo = new NextShapeInfo(testShape, 3);

        // Modify the original array after creating NextShapeInfo
        testShape[1][1] = 999;

        // The NextShapeInfo should not be affected by external modifications
        int[][] retrievedShape = shapeInfo.getShape();
        assertEquals(1, retrievedShape[1][1]); // Should still be 1, not 999
    }

    @Test
    void testEmptyShape() {
        // Test behavior with an empty shape matrix
        int[][] emptyShape = {};
        int position = 0;

        NextShapeInfo shapeInfo = new NextShapeInfo(emptyShape, position);

        assertEquals(0, shapeInfo.getShape().length);
        assertEquals(position, shapeInfo.getPosition());
    }

    @Test
    void testSingleCellShape() {
        // Test with a minimal 1x1 shape
        int[][] singleCell = {{1}};
        int position = 4;

        NextShapeInfo shapeInfo = new NextShapeInfo(singleCell, position);

        int[][] result = shapeInfo.getShape();
        assertEquals(1, result.length);
        assertEquals(1, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(position, shapeInfo.getPosition());
    }

    @Test
    void testLargeShape() {
        // Test with a larger shape matrix
        int[][] largeShape = {
            {1, 0, 1, 0, 1},
            {0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 0, 1, 0}
        };
        int position = 7;

        NextShapeInfo shapeInfo = new NextShapeInfo(largeShape, position);

        int[][] result = shapeInfo.getShape();
        assertEquals(4, result.length);
        assertEquals(5, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(0, result[0][1]);
        assertEquals(position, shapeInfo.getPosition());
    }

    @Test
    void testNegativePosition() {
        // Test that negative positions are handled (though they shouldn't occur in normal use)
        int[][] testShape = {{1, 1}, {1, 1}};
        int negativePosition = -1;

        NextShapeInfo shapeInfo = new NextShapeInfo(testShape, negativePosition);

        assertEquals(negativePosition, shapeInfo.getPosition());
        assertArrayEquals(testShape, shapeInfo.getShape());
    }

    @Test
    void testMultipleInstancesIndependence() {
        // Test that multiple NextShapeInfo instances don't interfere with each other
        int[][] shape1 = {{1, 0}, {0, 1}};
        int[][] shape2 = {{0, 1}, {1, 0}};

        NextShapeInfo info1 = new NextShapeInfo(shape1, 1);
        NextShapeInfo info2 = new NextShapeInfo(shape2, 2);

        // Verify each instance maintains its own data
        assertEquals(1, info1.getPosition());
        assertEquals(2, info2.getPosition());
        assertArrayEquals(shape1, info1.getShape());
        assertArrayEquals(shape2, info2.getShape());

        // Verify they're independent
        assertNotEquals(info1.getPosition(), info2.getPosition());
        assertFalse(java.util.Arrays.deepEquals(info1.getShape(), info2.getShape()));
    }

    @Test
    void testShapeContentPreservation() {
        // Test that complex shape patterns are preserved correctly
        int[][] complexShape = {
            {0, 1, 1, 0},
            {1, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 1}
        };

        NextShapeInfo shapeInfo = new NextShapeInfo(complexShape, 5);
        int[][] retrieved = shapeInfo.getShape();

        // Verify every cell is preserved correctly
        for (int i = 0; i < complexShape.length; i++) {
            for (int j = 0; j < complexShape[i].length; j++) {
                assertEquals(complexShape[i][j], retrieved[i][j], 
                    "Mismatch at position [" + i + "][" + j + "]");
            }
        }
    }
}
