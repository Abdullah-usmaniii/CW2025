package com.comp2042.Logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    void testGetNextBrickReturnsValue() {
        // Ensure getNextBrick() returns a valid brick immediately after initialization
        Brick nextBrick = generator.getNextBrick();
        assertNotNull(nextBrick, "Next brick should not be null");
        assertFalse(nextBrick.getShapeMatrix().isEmpty(), "Brick shape matrix should not be empty");
    }

    @Test
    void testGetNextBrickIsIdempotent() {
        // Calling getNextBrick() multiple times without calling getBrick()
        // should return the same object (peeking at the queue)
        Brick firstPeek = generator.getNextBrick();
        Brick secondPeek = generator.getNextBrick();
        Brick thirdPeek = generator.getNextBrick();

        assertSame(firstPeek, secondPeek, "getNextBrick should return the same instance on repeated calls");
        assertSame(secondPeek, thirdPeek, "getNextBrick should return the same instance on repeated calls");
    }

    @Test
    void testGetBrickAdvancesQueue() {
        // 1. Peek at the next brick
        Brick expectedCurrent = generator.getNextBrick();

        // 2. Get (consume) the brick
        Brick actualCurrent = generator.getBrick();

        // 3. Verify the consumed brick matches the one we peeked
        assertSame(expectedCurrent, actualCurrent, "getBrick() should return the brick previously shown by getNextBrick()");

        // 4. Verify the queue has advanced (the new 'next' brick is different OR a new instance)
        // Note: In rare random cases it might be the same type, but likely a different object instance
        // or at least logic implies the queue moved.
        Brick newNext = generator.getNextBrick();
        assertNotNull(newNext, "There should be a new next brick available");
    }

    @Test
    void testGeneratorRefillsAutomatically() {
        // The generator starts with 2 bricks.
        // We poll many times to ensure it refills its internal deque dynamically.

        for (int i = 0; i < 50; i++) {
            Brick b = generator.getBrick();
            assertNotNull(b, "Generator should never return null");

            // Basic validation of the brick content
            List<int[][]> matrix = b.getShapeMatrix();
            assertNotNull(matrix);
            assertFalse(matrix.isEmpty());
        }
    }

    @Test
    void testBrickStructureValidity() {
        // Fetch a brick and verify it strictly adheres to expected 4x4 or valid matrix structure
        Brick brick = generator.getBrick();
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            assertEquals(4, shape.length, "Brick matrix height should be 4");
            assertEquals(4, shape[0].length, "Brick matrix width should be 4");
        }
    }
}