package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A collider in the form of a box. Useful for rectangular game objects.
 *
 * @author isoteriksoftware
 */
public class BoxCollider extends Collider {
	private final Vector2 size;
	private Vector2 center = new Vector2();
	private float angle;

	/**
	 * Creates a new instance given a width and a height
	 * @param width the width of the box
	 * @param height the height of the box
	 */
	public BoxCollider(float width, float height)
	{ size = new Vector2(width, height); }

	/**
	 * Creates a new instance with no dimension. If no dimension is set before the collider is used, it will assume the dimension of its host game object
	 */
	public BoxCollider()
	{ this(0, 0); }

	/**
	 * Sets the size of the game object.
	 * <strong>Note:</strong> this has no effect if this collider has already been used by {@link Physics2d}
	 * @param width the width of the box
	 * @param height the height of the box
	 */
	public void setSize(float width, float height)
	{ size.set(width, height); }

	/**
	 * Sets the size of the game object.
	 * <strong>Note:</strong> this has no effect if this collider has already been used by {@link Physics2d}
	 * @param size the size of the box
	 */
	public void setSize(Vector2 size)
	{ this.size.set(size); }

	/**
	 *
	 * @return the size of the box
	 */
	public Vector2 getSize()
	{ return size; }

	/**
	 * Sets the center of the box in local coordinates.
	 * @param center the center
	 */
	public void setCenter(Vector2 center) {
		this.center = center;
	}

	/**
	 * Sets the center of the box in local coordinates.
	 * @param cx the x-coordinate of the center
	 * @param cy the y-coordinate of the center
	 */
	public void setCenter(float cx, float cy) {
		setCenter(new Vector2(cx, cy));
	}

	/**
	 * Returns the center of the box in local coordinates.
	 * @return the center of the box in local coordinates.
	 */
	public Vector2 getCenter() {
		return center;
	}

	/**
	 * Sets the rotation in radians of the box in local coordinates.
	 * @param angle the rotation in radians of the box in local coordinates.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * Gets the rotation in radians of the box in local coordinates.
	 * @return the rotation in radians of the box in local coordinates.
	 */
	public float getAngle() {
		return angle;
	}

	@Override
	public FixtureDef getFixtureDef() {
		// If the size is zero, assume the size of the host game object
		if (size.isZero())
			size.set(gameObject.transform.size.x, gameObject.transform.size.y);
			
		shape = new PolygonShape();

		((PolygonShape)shape).setAsBox(size.x * 0.5f, size.y * 0.5f, center, angle);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		
		return fdef;
	}
}
