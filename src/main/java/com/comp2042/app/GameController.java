package com.comp2042.app;

import com.comp2042.Logic.*;
import com.comp2042.events.EventSource;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.view.GuiController;


public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Initialize next brick display
        // bindScore moved to Main so the UI wiring happens there
    }

    public com.comp2042.Logic.Score getScore() {

        return board.getScore();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (canMove) {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        } else {
            board.mergeBrickToBackground();

            clearRow = board.clearRows();

            // Prioritize Clear sound over Place sound
            if (clearRow.getLinesRemoved() > 0) {
                SoundManager.getInstance().playClearSound();
                board.getScore().add(clearRow.getScoreBonus());
            } else {
                // Only plays place sound if no lines were cleared
                SoundManager.getInstance().playPlaceSound();
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            } else {
                // Refresh the next brick display when a new brick is created
                viewGuiController.refreshNextBrick(board.getViewData().getNextBrickData());
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        if (board.holdBrick()) {
            // If the hold/swap was successful, update the display.
            ViewData viewData = board.getViewData();
            viewGuiController.refreshNextBrick(viewData.getNextBrickData());
            viewGuiController.refreshHoldBrick(viewData.getHeldBrickData());
            return viewData;
        }
        return board.getViewData(); // Return current view data if hold failed
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event){
        // Initialize a counter for the points based on distance dropped
        int points = 0;

        // Loop moves the brick down until it hits something
        while(board.moveBrickDown()){
            points++; // Count 1 point for every successful move down
        }
        // Add the total drop distance points to the score
        board.getScore().add(points);

        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();

        boolean rowsCleared = clearRow.getLinesRemoved() > 0;
        if (rowsCleared){
            // Trigger CLEAR sound effect when rows are removed
            SoundManager.getInstance().playClearSound();
            board.getScore().add(clearRow.getScoreBonus());
        } else {
            // Trigger PLACE sound effect ONLY IF no rows were cleared
            SoundManager.getInstance().playPlaceSound();
        }

        if (board.createNewBrick()){
            viewGuiController.gameOver();
        } else {
            viewGuiController.refreshNextBrick(board.getViewData().getNextBrickData());
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        // Refresh next brick display for new game
        viewGuiController.refreshNextBrick(board.getViewData().getNextBrickData());
        viewGuiController.refreshHoldBrick(board.getViewData().getHeldBrickData());
    }
}
