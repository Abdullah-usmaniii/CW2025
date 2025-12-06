package com.comp2042.RotationOperations;

import com.comp2042.Logic.bricks.Brick;

/**
 * Manages the rotation state of a brick.
 * handles the cycling through different rotation states (shapes) of the current brick.
 *
 * @author Abdullah Usmani
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates and retrieves the next rotation shape in the sequence.
     * @return A {@link NextShapeInfo} object containing the next shape matrix and its index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the matrix for the brick's current rotation state.
     * @return A 2D integer array representing the current shape.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index of the brick.
     * @param currentShape The index of the shape in the brick's rotation list.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the active brick to be rotated and resets the rotation index to 0.
     * @param brick The new Brick object.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Gets the current Brick object.
     * @return The Brick instance.
     */
    public Brick getBrick(){
        return this.brick;
    }
}