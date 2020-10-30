package com.isoterik.mgdx.input;

/**
 * Register an instance of this interface and a {@link TouchTrigger} with {@link InputManager} to define what happens when a touch event occurs.
 *
 * @author isoteriksoftware
 */
public interface ITouchListener extends InputListener {
    /**
     * Called when a touch event occurs. The {@link TouchEventData} passed must not be modified, it should be used as a read-only data source.
     * @param mappingName the name of the mapping that this listener is mapped to. If this listener is not mapped, this value will be {@code null}
     * @param touchEventData the touch event data containing information about the touch event that occurred
     */
    void onTouch(String mappingName, TouchEventData touchEventData);
}
