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

    // Queue to hold the next 3 bricks
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

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
        // Pop the top brick from the queue to be the current brick
        Brick currentBrick = nextBricks.poll();

        // Add a new brick to the end of the queue (Queue moves up)
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

        canHold = false;
        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);
            // We do NOT pull from the queue on a swap, we just swap the active piece
            currentOffset = new Point(Constants.SPAWN_X, Constants.SPAWN_Y);
        }
        return true;
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

        // Convert the queue of bricks to a List of matrices for ViewData
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

        // Reset Queue
        nextBricks.clear();
        for (int i = 0; i < 3; i++) {
            nextBricks.add(brickGenerator.getBrick());
        }

        createNewBrick();
    }
}