package com.isoterik.mgdx;

public class ActorGameObject extends GameObject {
    public final ActorTransform actorTransform;

    public ActorGameObject(String tag) {
        super(tag);

        components.removeValue(transform, true);
        transform = new ActorTransform();
        transform.__setGameObject(this);
        components.add(transform);

        actorTransform = (ActorTransform) transform;
    }

    public ActorGameObject()
    { this("Untagged"); }

    /**
     * Creates a new {@link ActorGameObject} given a tag.
     * @param tag the tag for the game object
     * @return the created game object
     */
    public static ActorGameObject newInstance(String tag)
    { return new ActorGameObject(tag); }

    /**
     * Creates a new {@link ActorGameObject} using 'Untagged' as the default tag.
     * @return the created game object
     */
    public static ActorGameObject newInstance()
    { return new ActorGameObject(); }

    public ActorTransform transform()
    { return (ActorTransform) transform; }
}
