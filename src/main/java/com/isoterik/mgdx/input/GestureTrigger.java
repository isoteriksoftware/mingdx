package com.isoterik.mgdx.input;

/**
 * A trigger for gesture events.
 * @see com.isoterik.mgdx.input.ITrigger
 *
 * @author isoteriksoftware
 */
public class GestureTrigger extends Trigger {
    /**
     * Determines when this trigger should fire
     */
    public GestureEventData gestureEventData;
    
    @Override
    public GestureTrigger setActive (boolean active)
    { super.setActive(active); return this; }
    
    @Override
    public GestureTrigger setPolled (boolean polled)
    { super.setPolled(polled); return this; }

    /**
     * Creates a new instance given a gesture event data that determines when the trigger should be fired
     * @param gestureEventData the event data
     */
    public GestureTrigger (GestureEventData gestureEventData)
    { this.gestureEventData = gestureEventData; }

    /**
     *
     * @return a trigger that fires when a fling event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger flingTrigger ()
    { return new GestureTrigger(GestureEventData.flingEvent()); }

    /**
     *
     * @return a trigger that fires when a long press event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger longPressTrigger ()
    { return new GestureTrigger(GestureEventData.longPressEvent()); }

    /**
     *
     * @return a trigger that fires when a pan event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger panTrigger ()
    { return new GestureTrigger(GestureEventData.panEvent()); }

    /**
     *
     * @return a trigger that fires when a panStop event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger panStopTrigger ()
    { return new GestureTrigger(GestureEventData.panStopEvent()); }

    /**
     *
     * @return a trigger that fires when a pinch event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger pinchTrigger ()
    { return new GestureTrigger(GestureEventData.pinchEvent()); }

    /**
     *
     * @return a trigger that fires when a pinchStop event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger pinchStopTrigger ()
    { return new GestureTrigger(GestureEventData.pinchStopEvent()); }

    /**
     *
     * @return a trigger that fires when a tap event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger tapTrigger ()
    { return new GestureTrigger(GestureEventData.tapEvent()); }

    /**
     *
     * @return a trigger that fires when a doubleTap event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger doubleTapTrigger ()
    { return new GestureTrigger(GestureEventData.doubleTapEvent()); }

    /**
     *
     * @return a trigger that fires when a zoom event occurs
     * @see com.isoterik.mgdx.input.GestureEventData.GestureEvent
     */
    public static GestureTrigger zoomTrigger ()
    { return new GestureTrigger(GestureEventData.zoomEvent()); }
}
