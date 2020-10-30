package com.isoterik.mgdx;

import com.badlogic.gdx.utils.Array;

/**
 * Layers are be used to group {@link GameObject}s of a {@link Scene}.
 *
 * @author isoteriksoftware
 */
public class Layer {
    private Array<GameObject> gameObjects;

    private String name;

    /**
     * Creates a new layer with a given name.
     * @param name a name for the layer
     */
    public Layer(String name) {
        this.name = name;

        gameObjects = new Array<>();
    }

    /**
     * Adds a game object to this layer
     * @param gameObject the game object to add
     */
    public void addGameObject(GameObject gameObject)
    { gameObjects.add(gameObject); }

    /**
     * Removes a game object from this layer
     * @param gameObject the game object to remove
     * @return true if the game object was removed. false otherwise
     */
    public boolean removeGameObject(GameObject gameObject)
    { return gameObjects.removeValue(gameObject, true); }

    /**
     *
     * @return the game objects of this layer
     */
    public Array<GameObject> getGameObjects()
    { return gameObjects; }

    /**
     * Sets a name for this layer.
     * @param name the name
     */
    public void setName(String name)
    { this.name = name; }

    /**
     *
     * @return the name of this layer.
     */
    public String getName()
    { return name; }
}
