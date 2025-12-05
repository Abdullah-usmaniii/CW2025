package com.comp2042.Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BombManagerTest {

    private BombManager bombManager;

    @BeforeEach
    void setUp() {
        bombManager = new BombManager();
    }

    @Test
    void testInitialState() {
        assertEquals(4, bombManager.getInventory(), "Initial inventory should be 4");
        assertFalse(bombManager.isActive(), "Should not be active initially");
    }

    @Test
    void testTryActivateSuccess() {
        assertTrue(bombManager.tryActivate(), "Should return true when activating with inventory > 0");
        assertEquals(3, bombManager.getInventory(), "Inventory should decrease by 1");
        assertTrue(bombManager.isActive(), "Should be active after successful activation");
    }

    @Test
    void testTryActivateFail() {
        // Use up all bombs
        bombManager.tryActivate();
        bombManager.tryActivate();
        bombManager.tryActivate();
        bombManager.tryActivate();

        assertEquals(0, bombManager.getInventory());

        // Try one more time
        assertFalse(bombManager.tryActivate(), "Should return false when inventory is 0");
        assertEquals(0, bombManager.getInventory(), "Inventory should remain 0");
    }

    @Test
    void testReset() {
        bombManager.tryActivate();
        bombManager.reset();

        assertEquals(4, bombManager.getInventory(), "Inventory should reset to 4");
        assertFalse(bombManager.isActive(), "Active state should be cleared");
    }

    @Test
    void testDeactivate() {
        bombManager.tryActivate();
        assertTrue(bombManager.isActive());

        bombManager.deactivate();
        assertFalse(bombManager.isActive(), "Should be inactive after calling deactivate");
        assertEquals(3, bombManager.getInventory(), "Inventory should not change on deactivate");
    }
}