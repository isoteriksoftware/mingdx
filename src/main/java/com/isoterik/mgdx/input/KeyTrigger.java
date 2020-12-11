package com.isoterik.mgdx.input;

/**
 * A trigger for key events.
 * @see com.isoterik.mgdx.input.ITrigger
 *
 * @author isoteriksoftware
 */
public class KeyTrigger extends Trigger {
    /**
     * Determines when this trigger should fire
     */
    public KeyEventData keyEventData;
    
    @Override
    public KeyTrigger setActive (boolean active)
    { super.setActive(active); return this; }
    
    @Override
    public KeyTrigger setPolled (boolean polled)
    { super.setPolled(polled); return this; }

    /**
     * Creates a new instance given a key event data
     * @param keyEventData the key event data
     */
    public KeyTrigger (KeyEventData keyEventData)
    { this.keyEventData = keyEventData; }

    /**
     * @param keyCode the key code
     * @return a trigger that fires when a keyUp event occurs
     * @see com.isoterik.mgdx.input.KeyEventData.KeyEvent
     */
    public static KeyTrigger keyUpTrigger (int keyCode)
    { return new KeyTrigger(KeyEventData.upEvent(keyCode)); }

    /**
     * @param keyCode the key code
     * @return a trigger that fires when a keyDown event occurs
     * @see com.isoterik.mgdx.input.KeyEventData.KeyEvent
     */
    public static KeyTrigger keyDownTrigger (int keyCode)
    { return new KeyTrigger(KeyEventData.downEvent(keyCode)); }

    /**
     * @param keyChar the key character
     * @return a trigger that fires when a keyTyped event occurs
     * @see com.isoterik.mgdx.input.KeyEventData.KeyEvent
     */
    public static KeyTrigger keyTypedTrigger (char keyChar)
    { return new KeyTrigger(KeyEventData.typedEvent(keyChar)); }
}
