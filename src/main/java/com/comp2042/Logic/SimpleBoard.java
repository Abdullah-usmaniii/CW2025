/**
 * src/main/java/com/comp2042/Logic/SimpleBoard.java
 */
package com.comp2042.Logic;

import com.comp2042.Logic.bricks.Brick;
import com.comp2042.Logic.bricks.BrickGenerator;
import com.comp2042.Logic.bricks.RandomBrickGenerator;
import com.comp2042.RotationOperations.BrickRotator;
import com.comp2042.RotationOperations.NextShapeInfo;
import com.comp2042.app.Constants;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the game board logic, storing the state of the grid and handling brick movements.
 * Implements the {@link Board} interface.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean canHold = true;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a SimpleBoard with specified dimensions.
     * Initializes the score, brick generator, and pre-fills the next brick queue.
     *
     * @param width  The number of rows in the board (vertical height in logical matrix).
     * @param height The number of columns in the board (horizontal width).
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();

        // Initialize the queue with 3 bricks
        for (int i = 0; i < 3; i++) {
            nextBricks.add(brickGenerator.getBrick());
        }
    }

    /**
     * Injects a row of garbage blocks at the bottom of the board.
     * Shifts all existing blocks up by one row. The top row is lost (potentially causing game over logic elsewhere).
     * The new bottom row will have at least one empty space to ensure it is clearable.
     */
    public void injectGarbageRow() {
        // 1. Shift all rows up by 1
        // loop from 0 to second-to-last row
        for (int i = 0; i < width - 1; i++) {
            System.arraycopy(currentGameMatrix[i + 1], 0, currentGameMatrix[i], 0, height);
        }

        // 2. Generate new bottom row
        int[] garbageRow = new int[height];
        int holeIndex = ThreadLocalRandom.current().nextInt(height);

        for (int j = 0; j < height; j++) {
            if (j == holeIndex) {
                garbageRow[j] = 0; // The hole
            } else {
                // Random color block (1-7), avoiding 0
                // 8 is a good "garbage" color (grey) if supported by renderer, otherwise random 1-7
                garbageRow[j] = ThreadLocalRandom.current().nextInt(1, 8);
            }
        }

        // 3. Assign to the last row
        currentGameMatrix[width - 1] = garbageRow;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = nextBricks.poll();
        nextBricks.add(brickGenerator.getBrick());
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(Constants.SPAWN_X, Constants.SPAWN_Y);
        canHold = true;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public boolean holdBrick() {
        if (!canHold) {
            return false;
        }

        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            heldBrick = currentBrick;
            createNewBrick();
            canHold = false;
            return true;
        } else {
            Brick incomingBrick = heldBrick;
            int[][] nextShape = incomingBrick.getShapeMatrix().get(0);
            boolean hasConflict = MatrixOperations.intersect(currentGameMatrix, nextShape, (int) currentOffset.getX(), (int) currentOffset.getY());

            if (hasConflict) {
                return false;
            }
            heldBrick = currentBrick;
            brickRotator.setBrick(incomingBrick);
            canHold = false;
            return true;
        }
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int[][] heldData;
        if(heldBrick != null){
            heldData = heldBrick.getShapeMatrix().get(0);
        } else{
            heldData = new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        }

        List<int[][]> nextShapes = new ArrayList<>();
        for (Brick b : nextBricks) {
            nextShapes.add(b.getShapeMatrix().get(0));
        }

        return new ViewData(brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextShapes,
                getGhostY(),
                heldData
        );
    }

    private int getGhostY() {
        int[][] currentMatrix = currentGameMatrix;
        int[][] shape = brickRotator.getCurrentShape();
        int x = (int) currentOffset.getX();
        int y = (int) currentOffset.getY();

        while (!MatrixOperations.intersect(currentMatrix, shape, x, y + 1)) {
            y++;
        }
        return y;
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        canHold = true;
        nextBricks.clear();
        for (int i = 0; i < 3; i++) {
            nextBricks.add(brickGenerator.getBrick());
        }
        createNewBrick();
    }
}