package com.comp2042.Logic;

import com.comp2042.Logic.bricks.Brick;
import com.comp2042.Logic.bricks.BrickGenerator;
import com.comp2042.Logic.bricks.RandomBrickGenerator;
import com.comp2042.RotationOperations.BrickRotator;
import com.comp2042.RotationOperations.NextShapeInfo;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean canHold = true; // Prevents holding multiple times per piece

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
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
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 1); // The bug responsible for spawning bricks in the middle of the board
        canHold = true; // Reset hold ability for new brick
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public boolean holdBrick() {
        if (!canHold) {
            return false; // Cannot hold if flag is false
        }

        canHold = false;
        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            // Case 1: Hold is empty. Store current and spawn new brick.
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            // Case 2: Hold has a brick. Swap and reset current piece state.
            Brick temp = heldBrick;
            heldBrick = currentBrick;

            // Swap the held brick to be the current one
            brickRotator.setBrick(temp);
            currentOffset = new Point(4, 1); // Reset position
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
            // Return an empty 4x4 matrix if nothing is held, matching brick dimensions
            heldData = new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        }

        return new ViewData(brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
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
        heldBrick = null; // Clear held brick on new game
        canHold = true; // Reset hold flag
        createNewBrick();
    }


}
