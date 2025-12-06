package com.comp2042.Logic;

/**
 * Data wrapper containing the result of a downward movement event.
 * Combines row clearing information and the visual state of the board.
 *
 * @author Abdullah Usmani
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownData object.
     *
     * @param clearRow Information about any rows cleared during the move.
     * @param viewData The current visual state of the game.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the row clearing information.
     * @return The ClearRow object.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the visual data for the game.
     * @return The ViewData object.
     */
    public ViewData getViewData() {
        return viewData;
    }
}