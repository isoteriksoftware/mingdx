package com.isoterik.mgdx.m2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isoterik.mgdx.GameCamera;
import com.isoterik.mgdx.MinGdx;
import com.isoterik.mgdx.utils.WorldUnits;

/**
 * A game camera that wraps an {@link OrthographicCamera} for rendering in 2D space.
 *
 * @author isoteriksoftware
 */
public class GameCamera2d extends GameCamera {
    private SpriteBatch spriteBatch;

    private Color backgroundColor;

    /**
     * Creates a new scene given a viewport and an instance of {@link WorldUnits} for unit conversions.
     * <strong>Note:</strong> an {@link OrthographicCamera} will be created.
     * @param viewport the viewport
     * @param worldUnits an instance of {@link WorldUnits}
     */
    public GameCamera2d(Viewport viewport, WorldUnits worldUnits) {
        super(viewport, worldUnits);
        this.spriteBatch = new SpriteBatch();
        this.backgroundColor = new Color(1, 0, 0, 1);

        if (camera == null || !(camera instanceof  OrthographicCamera))
            camera = new OrthographicCamera(worldUnits.getWorldWidth(), worldUnits.getWorldHeight());

        getCamera().position.set(worldUnits.getWorldWidth() * .5f, worldUnits.getWorldHeight() * .5f, 0);
        viewport.setCamera(camera);
    }

    /**
     * Creates a new instance given an instance of {@link WorldUnits} for unit conversions. The viewport defaults to an {@link ExtendViewport}.
     * @param worldUnits an instance of {@link WorldUnits}
     */
    public GameCamera2d(WorldUnits worldUnits) {
        this(new ExtendViewport(worldUnits.getWorldWidth(), worldUnits.getWorldHeight(),
                new OrthographicCamera(worldUnits.getWorldWidth(), worldUnits.getWorldHeight())),
                worldUnits);
    }

    /**
     * Creates a new scene. The screen dimensions defaults to (1280 x 720) taking 100 pixels per meter for unit conversions.
     * The viewport defaults to an {@link ExtendViewport}.
     */
    public GameCamera2d() {
        this(new WorldUnits(MinGdx.instance().defaultSettings.VIEWPORT_WIDTH, MinGdx.instance().defaultSettings.VIEWPORT_HEIGHT,
                MinGdx.instance().defaultSettings.PIXELS_PER_UNIT));
    }

    /**
     * Sets the sprite batch used for rendering
     * @param spriteBatch the sprite batch
     */
    public void setSpriteBatch(SpriteBatch spriteBatch)
    { this.spriteBatch = spriteBatch; }

    /**
     *
     * @return the sprite batch used for rendering
     */
    public SpriteBatch getSpriteBatch()
    { return spriteBatch; }

    /**
     * Sets the color used for clearing the scene every frame.
     * @param backgroundColor color used for clearing the scene every frame
     */
    public void setBackgroundColor(Color backgroundColor)
    { this.backgroundColor = backgroundColor; }

    /**
     *
     * @return color used for clearing the scene every frame
     */
    public Color getBackgroundColor()
    { return backgroundColor; }

    @Override
    public OrthographicCamera getCamera()
    { return (OrthographicCamera)camera; }

    @Override
    public void setup(Viewport viewport, WorldUnits worldUnits) {
        super.setup(viewport, worldUnits);
        camera = new OrthographicCamera(worldUnits.getWorldWidth(), worldUnits.getWorldHeight());
        camera.position.set(worldUnits.getWorldWidth() * .5f, worldUnits.getWorldHeight() * .5f, 0);
        viewport.setCamera(camera);
    }

    @Override
    public void __dispose()
    { spriteBatch.dispose(); }
}





























