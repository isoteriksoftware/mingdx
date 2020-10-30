package com.isoterik.mgdx.input;

import com.badlogic.gdx.utils.*;

/**
 * Contains information about a {@link TouchEventData}.
 * The amount of information contained is determined by the type of event that occurred.
 * <strong>Mouse events are also treated as touch events</strong>
 *
 * @author isoteriksoftware
 */
public class TouchEventData implements Pool.Poolable {
	/**
	 * Types of touch events
	 */
    public enum TouchEvent
    { TOUCH_UP, TOUCH_DOWN, TOUCH_DRAGGED }

	/**
	 * The touch event
	 */
	public TouchEvent touchEvent;

	/**
	 * The touched position in world units
	 */
    public float touchX, touchY;

	/**
	 * The pointer index of the finger that produced this touch event
	 */
	public int pointer = -1;

	/**
	 * The mouse button that produced this touch event
	 * @see com.badlogic.gdx.Input.Buttons
	 */
    public int button = -10;

	/**
	 * Creates a new instance given a touch event
	 * @param touchEvent the touch event
	 */
	public TouchEventData (TouchEvent touchEvent)
    { this.touchEvent = touchEvent; }

    public TouchEventData (){}

	/**
	 * Determines if the touch event that this instance contains is similar to a given touch event
	 * @param secondEvent the touch event to compare against
	 * @return {@code true} it the two events are similar
	 */
    public boolean sameEvent (TouchEvent secondEvent)
    { return this.touchEvent == secondEvent; }

	/**
	 * Determines if a given {@link TouchEventData} has the same event as this one.
	 * <strong>Note:</strong> only the events are compared, the comparison does not include other attributes
	 * @param secondData the given event data
	 * @return {@code true} it the two event data have similar events
	 */
    public boolean sameEvent (TouchEventData secondData)
    { return sameEvent(secondData.touchEvent); }

	@Override
	public void reset() {
		touchEvent = null;
		touchX = touchY = 0;
		pointer = -1;
		button = -10;
	}

	/**
	 *
	 * @return a touch event data for an up event
	 */
	public static TouchEventData upEvent ()
    { return new TouchEventData(TouchEvent.TOUCH_UP); }

	/**
	 *
	 * @return a touch event data for a down event
	 */
    public static TouchEventData downEvent ()
    { return new TouchEventData(TouchEvent.TOUCH_DOWN); }

	/**
	 *
	 * @return a touch event data for a dragged event
	 */
    public static TouchEventData draggedEvent ()
    { return new TouchEventData(TouchEvent.TOUCH_DRAGGED); }

	/**
	 * A pool for recycling instances of {@link TouchEventData}
	 */
	public static class DataPool extends Pool<TouchEventData> {
		@Override
		protected TouchEventData newObject() {
			return new TouchEventData();
		}
	}
}
