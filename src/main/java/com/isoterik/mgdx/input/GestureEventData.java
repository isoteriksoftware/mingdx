package com.isoterik.mgdx.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Contains information about a {@link GestureEvent}.
 * The amount of information contained is determined by the type of event that occurred
 *
 * @author isoteriksoftware
 */
public class GestureEventData implements Pool.Poolable {
    /**
     * Types of gesture events
     */
    public enum GestureEvent {
        FLING, LONG_PRESS, PAN, PAN_STOP, PINCH, PINCH_STOP,
        TAP, DOUBLE_TAP, ZOOM
    }

    /**
     * The gesture event
     */
    public GestureEvent gestureEvent;

    /**
     * The position (in world units) where the event occurred
     */
    public float x, y;

    /**
     * The fling velocity (in world units per second)
     */
    public float flingVelocityX, flingVelocityY;

    /**
     * The amount of change (in world units) after panning
     */
    public float panDeltaX, panDeltaY;

    /**
     * The initial distance between the two fingers before the zoom gesture started
     */
    public float initialZoomDistance;

    /**
     * The current distance between the two fingers after zoom
     */
    public float zoomDistance;

    /**
     * The ratio of {@link #initialZoomDistance} to {@link #zoomDistance}. You can multiply this with the current zoom of a camera to get the final zoom
     */
    public float zoomFactor;

    /**
     * Determines whether a zoom-in gesture was detected.
     */
    public boolean zoomedIn;

    /**
     * The pointer index of the finger that produced this gesture
     */
    public int pointer = -1;

    /**
     * The mouse button that produced this gesture
     * @see com.badlogic.gdx.Input.Buttons
     */
    public int button = -10;

    /**
     * The number of taps produced by this gesture
     */
    public int tapCount;

    /**
     * The position (in world units) of the finger before the pinch gesture started.
     */
    public Vector2 initialPinchPointer1, initialPinchPointer2;

    /**
     * The current position (in world units) of the finger after the pinch gesture.
     */
    public Vector2 pinchPointer1, pinchPointer2;

    /**
     * Creates a new instance given a gesture event
     * @param gestureEvent the gesture event
     */
    public GestureEventData (GestureEvent gestureEvent) {
        this.gestureEvent = gestureEvent;
    }
    
    public GestureEventData() {}

	@Override
	public void reset() {
		gestureEvent = null;
		x = y = 0;
		flingVelocityX = flingVelocityY = 0;
		panDeltaX = panDeltaY = 0;
		initialZoomDistance = zoomDistance = 0;
		zoomFactor = 0;
		zoomedIn = false;
		pointer = -1;
		button = -10;
		tapCount = 0;
		initialPinchPointer1 = null;
		initialPinchPointer2 = null;
		pinchPointer1 = null;
		pinchPointer2 = null;
	}

    /**
     * Determines if the gesture event that this instance contains is similar to a given gesture event
     * @param secondEvent the gesture event to compare against
     * @return {@code true} it the two events are similar
     */
    public boolean sameEvent (GestureEvent secondEvent) {
        return gestureEvent == secondEvent;
    }

    /**
     * Determines if a given {@link GestureEventData} has the same event as this one.
     * <strong>Note:</strong> only the events are compared, the comparison does not include other attributes
     * @param secondData the given event data
     * @return {@code true} it the two event data have similar events
     */
    public boolean sameEvent (GestureEventData secondData) {
        return sameEvent(secondData.gestureEvent);
    }

    /**
     *
     * @return a gesture event data for a fling event
     */
    public static GestureEventData flingEvent ()
    { return new GestureEventData(GestureEvent.FLING); }

    /**
     *
     * @return a gesture event data for a long press event
     */
    public static GestureEventData longPressEvent ()
    { return new GestureEventData(GestureEvent.LONG_PRESS); }

    /**
     *
     * @return a gesture event data for a pan event
     */
    public static GestureEventData panEvent ()
    { return new GestureEventData(GestureEvent.PAN); }

    /**
     *
     * @return a gesture event data for a panStop event
     */
    public static GestureEventData panStopEvent ()
    { return new GestureEventData(GestureEvent.PAN_STOP); }

    /**
     *
     * @return a gesture event data for a pinch event
     */
    public static GestureEventData pinchEvent ()
    { return new GestureEventData(GestureEvent.PINCH); }

    /**
     *
     * @return a gesture event data for a pinchStop event
     */
    public static GestureEventData pinchStopEvent ()
    { return new GestureEventData(GestureEvent.PINCH_STOP); }

    /**
     *
     * @return a gesture event data for a tap event
     */
    public static GestureEventData tapEvent ()
    { return new GestureEventData(GestureEvent.TAP); }

    /**
     *
     * @return a gesture event data for a doubleTap event
     */
    public static GestureEventData doubleTapEvent ()
    { return new GestureEventData(GestureEvent.DOUBLE_TAP); }

    /**
     *
     * @return a gesture event data for a zoom event
     */
	public static GestureEventData zoomEvent ()
    { return new GestureEventData(GestureEvent.ZOOM); }

    /**
     * A pool for recycling instances of {@link GestureEventData}
     */
	public static class DataPool extends Pool<GestureEventData>
	{
		@Override
		protected GestureEventData newObject()
		{
			return new GestureEventData();
		}
	}
}
