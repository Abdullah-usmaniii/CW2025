package com.comp2042.RotationOperations;

import com.comp2042.Logic.MatrixOperations;

/**
 * Immutable data holder for the next rotation state of a brick.
 * Stores the shape matrix and its corresponding rotation index (position).
 * used to pass rotation information safely between the rotator and the board logic.
 *
 * @author Abdullah Usmani
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a NextShapeInfo object.
     * Creates a defensive copy of the shape matrix to ensure immutability.
     *
     * @param shape    The 2D array representing the brick's shape.
     * @param position The rotation index associated with this shape.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = MatrixOperations.copy(shape);
        this.position = position;
    }

    /**
     * Retrieves a copy of the shape matrix.
     * Returns a copy to prevent external modification of the internal state.
     *
     * @return A 2D integer array representing the shape.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Retrieves the rotation position index.
     *
     * @return The integer representing the rotation state.
     */
    public int getPosition() {
        return position;
    }
}