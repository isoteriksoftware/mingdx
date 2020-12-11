package com.isoterik.mgdx.input;

import com.badlogic.gdx.utils.Pool;

/**
 * Contains information about a {@link KeyEvent}.
 * The amount of information contained is determined by the type of event that occurred
 *
 * @author isoteriksoftware
 */
public class KeyEventData implements Pool.Poolable {
	/**
	 * Types of key events
	 */
    public enum KeyEvent
    { KEY_UP, KEY_DOWN, KEY_TYPED }

	/**
	 * The key event
	 */
	public KeyEvent keyEvent;

	/**
	 * The key code of the key event
	 * @see KeyCodes
	 */
    public int keyCode;

	/**
	 * The key character of the key event
	 */
	public char keyChar;

	/**
	 * Creates a new instance given a key event and a key code
	 * @param keyEvent the key event
	 * @param keyCode the key code
	 */
    public KeyEventData (KeyEvent keyEvent, int keyCode) {
        this.keyEvent = keyEvent;
        this.keyCode  = keyCode;
    }

	/**
	 * Creates a new instance given a key event, key code and a character.
	 * @param keyChar the key character
	 */
    public KeyEventData (char keyChar) {
        this.keyEvent = KeyEvent.KEY_TYPED;
        this.keyChar  = keyChar;
    }

    public KeyEventData() {}

	@Override
	public void reset() {
		keyEvent = null;
		keyCode = 0;
		keyChar = 0;
	}

	/**
	 * Determines if the key event that this instance contains is similar to a given key event
	 * @param secondEvent the key event to compare against
	 * @return {@code true} it the two events are similar
	 */
	public boolean sameEvent (KeyEvent secondEvent)
	{ return keyEvent == secondEvent; }

	/**
	 * Determines if a given {@link KeyEventData} has the same event as this one.
	 * <strong>Note:</strong> this compares all the properties.
	 * @param secondData the given event data
	 * @return {@code true} it the two event data have similar events
	 */
	public boolean sameEvent (KeyEventData secondData)
	{
		return keyEvent == secondData.keyEvent &&
				keyCode == secondData.keyCode &&
				keyChar == secondData.keyChar;
	}

	/**
	 *
	 * @param keyCode the key code
	 * @return a key event data for an up event
	 */
	public static KeyEventData upEvent(int keyCode)
    { return new KeyEventData(KeyEvent.KEY_UP, keyCode); }

	/**
	 * @param keyCode the key code
	 * @return a key event data for a down event
	 */
    public static KeyEventData downEvent(int keyCode)
    { return new KeyEventData(KeyEvent.KEY_DOWN, keyCode); }

	/**
	 * @param keyChar the keyboard character
	 * @return a key event data for a typed event
	 */
	public static KeyEventData typedEvent(char keyChar)
    { return new KeyEventData( keyChar); }

	/**
	 * A pool for recycling instances of {@link KeyEventData}
	 */
	public static class DataPool extends Pool<KeyEventData> {
		@Override
		protected KeyEventData newObject()
		{
			return new KeyEventData();
		}
	}
}
