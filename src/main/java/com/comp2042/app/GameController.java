package com.comp2042.app;

import com.comp2042.Logic.Board;
import com.comp2042.Logic.ClearRow;
import com.comp2042.Logic.DownData;
import com.comp2042.Logic.SimpleBoard;
import com.comp2042.events.EventSource;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.Logic.ViewData;
import com.comp2042.view.GuiController;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Initialize next brick display
        viewGuiController.initNextBrick(board.getViewData().getNextBrickData());
        // bindScore moved to Main so the UI wiring happens there
    }

    public com.comp2042.Logic.Score getScore() {

        return board.getScore();
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            } else {
                // Refresh next brick display when a new brick is created
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
    public ViewData onHoldEvent(MoveEvent event) { // New method
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
        while(board.moveBrickDown()){
            // Keep moving down
        }

        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0){
            board.getScore().add(clearRow.getScoreBonus());
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
