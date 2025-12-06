package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents the O Brick in Tetris.
 *
 * @author Abdullah Usmani
 */
public final class OBrick implements Brick {

    // Flyweight: Static storage shared by all OBrick instances
    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        BRICK_MATRIX.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    public OBrick() {
        // Empty constructor - data is initialized statically
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}