package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public final class SBrick implements Brick {

    // Flyweight: Static storage shared by all SBrick instances
    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        BRICK_MATRIX.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    public SBrick() {
        // Empty constructor - data is initialized statically
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}