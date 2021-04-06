package com.isoterik.mgdx.input;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.isoterik.mgdx.Scene;

/**
 * An instance of this class is used to manage all input-related tasks of a {@link Scene}.
 * It uses {@link ITrigger}s to determine when {@link InputListener}s should be invoked.
 * {@link InputListener}s can be mapped.
 *
 * @author isoteriksoftware
 */
public class InputManager extends InputAdapter implements GestureDetector.GestureListener {
	private final Scene hostScene;

	private final ArrayMap<String, Array<ITrigger>> mappings;
	private final ArrayMap<String, Array<ITouchListener>> mappedTouchListeners;
	private final ArrayMap<String, Array<IKeyListener>> mappedKeyListeners;
	private final ArrayMap<String, Array<IGestureListener>> mappedGestureListeners;

	private final ArrayMap<TouchTrigger, ITouchListener> touchListeners;
	private final ArrayMap<KeyTrigger, IKeyListener> keyListeners;
	private final ArrayMap<GestureTrigger, IGestureListener> gestureListeners;

	private static TouchEventData.DataPool touchDataPool;
	private static KeyEventData.DataPool keyDataPool;
	private static GestureEventData.DataPool gestureDataPool;

	private final InputMultiplexer inputMultiplexer;
	private final GestureDetector gestureDetector;

	private final Vector2 tempVector = new Vector2();
	private final Vector3 tempVector3 = new Vector3();

	/**
	 * An array of supported mouse buttons.
	 * @see Input.Buttons
	 */
	public static final int[] MOUSE_BUTTONS = {
			Input.Buttons.BACK,
			Input.Buttons.FORWARD,
			Input.Buttons.LEFT,
			Input.Buttons.MIDDLE,
			Input.Buttons.RIGHT
	};

	/**
	 * Creates a new instance for a given scene
	 * @param hostScene the scene that this manager gets input data from
	 */
	public InputManager (Scene hostScene) {
		this.hostScene = hostScene;

		mappings = new ArrayMap<>();
		mappedTouchListeners = new ArrayMap<>();
		mappedKeyListeners   = new ArrayMap<>();
		mappedGestureListeners = new ArrayMap<>();

		touchListeners = new ArrayMap<>();
		keyListeners = new ArrayMap<>();
		gestureListeners = new ArrayMap<>();

		inputMultiplexer = new InputMultiplexer();
		gestureDetector = new GestureDetector(this);

		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(gestureDetector);
	}

	/**
	 *
	 * @return the scene that this manager gets input data from
	 */
	public Scene getHostScene()
	{ return hostScene; }

	/**
	 *
	 * @return the input multiplexer
	 */
	public InputMultiplexer getInputMultiplexer()
	{ return inputMultiplexer; }

	/**
	 * Adds a processor to the input multiplexer. Use this method to add other sources of input for the host scene
	 * @param inputProcessor the input processor
	 */
	public void addProcessor (InputProcessor inputProcessor)
	{ inputMultiplexer.addProcessor(inputProcessor); }

	/**
	 *
	 * @return the gesture detector used for gestures
	 */
	public GestureDetector getGestureDetector()
	{ return gestureDetector; }

	/**
	 * Creates a new mapping and map the given triggers.
	 * <strong>Note:</strong> this will not modify existing mappings. If you want to update an existing mapping, use {@link #updateMapping(String, ITrigger, ITrigger...)}
	 * @param mappingName a unique name for the mapping
	 * @param trigger a trigger to map
	 * @param triggers more triggers to map
	 * @return {@code true} if the mapping was created, {@code false} if the mapping already exists
	 */
	public boolean addMapping(String mappingName, ITrigger trigger, ITrigger... triggers) {
		if (hasMapping(mappingName))
			return false;

		Array<ITrigger> items = new Array<>();
		items.add(trigger);
		items.addAll(triggers);

		mappings.put(mappingName, items);
		return true;
	}

	/**
	 * Updates an existing mapping
	 * @param mappingName the name of the mapping to modify
	 * @param trigger a trigger to add
	 * @param triggers more triggers to add
	 * @return {@code true} if the mapping was modified, {@code false} if the mapping doesn't exist
	 */
	public boolean updateMapping(String mappingName, ITrigger trigger, ITrigger... triggers) {
		if (!hasMapping(mappingName))
			return false;

		Array<ITrigger> mappedTriggers = getMapping(mappingName);
		mappedTriggers.add(trigger);
		mappedTriggers.addAll(Array.with(triggers));
		return true;
	}

	/**
	 * Gets a mapping
	 * @param mappingName the name of the mapping
	 * @return an array of triggers for an existing map. {@code null} if the mapping doesn't exist
	 */
	public Array<ITrigger> getMapping (String mappingName) {
		if (!hasMapping(mappingName))
			return null;

		return mappings.get(mappingName);
	}

	/**
	 *
	 * @param mappingName the name of the mapping
	 * @return {@code true} if a mapping exist with this name, {@code false} otherwise
	 */
	public boolean hasMapping (String mappingName)
	{ return mappings.containsKey(mappingName); }

	/**
	 * Removes mapped triggers for a given mapping name.
	 * <strong>Note:</strong> this will not remove the mapping itself. If you want to remove an existing mapping use {@link #removeMapping(String)} instead
	 * @param mappingName the name of the mapping
	 * @return true if the mapped triggers were removed, false otherwise
	 */
	public boolean removedMappedTriggers(String mappingName) {
		if (!hasMapping(mappingName))
			return false;

		getMapping(mappingName).clear();
		clearMappedListeners(mappingName);

		return true;
	}

	/**
	 * Removes all mapped triggers from every mapping
	 */
	public void removeAllMappedTriggers() {
		ArrayMap.Keys<String> keys = mappings.keys();
		for (String key : keys)
			getMapping(key).clear();
	}

	/**
	 * Clears all mapped listeners for a given mapping name
	 * @param mappingName the name of the mapping to clear
	 */
	public void clearMappedListeners(String mappingName) {
		clearMappedTouchListeners(mappingName);
		clearMappedKeyListeners(mappingName);
		clearMappedGestureListeners(mappingName);
	}

	private void clearMappedTouchListeners(String mappingName) {
		if (!mappedTouchListeners.containsKey(mappingName))
			return;

		mappedTouchListeners.get(mappingName).clear();
	}

	private void clearMappedKeyListeners(String mappingName) {
		if (!mappedKeyListeners.containsKey(mappingName))
			return;

		mappedKeyListeners.get(mappingName).clear();
	}

	private void clearMappedGestureListeners(String mappingName) {
		if (!mappedGestureListeners.containsKey(mappingName))
			return;

		mappedGestureListeners.get(mappingName).clear();
	}

	private void removeMappedTouchListeners(String mappingName) {
		if (!mappedTouchListeners.containsKey(mappingName))
			return;

		mappedTouchListeners.removeKey(mappingName);
	}

	private void removeMappedTouchListener(String mappingName, ITouchListener touchListener) {
		if (!mappedTouchListeners.containsKey(mappingName))
			return;

		Array<ITouchListener> listeners =  mappedTouchListeners.get(mappingName);
		listeners.removeValue(touchListener, true);
	}

	private void removeMappedKeyListeners(String mappingName) {
		if (!mappedKeyListeners.containsKey(mappingName))
			return;

		mappedKeyListeners.removeKey(mappingName);
	}

	private void removeMappedKeyListener(String mappingName, IKeyListener keyListener) {
		if (!mappedKeyListeners.containsKey(mappingName))
			return;

		Array<IKeyListener> listeners =  mappedKeyListeners.get(mappingName);
		listeners.removeValue(keyListener, true);
	}

	private void removeMappedGestureListeners(String mappingName) {
		if (!mappedGestureListeners.containsKey(mappingName))
			return;

		mappedGestureListeners.removeKey(mappingName);
	}

	private void removeMappedGestureListener(String mappingName, IGestureListener gestureListener) {
		if (!mappedGestureListeners.containsKey(mappingName))
			return;

		Array<IGestureListener> listeners =  mappedGestureListeners.get(mappingName);
		listeners.removeValue(gestureListener, true);
	}

	/**
	 * Removes a mapped input listener
	 * @param mappingName the mapping to remove from
	 * @param listener the listener to remove
	 */
	public void removeMappedListener(String mappingName, InputListener listener) {
		if (listener instanceof ITouchListener)
			removeMappedTouchListener(mappingName, (ITouchListener)listener);
		else if (listener instanceof IKeyListener)
			removeMappedKeyListener(mappingName, (IKeyListener)listener);
		else if (listener instanceof IGestureListener)
			removeMappedGestureListener(mappingName, (IGestureListener)listener);
	}

	/**
	 * Removes all mapped listeners for a give mapping name
	 * @param mappingName the mapping name
	 */
	public void removeMappedListeners(String mappingName) {
		removeMappedTouchListeners(mappingName);
		removeMappedKeyListeners(mappingName);
		removeMappedGestureListeners(mappingName);
	}

	private void removeAllMappedTouchListeners()
	{ mappedTouchListeners.clear(); }

	private void removeAllMappedKeyListeners()
	{ mappedKeyListeners.clear(); }

	private void removeAllMappedGestureListeners ()
	{ mappedGestureListeners.clear(); }

	/**
	 * Removes all mapped listeners of every mapping.
	 * <strong>Note:</strong>
	 * <ul>
	 *     <li>
	 *         This wont remove mapped triggers. Use {@link #removeAllMappedTriggers()} to remove all mapped triggers.
	 *     </li>
	 *     <li>
	 *         This wont remove the mappings. Use {@link #removeAllMappings()} to remove all mappings
	 *     </li>
	 * </ul>
	 */
	public void removeAllMappedListeners() {
		removeAllMappedTouchListeners();
		removeAllMappedKeyListeners();
		removeAllMappedGestureListeners();
	}

	/**
	 * Removes a mapping
	 * @param mappingName the name of the mapping
	 */
	public void removeMapping(String mappingName) {
		if (!hasMapping(mappingName))
			return;

		mappings.removeKey(mappingName);
	}

	/**
	 * Removes all mappings.
	 */
	public void removeAllMappings()
	{ mappings.clear(); }

	/**
	 * Deactivates mapped triggers for a mapping.
	 * @param mappingName the name of the mapping
	 */
	public void deactivateMappedTriggers(String mappingName)
	{ changeTriggersState(mappingName, false); }

	/**
	 * Deactivates all mapped triggers of a particular type for a given mapping
	 * @param mappingName the name of the mapping
	 * @param triggerClass the type class of the triggers
	 * @param <T> the type of triggers
	 */
	public <T extends ITrigger> void deactivateMappedTriggers(String mappingName, Class<T> triggerClass)
	{ changeTriggersState(mappingName, false, triggerClass); }

	/**
	 * Activates mapped triggers for a mapping.
	 * @param mappingName the name of the mapping
	 */
	public void activateMappedTriggers(String mappingName)
	{ changeTriggersState(mappingName, true); }

	/**
	 * Activates all mapped triggers of a particular type for a given mapping
	 * @param mappingName the name of the mapping
	 * @param triggerClass the type class of the triggers
	 * @param <T> the type of triggers
	 */
	public <T extends ITrigger> void activateMappedTriggers(String mappingName, Class<T> triggerClass)
	{ changeTriggersState(mappingName, true, triggerClass); }

	/**
	 * Deactivates all mapped triggers of all mappings
	 */
	public void deactivateAllMappedTriggers() {
		ArrayMap.Keys<String> keys = mappings.keys();
		for (String key : keys)
			deactivateMappedTriggers(key);
	}

	/**
	 * Deactivates all mapped triggers of a particular type for all mappings
	 * @param triggerClass the type class of the triggers
	 * @param <T> the type of triggers
	 */
	public <T extends ITrigger> void deactivateAllMappedTriggers(Class<T> triggerClass) {
		ArrayMap.Keys<String> keys = mappings.keys();
		for (String key : keys)
			deactivateMappedTriggers(key, triggerClass);
	}

	/**
	 * Activates all mapped triggers of all mappings
	 */
	public void activateAllMappedTriggers() {
		ArrayMap.Keys<String> keys = mappings.keys();
		for (String key : keys)
			activateMappedTriggers(key);
	}

	/**
	 * Activates all mapped triggers of a particular type for all mappings
	 * @param triggerClass the type class of the triggers
	 * @param <T> the type of triggers
	 */
	public <T extends ITrigger> void activateAllMappedTriggers(Class<T> triggerClass) {
		ArrayMap.Keys<String> keys = mappings.keys();
		for (String key : keys)
			activateMappedTriggers(key, triggerClass);
	}

	private void changeTriggersState(String mappingName, boolean active) {
		if (!hasMapping(mappingName))
			return;

		Array<ITrigger> triggers = getMapping(mappingName);
		for (ITrigger trigger : triggers)
			trigger.setActive(active);

	}

	private <T extends ITrigger> void changeTriggersState (String mappingName, boolean active, Class<T> triggerClass) {
		if (!hasMapping(mappingName))
			return;

		Array<ITrigger> triggers = getMapping(mappingName);
		for (ITrigger trigger : triggers)
		{
			if (trigger.getClass() == triggerClass)
				trigger.setActive(active);
		}

	}

	private void mapListener(String mappingName, ITouchListener listener) {
		Array<ITouchListener> listeners =
				mappedTouchListeners.containsKey(mappingName) ?
						mappedTouchListeners.get(mappingName) : new Array<>();
		listeners.add(listener);
		if (!mappedTouchListeners.containsKey(mappingName))
			mappedTouchListeners.put(mappingName, listeners);

	}

	private void mapListener(String mappingName, IKeyListener listener) {
		Array<IKeyListener> listeners =
				mappedKeyListeners.containsKey(mappingName) ?
						mappedKeyListeners.get(mappingName) : new Array<>();
		listeners.add(listener);
		if (!mappedKeyListeners.containsKey(mappingName))
			mappedKeyListeners.put(mappingName, listeners);

	}

	private void mapListener(String mappingName, IGestureListener listener) {
		Array<IGestureListener> listeners =
				mappedGestureListeners.containsKey(mappingName) ?
						mappedGestureListeners.get(mappingName) : new Array<>();
		listeners.add(listener);
		if (!mappedGestureListeners.containsKey(mappingName))
			mappedGestureListeners.put(mappingName, listeners);

	}

	/**
	 * Maps one or more input listeners.
	 * @param mappingName the name of the mapping to map to
	 * @param listener a listener to map
	 * @param listeners more listeners
	 */
	public void mapListener(String mappingName, InputListener listener, InputListener... listeners) {
		if (listener instanceof ITouchListener)
			mapListener(mappingName, (ITouchListener)listener);
		else if (listener instanceof IKeyListener)
			mapListener(mappingName, (IKeyListener)listener);
		else if (listener instanceof IGestureListener)
			mapListener(mappingName, (IGestureListener)listener);

		for (InputListener lst : listeners) {
			if (lst instanceof ITouchListener)
				mapListener(mappingName, (ITouchListener)lst);
			else if (lst instanceof IKeyListener)
				mapListener(mappingName, (IKeyListener)lst);
			else if (lst instanceof IGestureListener)
				mapListener(mappingName, (IGestureListener)lst);
		}
	}

	/**
	 * Returns all mapped key listeners
	 * @return all mapped key listeners
	 */
	public ArrayMap<String, Array<IKeyListener>> getMappedKeyListeners()
	{ return mappedKeyListeners; }

	/**
	 * Returns all mapped key listeners that are mapped to the given mapping name.
	 * @param mappingName the mapping name
	 * @return all mapped key listeners that are mapped to the given mapping name. or null if no such mapping exists
	 */
	public Array<IKeyListener> getMappedKeyListeners(String mappingName) {
		if (!mappedKeyListeners.containsKey(mappingName))
			return null;

		return mappedKeyListeners.get(mappingName);
	}

	/**
	 * Returns all mapped touch listeners
	 * @return all mapped touch listeners
	 */
	public ArrayMap<String, Array<ITouchListener>> getMappedTouchListeners()
	{ return mappedTouchListeners; }

	/**
	 * Returns all mapped touch listeners that are mapped to the given mapping name.
	 * @param mappingName the mapping name
	 * @return all mapped touch listeners that are mapped to the given mapping name. or null if no such mapping exists
	 */
	public Array<ITouchListener> getMappedTouchListeners(String mappingName) {
		if (!mappedTouchListeners.containsKey(mappingName))
			return null;

		return mappedTouchListeners.get(mappingName);
	}

	/**
	 * Returns all mapped gesture listeners
	 * @return all mapped gesture listeners
	 */
	public ArrayMap<String, Array<IGestureListener>> getMappedGestureListeners()
	{ return mappedGestureListeners; }

	/**
	 * Returns all mapped gesture listeners that are mapped to the given mapping name.
	 * @param mappingName the mapping name
	 * @return all mapped gesture listeners that are mapped to the given mapping name. or null if no such mapping exists
	 */
	public Array<IGestureListener> getMappedGestureListeners(String mappingName) {
		if (!mappedGestureListeners.containsKey(mappingName))
			return null;

		return mappedGestureListeners.get(mappingName);
	}

	/**
	 * Adds a listener given a trigger and the listener to add.
	 * <strong>Note:</strong> listeners cannot be mapped this way. Use {@link #mapListener(String, InputListener, InputListener...)} to map listeners
	 * @param trigger a trigger for the listener
	 * @param listener the listener
	 */
	public void addListener(ITrigger trigger, InputListener listener) {
		if (trigger instanceof TouchTrigger && listener instanceof ITouchListener)
			addTouchListener((TouchTrigger)trigger, (ITouchListener)listener);
		if (trigger instanceof KeyTrigger && listener instanceof IKeyListener)
			addKeyListener((KeyTrigger)trigger, (IKeyListener) listener);
		if (trigger instanceof GestureTrigger && listener instanceof IGestureListener)
			addGestureListener((GestureTrigger) trigger, (IGestureListener) listener);
	}

	private void addTouchListener(TouchTrigger trigger, ITouchListener listener) {
		if (touchListeners.containsKey(trigger))
			return;

		touchListeners.put(trigger, listener);
	}

	private void addKeyListener(KeyTrigger trigger, IKeyListener listener) {
		if (keyListeners.containsKey(trigger))
			return;

		keyListeners.put(trigger, listener);
	}

	private void addGestureListener(GestureTrigger trigger, IGestureListener listener) {
		if (gestureListeners.containsKey(trigger))
			return;

		gestureListeners.put(trigger, listener);
	}

	/**
	 * Removes a listener given a trigger that is associated to the listener.
	 * <strong>Note:</strong> this wont remove mapped listener. Use {@link #removeMappedListener(String, InputListener)} to remove mapped listener.
	 * @param trigger the associated trigger
	 */
	public void removeListener(ITrigger trigger) {
		if (trigger instanceof TouchTrigger)
			removeTouchListener((TouchTrigger)trigger);
		if (trigger instanceof KeyTrigger)
			removeKeyListener((KeyTrigger) trigger);
		if (trigger instanceof GestureTrigger)
			removeGestureListener((GestureTrigger)trigger);
	}

	/**
	 * Removes a listener
	 * <strong>Note:</strong> this wont remove mapped listener. Use {@link #removeMappedListener(String, InputListener)} to remove mapped listener.
	 * @param listener the listener to remove
	 */
	public void removeListener(InputListener listener) {
		if (listener instanceof  ITouchListener)
			removeTouchListener((ITouchListener)listener);
		if (listener instanceof  IKeyListener)
			removeKeyListener((IKeyListener) listener);
		if (listener instanceof  IGestureListener)
			removeGestureListener((IGestureListener) listener);
	}

	private void removeTouchListener(TouchTrigger trigger) {
		if (!touchListeners.containsKey(trigger))
			return;

		touchListeners.removeKey(trigger);
	}

	private void removeTouchListener(ITouchListener touchListener)
	{ touchListeners.removeValue(touchListener, true); }

	/**
	 * Removes all touch listeners of a given event type
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 * @param touchEvent the event type
	 */
	public void removeTouchListeners(TouchEventData.TouchEvent touchEvent) {
		ArrayMap.Keys<TouchTrigger> triggers = touchListeners.keys();
		for (TouchTrigger trigger : triggers) {
			if (trigger.touchEventData.sameEvent(touchEvent)) {
				touchListeners.removeKey(trigger);
			}
		}
	}

	/**
	 * Removes all key listeners of a given event type
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 * @param keyEvent the event type
	 */
	public void removeKeyListeners(KeyEventData.KeyEvent keyEvent) {
		ArrayMap.Keys<KeyTrigger> triggers = keyListeners.keys();
		for (KeyTrigger trigger : triggers) {
			if (trigger.keyEventData.sameEvent(keyEvent)) {
				keyListeners.removeKey(trigger);
			}
		}
	}

	/**
	 * Removes all gesture listeners of a given event type
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 * @param gestureEvent the event type
	 */
	public void removeGestureListeners(GestureEventData.GestureEvent gestureEvent) {
		ArrayMap.Keys<GestureTrigger> triggers = gestureListeners.keys();
		for (GestureTrigger trigger : triggers) {
			if (trigger.gestureEventData.sameEvent(gestureEvent)) {
				gestureListeners.removeKey(trigger);
			}
		}
	}

	/**
	 * Removes all touch listeners.
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 */
	public void removeAllTouchListeners()
	{ touchListeners.clear(); }

	private void removeKeyListener(KeyTrigger trigger) {
		if (!keyListeners.containsKey(trigger))
			return;

		keyListeners.removeKey(trigger);
	}

	private void removeKeyListener(IKeyListener keyListener)
	{ keyListeners.removeValue(keyListener, true); }

	/**
	 * Removes all key listeners.
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 */
	public void removeAllKeyListeners ()
	{ keyListeners.clear(); }

	private void removeGestureListener(GestureTrigger trigger) {
		if (!gestureListeners.containsKey(trigger))
			return;

		gestureListeners.removeKey(trigger);
	}

	private void removeGestureListener(IGestureListener gestureListener)
	{ gestureListeners.removeValue(gestureListener, true); }

	/**
	 * Removes all gesture listeners.
	 * <strong>Note:</strong> this wont remove mapped listeners.
	 */
	public void removeAllGestureListeners ()
	{ gestureListeners.clear(); }

	/**
	 * Removes all listeners.
	 * <strong>Note:</strong> this wont remove mapped listeners. Use {@link #removeAllMappedListeners()} to remove all mapped listeners
	 */
	public void removeAllListeners() {
		removeAllTouchListeners();
		removeAllKeyListeners();
		removeAllGestureListeners();
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		Vector2 coords = getWorldCoords(screenX, screenY);

		TouchEventData eventData = obtainTouchEventData();
		eventData.touchEvent = TouchEventData.TouchEvent.TOUCH_UP;
		eventData.touchX = coords.x;
		eventData.touchY = coords.y;
		eventData.pointer = pointer;
		eventData.button = button;

		wakeTouchListeners(eventData);
		wakeMappedTouchListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		Vector2 coords = getWorldCoords(screenX, screenY);

		TouchEventData eventData = obtainTouchEventData();
		eventData.touchEvent = TouchEventData.TouchEvent.TOUCH_DOWN;
		eventData.touchX = coords.x;
		eventData.touchY = coords.y;
		eventData.pointer = pointer;
		eventData.button = button;

		wakeTouchListeners(eventData);
		wakeMappedTouchListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		Vector2 coords = getWorldCoords(screenX, screenY);

		TouchEventData eventData = obtainTouchEventData();
		eventData.touchEvent = TouchEventData.TouchEvent.TOUCH_DRAGGED;
		eventData.touchX = coords.x;
		eventData.touchY = coords.y;
		eventData.pointer = pointer;

		wakeTouchListeners(eventData);
		wakeMappedTouchListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean keyUp (int keyCode) {
		KeyEventData eventData = obtainKeyEventData();
		eventData.keyEvent = KeyEventData.KeyEvent.KEY_UP;
		eventData.keyCode = keyCode;

		wakeKeyListeners(eventData);
		wakeMappedKeyListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.keyUp(keyCode);
	}

	@Override
	public boolean keyDown (int keyCode) {
		KeyEventData eventData = obtainKeyEventData();
		eventData.keyEvent = KeyEventData.KeyEvent.KEY_DOWN;
		eventData.keyCode = keyCode;

		wakeKeyListeners(eventData);
		wakeMappedKeyListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.keyDown(keyCode);
	}

	@Override
	public boolean keyTyped (char keyChar) {
		KeyEventData eventData = obtainKeyEventData();
		eventData.keyEvent = KeyEventData.KeyEvent.KEY_TYPED;
		eventData.keyChar = keyChar;

		wakeKeyListeners(eventData);
		wakeMappedKeyListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return super.keyTyped(keyChar);
	}

	@Override
	public boolean tap (float x, float y, int count, int button) {
		Vector2 coords = getWorldCoords(x, y);

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.TAP;
		eventData.x = coords.x;
		eventData.y = coords.y;
		eventData.tapCount = count;
		eventData.button = button;

		if (count == 2)
			eventData.gestureEvent = GestureEventData.GestureEvent.DOUBLE_TAP;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean fling (float velocityX, float velocityY, int button) {
		Vector2 velocity = getWorldCoords(velocityX, velocityY);

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.FLING;
		eventData.flingVelocityX = velocity.x;
		eventData.flingVelocityY = velocity.y;
		eventData.button = button;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean longPress (float x, float y) {
		Vector2 coords = getWorldCoords(x, y);

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.LONG_PRESS;
		eventData.x = coords.x;
		eventData.y = coords.y;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.PINCH;

		Vector2 worldPoint = getWorldCoords(initialPointer1);
		eventData.initialPinchPointer1 = initialPointer1.set(worldPoint);

		worldPoint = getWorldCoords(initialPointer2);
		eventData.initialPinchPointer2 = initialPointer2.set(worldPoint);

		worldPoint = getWorldCoords(pointer1);
		eventData.pinchPointer1 = pointer1.set(worldPoint);

		worldPoint = getWorldCoords(pointer2);
		eventData.pinchPointer2 = pointer2.set(worldPoint);

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public void pinchStop () {
		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.PINCH_STOP;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);
	}

	@Override
	public boolean pan (float x, float y, float deltaX, float deltaY) {
		Vector2 coords = getWorldCoords(x, y);

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.PAN;
		eventData.x = coords.x;
		eventData.y = coords.y;

		coords = getWorldCoords(deltaX, deltaY);
		eventData.panDeltaX = coords.x;
		eventData.panDeltaY = coords.y;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean panStop (float x, float y, int pointer, int button) {
		Vector2 coords = getWorldCoords(x, y);

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.PAN_STOP;
		eventData.x = coords.x;
		eventData.y = coords.y;
		eventData.pointer = pointer;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean zoom (float initialDistance, float distance) {
		Vector2 worldPoint = getWorldCoords(initialDistance, distance);
		initialDistance = worldPoint.x;
		distance = worldPoint.y;

		GestureEventData eventData = obtainGestureEventData();
		eventData.gestureEvent = GestureEventData.GestureEvent.ZOOM;
		eventData.initialZoomDistance = initialDistance;
		eventData.zoomDistance = distance;
		eventData.zoomFactor = initialDistance / distance;
		eventData.zoomedIn = distance > initialDistance;

		wakeGestureListeners(eventData);
		wakeMappedGestureListeners(eventData);

		// Once all the listeners have been invoked, we need to recycle the event data for later use
		recycleEventData(eventData);

		return false;
	}

	@Override
	public boolean touchDown(float p1, float p2, int p3, int p4)
	{ return false; }

	/**
	 * Converts screen coordinates to world coordinates
	 * @param screenX screen position on the x-axis
	 * @param screenY screen position on the y-axis
	 * @return the coordinates in world units
	 */
	public Vector2 getWorldCoords (float screenX, float screenY) {
		if (hostScene.getMainCamera() == null)
			throw new IllegalStateException("There is no valid MainCamera for the target Scene!");

		tempVector3.set(screenX, screenY, 0);
		Vector3 touch = hostScene.getMainCamera().getCamera()
				.unproject(tempVector3);
		return tempVector.set(touch.x, touch.y);
	}

	/**
	 * Converts screen coordinates to world coordinates
	 * @param screenCoords the screen coordinates
	 * @return the coordinates in world units
	 */
	public Vector2 getWorldCoords(Vector2 screenCoords)
	{ return getWorldCoords(screenCoords.x, screenCoords.y); }

	/**
	 * A convenient method for adding a listener that responds to back presses on Android devices.
	 * @param listener the listener
	 */
	public void addOnBackpressListener(IKeyListener listener)
	{ addListener(KeyTrigger.keyDownTrigger(Keys.BACK), listener); }

	private void wakeMappedTouchListeners(TouchEventData eventData) {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedTouchListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<ITouchListener> listeners = mappedTouchListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || trigger.isPolled())
					continue;

				if (!(trigger instanceof TouchTrigger))
					continue;

				TouchTrigger trigg = (TouchTrigger) trigger;
				if (trigg.touchEventData.sameEvent(eventData)) {
					// trigger fired!
					for (ITouchListener listener : listeners)
						listener.onTouch(mappedKey, eventData);
				}
			}
		}
	}

	private void wakeMappedKeyListeners(KeyEventData eventData) {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedKeyListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<IKeyListener> listeners = mappedKeyListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || trigger.isPolled())
					continue;

				if (!(trigger instanceof KeyTrigger))
					continue;

				KeyTrigger trigg = (KeyTrigger)trigger;
				if (trigg.keyEventData.sameEvent(eventData)) {
					// trigger fired!
					for (IKeyListener listener : listeners)
						listener.onKey(mappedKey, eventData);
				}
			}
		}
	}

	private void wakeMappedGestureListeners(GestureEventData eventData) {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedGestureListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<IGestureListener> listeners = mappedGestureListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || trigger.isPolled())
					continue;

				if (!(trigger instanceof GestureTrigger))
					continue;

				GestureTrigger trigg = (GestureTrigger)trigger;
				if (trigg.gestureEventData.sameEvent(eventData)) {
					// trigger fired!
					for (IGestureListener listener : listeners)
						listener.onGesture(mappedKey, eventData);
				}
			}
		}
	}

	private void wakeTouchListeners(TouchEventData eventData) {
		// Check triggers that fired
		ArrayMap.Keys<TouchTrigger> triggers = touchListeners.keys();
		for (TouchTrigger trigger : triggers) {
			if (!trigger.isActive() || trigger.isPolled())
				continue;

			if (trigger.touchEventData.sameEvent(eventData))
				touchListeners.get(trigger).onTouch(null, eventData);
		}
	}

	private void wakeKeyListeners(KeyEventData eventData) {
		// Check triggers that fired
		ArrayMap.Keys<KeyTrigger> triggers = keyListeners.keys();
		for (KeyTrigger trigger : triggers) {
			if (!trigger.isActive() || trigger.isPolled())
				continue;

			if (trigger.keyEventData.sameEvent(eventData))
				keyListeners.get(trigger).onKey(null, eventData);
		}
	}

	private void wakeGestureListeners(GestureEventData eventData) {
		// Check triggers that fired
		ArrayMap.Keys<GestureTrigger> triggers = gestureListeners.keys();
		for (GestureTrigger trigger : triggers) {
			if (!trigger.isActive() || trigger.isPolled())
				continue;

			if (trigger.gestureEventData.sameEvent(eventData))
				gestureListeners.get(trigger).onGesture(null, eventData);
		}
	}

	private void wakeMappedPolledTouchListeners(TouchEventData eventData) {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedTouchListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<ITouchListener> listeners = mappedTouchListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || !trigger.isPolled())
					continue;

				if (!(trigger instanceof TouchTrigger))
					continue;

				TouchTrigger trigg = (TouchTrigger) trigger;
				if (trigg.touchEventData.sameEvent(eventData)) {
					// trigger fired!
					for (ITouchListener listener : listeners)
						listener.onTouch(mappedKey, eventData);
				}
			}
		}
	}

	private void wakeMappedPolledKeyListeners() {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedKeyListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<IKeyListener> listeners = mappedKeyListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || !trigger.isPolled())
					continue;

				if (!(trigger instanceof KeyTrigger))
					continue;

				KeyTrigger trigg = (KeyTrigger)trigger;
				if (isKeyDown(trigg.keyEventData.keyCode)
						&& trigg.keyEventData.sameEvent(KeyEventData.KeyEvent.KEY_DOWN)) {
					KeyEventData eventData = obtainKeyEventData();
					eventData.keyCode = trigg.keyEventData.keyCode;

					for (IKeyListener listener : listeners)
						listener.onKey(mappedKey, eventData);

					recycleEventData(eventData);
				}
			}
		}
	}

	private void wakeMappedPolledGestureListeners(GestureEventData eventData) {
		// Get the mapped listener keys
		ArrayMap.Keys<String> mappedListenerKeys  = mappedGestureListeners.keys();

		// For every mapped key, get the mapped triggers check if fired (associated event occurred)
		for (String mappedKey : mappedListenerKeys) {
			if (!hasMapping(mappedKey))
				continue;

			// Get the triggers
			Array<ITrigger> triggers = mappings.get(mappedKey);

			// Get the listeners
			Array<IGestureListener> listeners = mappedGestureListeners.get(mappedKey);

			// Check triggers that fired
			for (ITrigger trigger : triggers) {
				if (!trigger.isActive() || !trigger.isPolled())
					continue;

				if (!(trigger instanceof GestureTrigger))
					continue;

				GestureTrigger trigg = (GestureTrigger)trigger;
				if (trigg.gestureEventData.sameEvent(eventData)) {
					// trigger fired!
					for (IGestureListener listener : listeners)
						listener.onGesture(mappedKey, eventData);
				}
			}
		}
	}

	private void wakePolledTouchListeners(TouchEventData eventData) {
		ArrayMap.Keys<TouchTrigger> triggers = touchListeners.keys();
		for (TouchTrigger trigger : triggers) {
			if (!trigger.isActive() || !trigger.isPolled())
				continue;

			if (trigger.touchEventData.sameEvent(eventData))
				touchListeners.get(trigger).onTouch(null, eventData);
		}
	}

	private void wakePolledKeyListeners() {
		ArrayMap.Keys<KeyTrigger> triggers = keyListeners.keys();
		for (KeyTrigger trigger : triggers) {
			if (!trigger.isActive() || !trigger.isPolled())
				continue;

			if (isKeyDown(trigger.keyEventData.keyCode)
					&& trigger.keyEventData.sameEvent(KeyEventData.KeyEvent.KEY_DOWN)) {
				KeyEventData eventData = obtainKeyEventData();
				eventData.keyCode = trigger.keyEventData.keyCode;
				keyListeners.get(trigger).onKey(null, eventData);
				recycleEventData(eventData);
			}
		}
	}

	private void wakePolledGestureListeners(GestureEventData eventData) {
		ArrayMap.Keys<GestureTrigger> triggers = gestureListeners.keys();
		for (GestureTrigger trigger : triggers) {
			if (!trigger.isActive() || !trigger.isPolled())
				continue;

			if (trigger.gestureEventData.sameEvent(eventData))
				gestureListeners.get(trigger).onGesture(null, eventData);
		}
	}

	/**
	 * Called every frame internally by the system. This is where polled triggers gets evaluated and fired if need be.
	 * <strong>Do not call this method explicitly</strong>
	 */
	public void __update() {
		int maxPointers = Gdx.input.getMaxPointers();
		for (int i=0; i < maxPointers; i++) {
			if (isTouched(i)) {
				TouchEventData eventData = obtainTouchEventData();
				eventData.touchEvent = TouchEventData.TouchEvent.TOUCH_DOWN;
				eventData.touchX = getTouchedX(i);
				eventData.touchY = getTouchedY(i);
				eventData.pointer = i;
				for (int button : MOUSE_BUTTONS) {
					if (isMouseJustPressed(button)) {
						eventData.button = button;
						break;
					}
				}

				wakePolledTouchListeners(eventData);
				wakeMappedPolledTouchListeners(eventData);

				// Once all the listeners have been invoked, we need to recycle the event data for later use
				recycleEventData(eventData);
			}
		}

		if (isLongPressed()) {
			GestureEventData eventData = obtainGestureEventData();
			eventData.gestureEvent = GestureEventData.GestureEvent.LONG_PRESS;
			eventData.x = getTouchedX();
			eventData.y = getTouchedY();
			for (int button : MOUSE_BUTTONS) {
				if (isMouseJustPressed(button)) {
					eventData.button = button;
					break;
				}
			}

			wakePolledGestureListeners(eventData);
			wakeMappedPolledGestureListeners(eventData);

			// Once all the listeners have been invoked, we need to recycle the event data for later use
			recycleEventData(eventData);
		}

		if (isPanning()) {
			GestureEventData eventData = obtainGestureEventData();
			eventData.gestureEvent = GestureEventData.GestureEvent.PAN;
			eventData.x = getTouchedX();
			eventData.y = getTouchedY();
			eventData.panDeltaX = getPanDeltaX();
			eventData.panDeltaY = getPanDeltaY();
			for (int button : MOUSE_BUTTONS) {
				if (isMouseJustPressed(button)) {
					eventData.button = button;
					break;
				}
			}

			wakePolledGestureListeners(eventData);
			wakeMappedPolledGestureListeners(eventData);

			// Once all the listeners have been invoked, we need to recycle the event data for later use
			recycleEventData(eventData);
		}

		wakePolledKeyListeners();
		wakeMappedPolledKeyListeners();
	}

	/**
	 *
	 * @param button the button code to check
	 * @see com.badlogic.gdx.Input.Buttons
	 * @return {@code true} if the given button is currently pressed down, {@code false} otherwise
	 */
	public boolean isMouseDown(int button)
	{ return Gdx.input.isButtonPressed(button); }

	/**
	 *
	 * @param button the button code to check
	 * @see com.badlogic.gdx.Input.Buttons
	 * @return {@code true} if the given button was pressed, {@code false} otherwise
	 */
	public boolean isMouseJustPressed(int button)
	{ return Gdx.input.isButtonJustPressed(button); }

	/**
	 *
	 * @param pointer the pointer index for the finger to check
	 * @return {@code true} if the finger represented by the given pointer index or the mouse is currently touching the screen, {@code false} otherwise
	 */
	public boolean isTouched(int pointer)
	{ return Gdx.input.isTouched(pointer); }

	/**
	 *
	 * @return {@code true} if a finger or the mouse is currently touching the screen, {@code false} otherwise
	 */
	public boolean isTouched()
	{ return Gdx.input.isTouched(); }

	/**
	 *
	 * @param keyCode the key code to check
	 * @return whether a particular key is currently pressed down
	 */
	public boolean isKeyDown(int keyCode)
	{ return Gdx.input.isKeyPressed(keyCode); }

	/**
	 *
	 * @param keyCode the key code to check
	 * @return whether a particular key was just pressed
	 */
	public boolean isKeyJustPressed(int keyCode)
	{ return Gdx.input.isKeyJustPressed(keyCode); }

	public boolean isLongPressed(float maxTime)
	{ return gestureDetector.isLongPressed(maxTime); }

	/**
	 *
	 * @return whether the user touched the screen long enough to trigger a long press event.
	 */
	public boolean isLongPressed()
	{ return gestureDetector.isLongPressed(); }

	/**
	 *
	 * @return whether the screen is panned
	 */
	public boolean isPanning()
	{ return gestureDetector.isPanning(); }

	/**
	 *
	 * @param pointer the pointer index for a finger
	 * @return the x-coordinate (in world units) of the point where a touch event occurred
	 */
	public float getTouchedX(int pointer)
	{ return getWorldCoords(Gdx.input.getX(pointer), 0).x; }

	/**
	 *
	 * @return the x-coordinate (in world units) of the point where a touch event occurred
	 */
	public float getTouchedX()
	{ return getTouchedX(0); }

	/**
	 *
	 * @param pointer the pointer index for a finger
	 * @return the y-coordinate (in world units) of the point where a touch event occurred
	 */
	public float getTouchedY(int pointer)
	{ return getWorldCoords(0, Gdx.input.getY(pointer)).y; }

	/**
	 *
	 * @return the y-coordinate (in world units) of the point where a touch event occurred
	 */
	public float getTouchedY()
	{ return getTouchedY(0); }

	/**
	 *
	 * @param pointer the pointer index for a finger
	 * @return the difference between the given pointer location and the last pointer location on the x-axis (in world units).
	 */
	public float getPanDeltaX(int pointer)
	{ return getWorldCoords(Gdx.input.getDeltaX(pointer), 0).x; }

	/**
	 *
	 * @return the difference between the current pointer location and the last pointer location on the x-axis (in world units).
	 */
	public float getPanDeltaX()
	{ return getWorldCoords(Gdx.input.getDeltaX(), 0).x; }

	/**
	 *
	 * @param pointer the pointer index for a finger
	 * @return the difference between the given pointer location and the last pointer location on the y-axis (in world units).
	 */
	public float getPanDeltaY(int pointer)
	{ return getWorldCoords(0, Gdx.input.getDeltaY(pointer)).y; }

	/**
	 *
	 * @return the difference between the current pointer location and the last pointer location on the y-axis (in world units).
	 */
	public float getPanDeltaY()
	{ return getWorldCoords(0, Gdx.input.getDeltaY()).y; }

	/**
	 *
	 * @return acceleration around the x-axis scaled to the range [0, 1]
	 */
	public float getAccelerationX ()
	{ return Gdx.input.getAccelerometerX() / 10f; }

	/**
	 *
	 * @return acceleration around the y-axis scaled to the range [0, 1]
	 */
	public float getAccelerationY ()
	{ return Gdx.input.getAccelerometerY() / 10f; }

	/**
	 *
	 * @return acceleration around the z-axis scaled to the range [0, 1]
	 */
	public float getAccelerationZ ()
	{ return Gdx.input.getAccelerometerZ() / 10f; }

	/**
	 * Determines whether to prevent the BACK (on android devices) from exiting the application when pressed.
	 * This is handy for creating custom behaviors in such scenarios. For example; show confirmation dialog.
	 * <p>
	 * <strong>Note:</strong> This setting affects every instance of {@link InputManager}.
	 * Use {@link #addOnBackpressListener(IKeyListener)} to define custom behaviors.
	 * @param catchBackKey if {@code true} the BACK key will stop exiting the application when pressed
	 */
	public void setCatchBackKey(boolean catchBackKey)
	{ Gdx.input.setCatchKey(KeyCodes.BACK, catchBackKey); }

	/**
	 * Determines whether the mouse cursor is displayed.
	 * <p>
	 * <strong>Note:</strong> This setting affects every instance of {@link InputManager}.
	 * @param cursorCatched if true the cursor won't be displayed, else it displays.
	 */
	public void setCursorCatched(boolean cursorCatched) {
		Gdx.input.setCursorCatched(cursorCatched);
	}

	/**
	 * Sets the current position of the mouse cursor.
	 * <p>
	 * <strong>Note:</strong> This setting affects every instance of {@link InputManager}.
	 * @param x the position on the x-coordinate.
	 * @param y the position on the y-coordinate.
	 */
	public void setCursorPosition(int x, int y) {
		Gdx.input.setCursorPosition(x, y);
	}

	/**
	 * Initializes the internal pools.
	 * This method is called internally by the system. DO NOT CALL THIS METHOD
	 */
	public static void __initPools () {
		touchDataPool = new TouchEventData.DataPool();
		keyDataPool = new KeyEventData.DataPool();
		gestureDataPool = new GestureEventData.DataPool();
	}

	private static TouchEventData obtainTouchEventData () {
		if (touchDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		return touchDataPool.obtain();
	}

	private static KeyEventData obtainKeyEventData () {
		if (keyDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		return keyDataPool.obtain();
	}

	private static GestureEventData obtainGestureEventData () {
		if (gestureDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		return gestureDataPool.obtain();
	}

	private static void recycleEventData  (TouchEventData eventData) {
		if (touchDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		touchDataPool.free(eventData);
	}

	private static void recycleEventData  (KeyEventData eventData) {
		if (keyDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		keyDataPool.free(eventData);
	}

	private static void recycleEventData  (GestureEventData eventData) {
		if (gestureDataPool == null)
			throw new IllegalStateException("The Event Data Pools are not initialized yet!");

		gestureDataPool.free(eventData);
	}
}
