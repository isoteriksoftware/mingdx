package com.isoterik.mgdx.m2d.components.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.isoterik.mgdx.GameCamera;

/**
 * Renders an ellipse shape around the host game object
 *
 * @author isoteriksoftware
 */
public class EllipseDebugRenderer extends DebugRendererBase {
	protected int segments = 20;

	/**
	 * Creates a new instance given a shape type and a color
	 * @param shapeType the shape type
	 * @param color the color
	 */
	public EllipseDebugRenderer(ShapeType shapeType, Color color)
	{ super(shapeType, color); }

	/**
	 * Creates a new instance given a shape type
	 * @param shapeType the shape type
	 */
	public EllipseDebugRenderer(ShapeType shapeType)
	{ super(shapeType); }

	/**
	 * Creates a new instance
	 */
	public EllipseDebugRenderer()
	{ super(); }

	/**
	 * Sets the number of segments used to render the circle
	 * @param segments the number of segments
	 * @return this instance for chaining
	 */
	public EllipseDebugRenderer setSegments(int segments) {
		this.segments = segments;
		return this;
	}

	/**
	 *
	 * @return the number of segments used to render the circle
	 */
	public int getSegments()
	{ return segments; }
	
	@Override
	public EllipseDebugRenderer setColor(Color color) {
		super.setColor(color);
		return this;
	}

	@Override
	public EllipseDebugRenderer setShapeType(ShapeType shapeType) {
		super.setShapeType(shapeType);
		return this;
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer, GameCamera gameCamera) {
		Vector3 pos = gameObject.transform.position;
		Vector3 size = gameObject.transform.size;
		float rotation = gameObject.transform.getRotation();

		shapeRenderer.ellipse(pos.x, pos.y, size.x, size.y, 
			rotation, segments);
	}
}
