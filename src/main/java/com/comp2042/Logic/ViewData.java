package com.comp2042.Logic;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostYPosition;
    private final int[][] heldBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
        this.heldBrickData = heldBrickData;
    }

    public int[][] getBrickData() {

        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {

        return xPosition;
    }

    public int getyPosition() {

        return yPosition;
    }

    public int[][] getNextBrickData() {

        return MatrixOperations.copy(nextBrickData);
    }
    public int getGhostYPosition() {

        return ghostYPosition;
    }
    public int[][] getHeldBrickData() {

        return MatrixOperations.copy(heldBrickData);
    }
}
