package com.comp2042.Logic.bricks;

import java.util.List;

/**
 * Interface representing a Tetris brick.
 * implementing classes must provide the shape matrices for all rotation states.
 *
 * @author Abdullah Usmani
 */
public interface Brick {

    /**
     * Retrieves the list of shape matrices representing the brick's rotation states.
     * @return A list of 2D integer arrays.
     */
    List<int[][]> getShapeMatrix();
}