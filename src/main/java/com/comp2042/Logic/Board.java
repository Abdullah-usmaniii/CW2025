package com.comp2042.Logic;
/**
 * Interface representing the game board and its operations.
 *
 * @author Abdullah Usmani
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    boolean holdBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();
    

    ClearRow clearRows();

    Score getScore();

    void newGame();
}
