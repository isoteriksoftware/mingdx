package com.isoterik.mgdx.input;

/**
 * A trigger for touch events.
 * @see com.isoterik.mgdx.input.ITrigger
 *
 * @author isoteriksoftware
 */
public class TouchTrigger extends Trigger {
    /**
     * Determines when this trigger should fire
     */
    public TouchEventData touchEventData;
    
    @Override
    public TouchTrigger setActive (boolean active)
    { super.setActive(active); return this; }
    
    @Override
    public TouchTrigger setPolled (boolean polled)
    { super.setPolled(polled); return this; }

    /**
     * Creates a new instance given a touch event data
     * @param touchEventData the touch event data
     */
    public TouchTrigger (TouchEventData touchEventData)
    { this.touchEventData = touchEventData; }

    /**
     *
     * @return a trigger that fires when a touchUp event occurs.
     * @see com.isoterik.mgdx.input.TouchEventData.TouchEvent
     */
    public static TouchTrigger touchUpTrigger ()
    { return new TouchTrigger(TouchEventData.upEvent()); }

    /**
     *
     * @return a trigger that fires when a touchDown event occurs.
     * @see com.isoterik.mgdx.input.TouchEventData.TouchEvent
     */
    public static TouchTrigger touchDownTrigger ()
    { return new TouchTrigger(TouchEventData.downEvent()); }

    /**
     *
     * @return a trigger that fires when a touchDragged event occurs.
     * @see com.isoterik.mgdx.input.TouchEventData.TouchEvent
     */
    public static TouchTrigger touchDraggedTrigger ()
    { return new TouchTrigger(TouchEventData.draggedEvent()); }
}
