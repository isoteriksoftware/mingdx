package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A collider in the form of a box. Useful for rectangular game objects
 *
 * @author isoteriksoftware
 */
public class BoxCollider extends Collider
{
	private Vector2 size;

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

	@Override
	public FixtureDef getFixtureDef()
	{
		// If the size is zero, assume the size of the host game object
		if (size.isZero())
			size.set(gameObject.transform.size.x, gameObject.transform.size.y);
			
		shape = new PolygonShape();
		((PolygonShape)shape).setAsBox(size.x * 0.5f, size.y * 0.5f);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		
		return fdef;
	}
}
