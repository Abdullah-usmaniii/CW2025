package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the Z Brick in Tetris.
 *
 * @author Abdullah Usmani
 */
public final class ZBrick implements Brick {

    // Flyweight: Static storage shared by all ZBrick instances
    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        BRICK_MATRIX.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    public ZBrick() {
        // Empty constructor - data is initialized statically
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}