package com.comp2042.RotationOperations;

import com.comp2042.Logic.bricks.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    // 1. Replaced @Mock with a concrete Stub class
    private StubBrick stubBrick;
    private BrickRotator brickRotator;
    private List<int[][]> mockShapeMatrix;

    // Simple implementation of Brick for testing purposes
    static class StubBrick implements Brick {
        private final List<int[][]> matrix;

        public StubBrick(List<int[][]> matrix) {
            this.matrix = matrix;
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return matrix;
        }
    }

    @BeforeEach
    void setUp() {
        // No MockitoAnnotations.openMocks(this) needed
        brickRotator = new BrickRotator();

        int[][] shape0 = {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}};
        int[][] shape1 = {{0, 1, 0}, {0, 1, 1}, {0, 1, 0}};
        int[][] shape2 = {{0, 0, 0}, {1, 1, 1}, {0, 1, 0}};
        int[][] shape3 = {{0, 1, 0}, {1, 1, 0}, {0, 1, 0}};

        mockShapeMatrix = Arrays.asList(shape0, shape1, shape2, shape3);

        // 2. Initialize the stub instead of using 'mock()'
        stubBrick = new StubBrick(mockShapeMatrix);
    }

    @Test
    void testSetBrick() {
        // 3. Removed 'when(...).thenReturn(...)' since the stub handles it naturally
        brickRotator.setBrick(stubBrick);

        assertArrayEquals(mockShapeMatrix.get(0), brickRotator.getCurrentShape());
    }

    @Test
    void testGetCurrentShape() {
        brickRotator.setBrick(stubBrick);

        int[][] currentShape = brickRotator.getCurrentShape();

        assertArrayEquals(mockShapeMatrix.get(0), currentShape);
    }

    @Test
    void testSetCurrentShape() {
        brickRotator.setBrick(stubBrick);

        brickRotator.setCurrentShape(2);

        assertArrayEquals(mockShapeMatrix.get(2), brickRotator.getCurrentShape());
    }

    @Test
    void testGetNextShape() {
        brickRotator.setBrick(stubBrick);

        NextShapeInfo nextShape = brickRotator.getNextShape();

        assertArrayEquals(mockShapeMatrix.get(1), nextShape.getShape());
        assertEquals(1, nextShape.getPosition());
    }

    @Test
    void testGetNextShapeWrapsAround() {
        brickRotator.setBrick(stubBrick);
        brickRotator.setCurrentShape(3);

        NextShapeInfo nextShape = brickRotator.getNextShape();

        // Should wrap around to index 0
        assertArrayEquals(mockShapeMatrix.get(0), nextShape.getShape());
        assertEquals(0, nextShape.getPosition());
    }

    @Test
    void testMultipleRotations() {
        brickRotator.setBrick(stubBrick);

        for (int i = 0; i < mockShapeMatrix.size(); i++) {
            brickRotator.setCurrentShape(i);

            NextShapeInfo nextShape = brickRotator.getNextShape();
            int expectedNextPosition = (i + 1) % mockShapeMatrix.size();

            assertEquals(expectedNextPosition, nextShape.getPosition());
            assertArrayEquals(mockShapeMatrix.get(expectedNextPosition), nextShape.getShape());
        }
    }

    @Test
    void testSingleShapeBrick() {
        int[][] singleShape = {{1, 1}, {1, 1}};
        StubBrick singleBrick = new StubBrick(Arrays.asList(new int[][][]{singleShape}));
        brickRotator.setBrick(singleBrick);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        assertEquals(0, nextShape.getPosition());
        assertArrayEquals(singleShape, nextShape.getShape());
    }


    @Test
    void testCurrentShapeAfterBrickChange() {
        brickRotator.setBrick(stubBrick);
        brickRotator.setCurrentShape(2);

        // Create a different brick stub
        int[][] differentShape = {{1, 0}, {1, 1}};
        StubBrick anotherBrick = new StubBrick(Arrays.asList(new int[][][]{differentShape}));

        brickRotator.setBrick(anotherBrick);

        assertArrayEquals(differentShape, brickRotator.getCurrentShape());
    }
}
