package com.comp2042.Logic;

/**
 * Manages the inventory and activation state of bombs.
 * Tracks how many bombs the player has left and whether a bomb is currently primed for use.
 *
 * @author Abdullah Usmani
 */
public class BombManager {

    private int inventory;
    private boolean isActive;
    private static final int INITIAL_INVENTORY = 4;

    /**
     * Constructs a BombManager with the initial inventory of bombs.
     */
    public BombManager() {
        this.inventory = INITIAL_INVENTORY;
        this.isActive = false;
    }

    /**
     * Attempts to activate a bomb if there is inventory available.
     * Decrements the inventory count upon successful activation.
     *
     * @return true if a bomb was successfully activated, false if inventory is empty.
     */
    public boolean tryActivate() {
        if (inventory > 0) {
            inventory--;
            isActive = true;
            return true;
        }
        return false;
    }

    /**
     * Resets the bomb manager to its initial state, restoring full inventory.
     */
    public void reset() {
        this.inventory = INITIAL_INVENTORY;
        this.isActive = false;
    }

    /**
     * Checks if a bomb is currently active (ready to be spawned).
     *
     * @return true if active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Deactivates the bomb state.
     * Typically called after a bomb brick has been successfully spawned.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Gets the current number of bombs remaining in the inventory.
     *
     * @return The number of bombs left.
     */
    public int getInventory() {
        return inventory;
    }
}