package com.comp2042.Logic;

import java.util.List;
import java.util.ArrayList;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBricksData;
    private final int ghostYPosition;
    private final int[][] heldBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]>  nextBrickData, int ghostYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = nextBrickData;
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

    public List<int[][]>  getNextBrickData() {

        return new ArrayList<>(nextBricksData);
    }
    public int getGhostYPosition() {

        return ghostYPosition;
    }
    public int[][] getHeldBrickData() {

        return MatrixOperations.copy(heldBrickData);
    }
}
