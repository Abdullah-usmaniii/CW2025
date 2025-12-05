package com.comp2042.Logic.bricks;

import com.comp2042.Logic.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Bomb Brick.
 * <p>
 * Updated to a 4x4 matrix to ensure the GameRenderer clears the previous brick's
 * visual artifacts correctly. The bomb is located at [0][0] (Color ID 9),
 * and the rest is padding (Color ID 0).
 * </p>
 */
public final class BombBrick implements Brick {

    private static final List<int[][]> BRICK_MATRIX = new ArrayList<>();

    static {
        // 4x4 Matrix with the Bomb at [0][0] and the rest empty.
        // This ensures the renderer "erases" any leftover pixels from the previous brick.
        BRICK_MATRIX.add(new int[][]{
                {9, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    public BombBrick() {}

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(BRICK_MATRIX);
    }
}