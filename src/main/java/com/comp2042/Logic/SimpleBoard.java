package com.comp2042.Logic;

import com.comp2042.Logic.bricks.Brick;
import com.comp2042.Logic.bricks.BrickGenerator;
import com.comp2042.Logic.bricks.RandomBrickGenerator;
import com.comp2042.Logic.bricks.BombBrick;
import com.comp2042.RotationOperations.BrickRotator;
import com.comp2042.RotationOperations.NextShapeInfo;
import com.comp2042.app.Constants;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the game board logic, handling grid state, brick movement, and game mode specifics.
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

    // -- Bomb Mode Fields --
    private final BombManager bombManager;
    private boolean lastBrickWasBomb = false;

    /**
     * Constructs a SimpleBoard and initializes game components.
     * @param width  Number of rows (vertical size).
     * @param height Number of columns (horizontal size).
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
        this.bombManager = new BombManager();

        for (int i = 0; i < 3; i++) {
            nextBricks.add(brickGenerator.getBrick());
        }
    }

    /**
     * Gets the BombManager instance.
     * @return The BombManager.
     */
    public BombManager getBombManager() {
        return bombManager;
    }

    /**
     * Injects a row of garbage blocks at the bottom for Dig mode.
     */
    public void injectGarbageRow() {
        for (int i = 0; i < width - 1; i++) {
            System.arraycopy(currentGameMatrix[i + 1], 0, currentGameMatrix[i], 0, height);
        }
        int[] garbageRow = new int[height];
        int holeIndex = ThreadLocalRandom.current().nextInt(height);
        for (int j = 0; j < height; j++) {
            if (j == holeIndex) garbageRow[j] = 0;
            else garbageRow[j] = ThreadLocalRandom.current().nextInt(1, 8);
        }
        currentGameMatrix[width - 1] = garbageRow;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) return false;
        currentOffset = p;
        return true;
    }

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) return false;
        currentOffset = p;
        return true;
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) return false;
        currentOffset = p;
        return true;
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) return false;
        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    @Override
    public boolean createNewBrick() {
        // [Logic] Check if a bomb needs to be spawned
        if (bombManager.isActive()) {
            Brick currentBrick = new BombBrick();
            bombManager.deactivate();

            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(Constants.SPAWN_X, Constants.SPAWN_Y);
            canHold = true;
            return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        }

        Brick currentBrick = nextBricks.poll();
        nextBricks.add(brickGenerator.getBrick());
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(Constants.SPAWN_X, Constants.SPAWN_Y);
        canHold = true;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public boolean holdBrick() {
        if (!canHold) return false;

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

            if (hasConflict) return false;
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
        int[][] heldData = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
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
        // [Logic] Check if the merged brick is a Bomb to set flag
        if (brickRotator.getBrick() instanceof BombBrick) {
            lastBrickWasBomb = true;
        } else {
            lastBrickWasBomb = false;
        }
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        if (lastBrickWasBomb) {

            int rowToClear = (int) currentOffset.getY();

            // Validate bounds (width is num rows in this array structure)
            if (rowToClear >= 0 && rowToClear < width) {
                int[][] newMatrix = new int[width][height];

                // Shift all rows above the bomb down by 1
                for (int r = rowToClear; r > 0; r--) {
                    newMatrix[r] = currentGameMatrix[r - 1];
                }

                // Keep rows below the bomb
                for (int r = rowToClear + 1; r < width; r++) {
                    newMatrix[r] = currentGameMatrix[r];
                }
                newMatrix[0] = new int[height];
                currentGameMatrix = newMatrix;
                lastBrickWasBomb = false;
                return new ClearRow(1, newMatrix, 50);
            }
        }

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
        bombManager.reset();
        lastBrickWasBomb = false;
        nextBricks.clear();
        for (int i = 0; i < 3; i++) {
            nextBricks.add(brickGenerator.getBrick());
        }
        createNewBrick();
    }
}