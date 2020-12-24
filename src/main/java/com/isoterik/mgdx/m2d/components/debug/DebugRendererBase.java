package com.isoterik.mgdx.m2d.components.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.GameCamera;

/**
 * The base class for all debug renderers.
 *
 * @author isoteriksoftware
 */
public abstract class DebugRendererBase extends Component {
	public static final ShapeRenderer.ShapeType LINE_SHAPE
		= ShapeRenderer.ShapeType.Line;
	public static final ShapeRenderer.ShapeType FILLED_SHAPE
		= ShapeRenderer.ShapeType.Filled;
	public static final ShapeRenderer.ShapeType POINT_SHAPE
		= ShapeRenderer.ShapeType.Point;
		
	protected ShapeRenderer.ShapeType shapeType;
	
	protected Color color;

	public DebugRendererBase(ShapeRenderer.ShapeType shapeType, Color color) {
		this.shapeType = shapeType;
		this.color = color;
	}
	
	public DebugRendererBase(ShapeRenderer.ShapeType shapeType) {
		this(shapeType, Color.WHITE);
	}
	
	public DebugRendererBase() {
		this(LINE_SHAPE);
	}

	/**
	 * Sets the color to use for rendering
	 * @param color the color
	 * @return this instance for chaining
	 */
	public DebugRendererBase setColor(Color color) {
		this.color = color;
		return this;
	}

	/**
	 *
	 * @return the color used for rendering
	 */
	public Color getColor()
	{ return color; }

	/**
	 * Sets the shape type to used for rendering
	 * @param shapeType the shape type
	 * @return this instance for chaining
	 */
	public DebugRendererBase setShapeType(ShapeRenderer.ShapeType shapeType) {
		this.shapeType = shapeType;
		return this;
	}

	/**
	 *
	 * @return the shape type to used for rendering
	 */
	public ShapeRenderer.ShapeType getShapeType()
	{ return shapeType; }

	@Override
	public void drawDebugLine(ShapeRenderer shapeRenderer, GameCamera gameCamera) {
		if (shapeType == LINE_SHAPE) {
			shapeRenderer.setColor(color);
			draw(shapeRenderer, gameCamera);
		}
	}

	@Override
	public void drawDebugFilled(ShapeRenderer shapeRenderer, GameCamera gameCamera) {
		if (shapeType == FILLED_SHAPE) {
			shapeRenderer.setColor(color);
			draw(shapeRenderer, gameCamera);
		}
	}

	@Override
	public void drawDebugPoint(ShapeRenderer shapeRenderer, GameCamera gameCamera) {
		if (shapeType == POINT_SHAPE) {
			shapeRenderer.setColor(color);
			draw(shapeRenderer, gameCamera);
		}
	}

	/**
	 * Concrete subclasses must implement this method to define how they are rendered.
	 * <strong>Note:</strong> this method is only called for shape type supported by the subclass. This means a subclass can disallow change of shape type if it
	 * only supports a particular type of shape
	 * @param shapeRenderer a {@link ShapeRenderer} that can be used for rendering shapes
	 * @param gameCamera the game camera provided by the scene where the host game object resides
	 */
	public abstract void draw(ShapeRenderer shapeRenderer,
		GameCamera gameCamera);
}
