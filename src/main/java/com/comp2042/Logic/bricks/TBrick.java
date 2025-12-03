package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public final class TBrick implements Brick {

    // Flyweight: Static storage shared by all TBrick instances
    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        BRICK_MATRIX.add(new int[][]{
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        BRICK_MATRIX.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
    }

    public TBrick() {
        // Empty constructor - data is initialized statically
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}