package com.comp2042.Logic.bricks;

/**
 * Interface for generating bricks.
 * Handles the creation of current and upcoming bricks for the game queue.
 *
 * @author Abdullah Usmani
 */
public interface BrickGenerator {

    /**
     * Retrieves the next brick from the queue and advances the generator.
     * @return The next {@link Brick} to be played.
     */
    Brick getBrick();

    /**
     * Peeks at the upcoming brick without removing it from the queue.
     * @return The upcoming {@link Brick}.
     */
    Brick getNextBrick();
}