package com.isoterik.mgdx.m2d.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;
import com.isoterik.mgdx.GameObject;

/**
 * A Collision2d is generated when the {@link com.badlogic.gdx.physics.box2d.Body} of a {@link GameObject} collides with another {@link com.badlogic.gdx.physics.box2d.Body}.
 * The other {@link com.badlogic.gdx.physics.box2d.Body} may be the body of another {@link GameObject} but not always.
 *
 * @author isoteriksoftware
 */
public final class Collision2d implements Pool.Poolable {
    /**
     * The {@link Contact} instance generated for this collision.
     */
    public Contact contact;

    /**
     * The {@link GameObject} collided with. This will be null if the collision is not with a known game object (like an independent {@link com.badlogic.gdx.physics.box2d.Body})
     */
    public GameObject other;

    /**
     * The fixture of the current game object that collided.
     */
    public Fixture fixture;

    /**
     * The fixture of the other game object that collided.
     */
    public Fixture otherFixture;

    /**
     * Creates a new instance given a contact and the game object collided with
     * @param contact the contact
     * @param other the other game object or null if its not a game object
     */
    public Collision2d(Contact contact, GameObject other) {
        this.contact = contact;
        this.other = other;
    }

    public Collision2d(){}

    /**
     * Compares the tag of the other game object that collided with the current game object. If the other game object is null, false will be returned.
     * @param tag the tag to compare
     * @return true if the two game objects have tags similar to the given one. false otherwise
     */
    public boolean compareTag(String tag) {
        if (other == null)
            return false;

        return other.getTag().equals(tag);
    }

    @Override
    public void reset() {
        contact = null;
        other = null;
        fixture = null;
        otherFixture = null;
    }

    /**
     * A pool for recycling instances of {@link Collision2d}
     */
    public static class CollisionPool extends Pool<Collision2d> {
        @Override
        public Collision2d newObject()
        {
            return new Collision2d();
        }

        public Collision2d obtain(Contact contact, GameObject other, Fixture fixture, Fixture otherFixture) {
            Collision2d collision = obtain();
            collision.contact = contact;
            collision.other = other;
            collision.fixture = fixture;
            collision.otherFixture = otherFixture;

            return collision;
        }
    }
}