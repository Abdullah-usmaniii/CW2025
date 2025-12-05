package com.comp2042.Logic;

/**
 * Manages the inventory and activation state of bombs.
 */
public class BombManager {

    private int inventory;
    private boolean isActive;
    private static final int INITIAL_INVENTORY = 4;

    public BombManager() {
        this.inventory = INITIAL_INVENTORY;
        this.isActive = false;
    }

    public boolean tryActivate() {
        if (inventory > 0) {
            inventory--;
            isActive = true;
            return true;
        }
        return false;
    }

    public void reset() {
        this.inventory = INITIAL_INVENTORY;
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public int getInventory() {
        return inventory;
    }
}