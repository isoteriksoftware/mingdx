package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A collider in the form of a convex polygon. Useful for oddly shaped game objects.
 *
 * @author isoteriksoftware
 */
public class PolygonCollider extends Collider {
	private float[] vertices;

	/**
	 * Creates a new instance given the vertices of the polygon. It is assumed the vertices are in x,y order and define a convex polygon. It is
	 * assumed that the exterior is the the right of each edge.
	 * @param vertices the vertices of the polygon
	 * @throws IllegalArgumentException if the vertices array is null or empty
	 */
	public PolygonCollider(float[] vertices) throws IllegalArgumentException {
		if (vertices == null || vertices.length == 0)
			throw new IllegalArgumentException("Vertices are required!");
			
		this.vertices = vertices;
	}
	
	@Override
	public FixtureDef getFixtureDef() {
		shape = new PolygonShape();
		((PolygonShape)shape).set(vertices);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;

		return fdef;
	}
}
