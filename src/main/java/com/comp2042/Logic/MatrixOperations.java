package com.comp2042.Logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import com.comp2042.app.Constants;

/**
 * Utility class providing static methods for matrix manipulation.
 * Handles collision detection, matrix copying, merging bricks, and clearing rows.
 *
 * @author Abdullah Usmani
 */
public class MatrixOperations {

    private MatrixOperations(){
    }

    /**
     * Checks if a specific brick shape intersects with existing blocks in the matrix or goes out of bounds.
     *
     * @param matrix The background board matrix.
     * @param brick  The shape matrix of the brick to check.
     * @param x      The x-coordinate (column) of the brick.
     * @param y      The y-coordinate (row) of the brick.
     * @return true if there is a collision or out-of-bounds condition, false otherwise.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to determine if coordinates are outside the board limits.
     *
     * @param matrix  The board matrix.
     * @param targetX The target x-coordinate.
     * @param targetY The target y-coordinate.
     * @return true if the coordinates are out of bounds.
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original The array to copy.
     * @return A new 2D array identical to the original.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick into the background matrix.
     * Used when a brick lands and becomes part of the board.
     *
     * @param filledFields The current background matrix.
     * @param brick        The shape matrix of the brick to merge.
     * @param x            The x-coordinate of the brick.
     * @param y            The y-coordinate of the brick.
     * @return A new matrix with the brick merged in.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks the board for fully filled rows, removes them, and calculates score bonuses.
     *
     * @param matrix The board matrix to check.
     * @return A {@link ClearRow} object containing the number of cleared lines, the new matrix, and the score.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = Constants.SCORE_PER_LINE_MULTIPLIER * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Creates a deep copy of a list of 2D arrays.
     *
     * @param list The list to copy.
     * @return A new list containing deep copies of the arrays.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}