package com.comp2042.Logic;

/**
 * Data object representing the result of a row clearing operation.
 * Contains details on how many lines were cleared, the resulting board state, and the score points awarded.
 *
 * @author Abdullah Usmani
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a ClearRow object.
     *
     * @param linesRemoved The number of lines cleared in this step.
     * @param newMatrix    The new state of the board matrix after clearing.
     * @param scoreBonus   The score points calculated for this clear.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed.
     * @return The count of removed lines.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the updated board matrix.
     * @return A 2D integer array representing the board.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus awarded.
     * @return The score points.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}