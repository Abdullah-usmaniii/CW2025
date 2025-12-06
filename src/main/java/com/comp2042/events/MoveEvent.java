package com.comp2042.events;

/**
 * Represents a movement event within the game.
 * Encapsulates the type of movement and the source (User input or Game Thread).
 *
 * @author Abdullah Usmani
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent.
     *
     * @param eventType   The type of action (e.g., LEFT, RIGHT, DOWN).
     * @param eventSource The origin of the event (USER or THREAD).
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of the event.
     * @return The EventType.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the event.
     * @return The EventSource.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}