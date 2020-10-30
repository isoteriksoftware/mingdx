package com.isoterik.mgdx.input;

/**
 * Register an instance of this interface and a {@link GestureTrigger} with {@link InputManager} to define what happens when a gesture event occurs.
 *
 * @author isoteriksoftware
 */
public interface IGestureListener extends InputListener {
    /**
     * Called when a gesture event occurs. The {@link GestureEventData} passed must not be modified, it should be used as a read-only data source.
     * @param mappingName the name of the mapping that this listener is mapped to. If this listener is not mapped, this value will be {@code null}
     * @param gestureEventData the gesture event data containing information about the gesture event that occurred
     */
    void onGesture(String mappingName, GestureEventData gestureEventData);
}
