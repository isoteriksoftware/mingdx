package com.isoterik.mgdx.input;

/**
 * The base interface for all triggers.
 * A trigger uses an event data to determine when to fire. When a trigger fires, the {@link InputManager} invokes any {@link InputListener} associated with that trigger.
 * @see Trigger
 * @see KeyTrigger
 * @see TouchTrigger
 * @see GestureTrigger
 *
 * @author isoteriksoftware
 */
public interface ITrigger {
    /**
     * Changes the state of this trigger
     * @param active if {@code true} this trigger can be fired, otherwise it is disabled
     * @return this instance for chaining
     */
    ITrigger setActive(boolean active);

    /**
     *
     * @return {@code true} if this trigger can be fired, {@code false} otherwise
     */
    boolean isActive();

    /**
     * Determines if this trigger is polled or not.
     * @param polled if {@code true} this trigger will continue to fire as long as a certain event is occurring, else thr trigger will only be fire once when a certain event occurs
     * @return this instance for chaining
     */
    ITrigger setPolled(boolean polled);

    /**
     *
     * @return {@code true} if this trigger is polled, {@code false} otherwise
     */
    boolean isPolled();
}
