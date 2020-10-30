package com.isoterik.mgdx.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isoterik.mgdx.Transform;

/**
 * Provides convenient methods for working with both world units and screen (pixel) units.
 *
 * @author isoteriksoftware
 */
public class WorldUnits {
    private final float unit;
    private final float ppu;
    private final float worldWidth;
    private final float worldHeight;

    /**
     * Creates a new instance given the pixel dimensions of the game world and the pixels per meter.
     * @param pixelsWidth number of horizontal pixels
     * @param pixelsHeight number of vertical pixels
     * @param pixelsPerUnit how many pixels make one world unit
     */
    public WorldUnits(float pixelsWidth, float pixelsHeight,
                      float pixelsPerUnit) {
        ppu = pixelsPerUnit;

        worldWidth = pixelsWidth/ ppu;
        worldHeight = pixelsHeight/ ppu;
        unit = 1/ ppu;
    }

    /**
     * Converts a given pixel value to an equivalent world unit
     * @param pixels the pixels
     * @return the world unit equivalent of the given pixels
     */
    public float toWorldUnit(float pixels)
    { return(unit * pixels); }

    /**
     * Converts a given world unit value to an equivalent pixel value
     * @param unit the world unit
     * @return the pixel equivalent of the given world unit
     */
    public float toPixels(float unit)
    { return(unit * ppu); }

    /**
     * Converts a given pixel coordinate to an equivalent world unit coordinate
     * @param pixelsX pixels on the x-axis
     * @param pixelsY pixels on the y-axis
     * @return the position in world unit
     */
    public Vector2 toWorldUnit(float pixelsX, float pixelsY) {
        return(new Vector2(toWorldUnit(pixelsX),
                toWorldUnit(pixelsY)));
    }

    /**
     *
     * @param region a texture region
     * @return the dimension of the texture region in world unit
     */
    public Vector2 toWorldUnit(TextureRegion region) {
        return(toWorldUnit(region.getRegionWidth(),
                region.getRegionHeight()));
    }

    /**
     * Converts the dimensions and position of a {@link Transform} to world units.
     * @param transform a transform
     */
    public void toWorldTransform(Transform transform) {
        float w = toWorldUnit(transform.size.x);
        float h = toWorldUnit(transform.size.y);
        float d = toWorldUnit(transform.size.z);
        float x = toWorldUnit(transform.position.x);
        float y = toWorldUnit(transform.position.y);
        float z = toWorldUnit(transform.position.z);

        transform.setSize(w, h, d);
        transform.setPosition(x, y, z);
        transform.setOrigin(w * .5f, h * .5f, d * .5f);
    }

    /**
     *
     * @return the number of horizontal pixels in world units.
     */
    public float getWorldWidth()
    { return(worldWidth); }

    /**
     *
     * @return the number of vertical pixels in world units.
     */
    public float getWorldHeight()
    { return(worldHeight); }

    /**
     *
     * @return the number of horizontal pixels
     */
    public float getScreenWidth()
    { return toPixels(worldWidth); }

    /**
     *
     * @return the number of vertical pixels
     */
    public float getScreenHeight()
    { return toPixels(worldHeight); }

    /**
     *
     * @return the number of pixels that makes one world unit
     */
    public float getPixelsPerUnit()
    { return ppu; }
}
