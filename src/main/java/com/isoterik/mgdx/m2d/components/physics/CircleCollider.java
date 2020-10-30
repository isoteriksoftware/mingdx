package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * A collider in the form of a circle. Useful for circular game objects
 *
 * @author isoteriksoftware
 */
public class CircleCollider extends Collider {
	private float radius;
	private Vector2 position;

	/**
	 * Creates a new instance given a radius and a position on the {@link com.badlogic.gdx.physics.box2d.Body} to place the shape at
	 * @param radius the radius of the circle
	 * @param x the x-coordinate of the position of the circle
	 * @param y the y-coordinate of the position of the circle
	 */
	public CircleCollider(float radius, float x, float y) {
		position = new Vector2(x, y);
		this.radius = radius;
	}

	/**
	 * Creates a new instance given a radius. Position defaults to (0, 0)
	 * @param radius the radius of the circle
	 */
	public CircleCollider(float radius)
	{ this(radius, 0, 0); }

	/**
	 * Creates a new instance with no radius. If no valid radius is set before this collider gets used by {@link Physics2d}, the collider will assume the radius of
	 * the host game object
	 */
	public CircleCollider()
	{ this(-1f); }

	/**
	 * Sets the radius of the circle
	 * @param radius the radius of the circle
	 */
	public void setRadius(float radius)
	{ this.radius = radius; }

	/**
	 *
	 * @return the radius of the circle
	 */
	public float getRadius()
	{ return radius; }

	@Override
	public FixtureDef getFixtureDef() {
		// Assumes the radius of the host game object if the radius is <= 0
		if (radius <= 0)
			radius = gameObject.transform.size.x * .5f;

		shape = new CircleShape();
		shape.setRadius(radius);
		((CircleShape)shape).setPosition(position);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		return fdef;
	}
}
