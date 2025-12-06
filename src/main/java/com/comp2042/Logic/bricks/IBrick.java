package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the I Brick.
 *
 * @author Abdullah Usmani
 */
public final class IBrick implements Brick {

    // Flyweight: Static storage shared by all IBrick instances
    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        BRICK_MATRIX.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    public IBrick() {
        // Empty constructor - data is initialized statically
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}