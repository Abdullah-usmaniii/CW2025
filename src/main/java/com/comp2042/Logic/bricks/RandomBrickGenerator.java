package com.comp2042.Logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Concrete implementation of {@link BrickGenerator} that provides random bricks.
 * Manages a queue of upcoming bricks to ensure a continuous stream of random pieces.
 *
 * @author Abdullah Usmani
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Initializes the generator with standard Tetris bricks (I, J, L, O, S, T, Z)
     * and populates the initial queue with two random bricks.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    /**
     * Retrieves the next brick from the queue and ensures upcoming bricks are generated.
     * If the queue runs low, a new random brick is added to maintain the sequence.
     *
     * @return The next {@link Brick} in the queue.
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    /**
     * Peeks at the next brick in the queue without removing it.
     * Allows the game to display a preview of the upcoming piece.
     *
     * @return The upcoming {@link Brick}.
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}