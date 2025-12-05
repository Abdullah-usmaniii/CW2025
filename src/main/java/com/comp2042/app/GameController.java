package com.comp2042.app;

import com.comp2042.Logic.*;
import com.comp2042.events.EventSource;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.view.GuiController;
import com.comp2042.view.BombPanel;
import javafx.scene.control.Button;

/**
 * Controls the main game Logic and acts as the bridge between UI and game Logic.
 * It handles input events and updates the game state accordingly.
 */

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
    private final GuiController viewGuiController;
    private final LevelManager levelManager;
    private final GameMode gameMode;

    /**
     * Constructs a new GameController with the specified Game Mode.
     * Initializes the board, event listener, and game view.
     *
     * @param c    The GuiController instance responsible for rendering.
     * @param mode The selected GameMode (CLASSIC or DIG).
     */
    public GameController(GuiController c, GameMode mode) {
        this.viewGuiController = c;
        this.gameMode = mode;

        board.createNewBrick();
        this.levelManager = new LevelManager(
                board.getScore(),
                viewGuiController::updateLevel,
                this::handleSpeedChange
        );

        if (mode == GameMode.BOMB) {
            Button btn = viewGuiController.getBombButton();
            if (btn != null && board instanceof SimpleBoard) {
                // Focus handling: ensure clicking button doesn't steal focus permanently
                btn.setFocusTraversable(false);

                btn.setOnAction(e -> {
                    SimpleBoard sb = (SimpleBoard) board;
                    boolean success = sb.getBombManager().tryActivate();
                    if (success) {
                        // 1. Update UI Button State
                        viewGuiController.updateBombButtonState(sb.getBombManager().getInventory());

                        // 2. Show Notification
                        viewGuiController.showNotification(sb.getBombManager().getInventory() + " BOMBS REMAINING");

                        // 3. Action: Force spawn new brick (which will be a bomb due to active state)
                        sb.createNewBrick();

                        // 4. Update visuals immediately
                        viewGuiController.refreshBrick(sb.getViewData());
                        viewGuiController.refreshNextBrick(sb.getViewData().getNextBrickData());

                        // 5. Return focus to game panel so keyboard works
                        viewGuiController.returnFocusToGame();                    }
                });
            }
        }

        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
    }

    private void handleSpeedChange(Double rate) {
        viewGuiController.setGameSpeed(rate);
        viewGuiController.updateSpeed(rate);
    }
    /**
     * Retrieves the current Score object associated with the game board.
     *
     * @return The Score object containing the current score and high score.
     */

    public com.comp2042.Logic.Score getScore() {
        return board.getScore();
    }

    /**
     * Handles the 'Down' event, which can be triggered by user input or the game loop (gravity).
     * Moves the brick down if possible; otherwise, merges the brick to the background,
     * checks for cleared rows, plays sound effects, and spawns a new brick.
     *
     * @param event The MoveEvent containing the source of the event (USER or THREAD).
     * @return A DownData object containing information about cleared rows and the current view data.
     */
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

            // --- DIG MODE LOGIC ---
            // If in Dig Mode and lines were cleared, add garbage to challenge the user
            if (gameMode == GameMode.DIG && clearRow.getLinesRemoved() > 0) {
                if (board instanceof SimpleBoard) {
                    // Add one row of garbage per clear event to maintain challenge
                    ((SimpleBoard) board).injectGarbageRow();
                }
            }
            // -----------------------

            if (clearRow.getLinesRemoved() > 0) {
                SoundManager.getInstance().playClearSound();
                board.getScore().add(clearRow.getScoreBonus());
            } else {
                SoundManager.getInstance().playPlaceSound();
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            } else {
                viewGuiController.refreshNextBrick(board.getViewData().getNextBrickData());
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the 'Left' event triggered by user input.
     * Attempts to move the current brick one unit to the left.
     *
     * @param event The MoveEvent associated with the left movement.
     * @return The updated ViewData reflecting the new position of the brick.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the 'Right' event triggered by user input.
     * Attempts to move the current brick one unit to the right.
     *
     * @param event The MoveEvent associated with the right movement.
     * @return The updated ViewData reflecting the new position of the brick.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the 'Rotate' event triggered by user input.
     * Attempts to rotate the current brick counter-clockwise.
     *
     * @param event The MoveEvent associated with the rotation.
     * @return The updated ViewData reflecting the new orientation of the brick.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Handles the 'Hold' event triggered by user input.
     * Swaps the current brick with the held brick or holds the current brick if the hold slot is empty.
     *
     * @param event The MoveEvent associated with the hold action.
     * @return The updated ViewData, typically reflecting the swapped or new brick.
     */
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

    /**
     * Handles the 'Hard Drop' event triggered by user input.
     * Instantly moves the brick to the lowest possible position, calculates the score
     * based on distance dropped, merges the brick, and checks for cleared rows.
     *
     * @param event The MoveEvent associated with the hard drop action.
     * @return A DownData object containing information about cleared rows and the current view data.
     */
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int points = 0;
        while(board.moveBrickDown()){
            points++;
        }
        board.getScore().add(points);
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        // --- DIG MODE LOGIC ---
        if (gameMode == GameMode.DIG && clearRow.getLinesRemoved() > 0) {
            if (board instanceof SimpleBoard) {
                ((SimpleBoard) board).injectGarbageRow();
            }
        }
        // -----------------------

        if (clearRow.getLinesRemoved() > 0){
            SoundManager.getInstance().playClearSound();
            board.getScore().add(clearRow.getScoreBonus());
        } else {
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

    /**
     * Resets the game state to start a new game.
     * Clears the board, resets the score, and refreshes all UI components including
     * the game background, next brick, and hold brick displays.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        levelManager.reset();

        if (gameMode == GameMode.BOMB && viewGuiController.getBombButton() != null && board instanceof SimpleBoard) {
            viewGuiController.updateBombButtonState(((SimpleBoard)board).getBombManager().getInventory());
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshNextBrick(board.getViewData().getNextBrickData());
        viewGuiController.refreshHoldBrick(board.getViewData().getHeldBrickData());
    }
}
