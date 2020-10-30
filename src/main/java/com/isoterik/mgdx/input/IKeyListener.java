package com.isoterik.mgdx.input;

/**
 * Register an instance of this interface and a {@link KeyTrigger} with {@link InputManager} to define what happens when a key event occurs.
 *
 * @author isoteriksoftware
 */
public interface IKeyListener extends InputListener {
    /**
     * Called when a key event occurs. The {@link KeyEventData} passed must not be modified, it should be used as a read-only data source.
     * @param mappingName the name of the mapping that this listener is mapped to. If this listener is not mapped, this value will be {@code null}
     * @param keyEventData the key event data containing information about the key event that occurred
     */
    void onKey(String mappingName, KeyEventData keyEventData);
}
