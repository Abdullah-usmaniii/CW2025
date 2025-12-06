package com.comp2042.Logic;

import java.util.List;
import java.util.ArrayList;

/**
 * Immutable data transfer object that holds the state of the game required for rendering.
 * Contains the current brick, position, ghost position, and upcoming/held bricks.
 *
 * @author Abdullah Usmani
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBricksData;
    private final int ghostYPosition;
    private final int[][] heldBrickData;

    /**
     * Constructs a ViewData object with all necessary game state information.
     *
     * @param brickData      The shape matrix of the current falling brick.
     * @param xPosition      The x-coordinate of the current brick.
     * @param yPosition      The y-coordinate of the current brick.
     * @param nextBrickData  A list of shape matrices for upcoming bricks.
     * @param ghostYPosition The y-coordinate where the ghost brick should be drawn.
     * @param heldBrickData  The shape matrix of the currently held brick.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData, int ghostYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
        this.heldBrickData = heldBrickData;
    }

    /**
     * Gets a copy of the current brick's shape matrix.
     * @return A 2D integer array.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the X position of the current brick.
     * @return The column index.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the Y position of the current brick.
     * @return The row index.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a list containing the shape matrices of the next bricks.
     * @return A list of 2D integer arrays.
     */
    public List<int[][]> getNextBrickData() {
        return new ArrayList<>(nextBricksData);
    }

    /**
     * Gets the Y position calculated for the ghost brick (shadow).
     * @return The row index for the ghost.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }

    /**
     * Gets a copy of the held brick's shape matrix.
     * @return A 2D integer array.
     */
    public int[][] getHeldBrickData() {
        return MatrixOperations.copy(heldBrickData);
    }
}