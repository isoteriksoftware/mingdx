package com.isoterik.mgdx.m2d.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.GameCamera;
import com.isoterik.mgdx.m2d.GameCamera2d;
import com.isoterik.mgdx.utils.WorldUnits;

/**
 * {@link com.isoterik.mgdx.GameObject}s that needs to render sprites ({@link Texture} or {@link TextureRegion}) should attach this component.
 *
 * @author isoteriksoftware
 */
public class SpriteRenderer extends Component {
    private TextureRegion sprite;

    private Color color;

    private boolean flipX, flipY;
    private boolean cull;
    private boolean visible;

    private WorldUnits worldUnits;

    private Vector3 temp = new Vector3();

    /**
     * Creates a new instance given a sprite and {@link WorldUnits} to use for converting the sprite dimension to world units.
     * @param sprite an instance of {@link TextureRegion}
     * @param worldUnits an instance of {@link WorldUnits}
     */
    public SpriteRenderer(TextureRegion sprite, WorldUnits worldUnits) {
        this.worldUnits = worldUnits;

        this.sprite = sprite;
        this.color  = Color.WHITE;
        flipX       = false;
        flipY       = false;
        cull        = true;
        visible     = true;
    }

    /**
     * Creates a new instance given a {@link TextureRegion} and {@link WorldUnits} to use for converting the sprite dimension to world units.
     * <strong>Note:</strong> the entire {@link Texture} will be used as the sprite. This constructor isn't useful if the {@link Texture} is a sprite sheet (atlas)
     * @param sprite an instance of {@link Texture}
     * @param worldUnits an instance of {@link WorldUnits}
     */
    public SpriteRenderer(Texture sprite, WorldUnits worldUnits)
    { this(new TextureRegion(sprite), worldUnits); }

    /**
     * Changes the visibility of the sprite.
     * <strong>Note:</strong> this only affects the sprite and not the game object itself
     * @param visible whether sprite should be rendered or not
     */
    public void setVisible(boolean visible)
    { this.visible = visible; }

    /**
     *
     * @return whether sprite is currently rendered or not
     */
    public boolean isVisible()
    { return visible; }

    /**
     * Whether this sprite should be culled. When enabled, sprite will be rendered only if the game object can be seen by the camera.
     * This significantly reduces processor load. It is enabled by default
     * @param cull whether culling should be enabled for this renderer
     */
    public void setCull(boolean cull)
    { this.cull = cull; }

    /**
     *
     * @return whether culling is enabled
     */
    public boolean isCull()
    { return cull; }

    /**
     * Sets the sprite ({@link TextureRegion}) for this renderer. The host game object will be resized to fit the dimensions of the sprite.
     * The {@link WorldUnits} given will be used for converting the sprite dimension to world units.
     * @param sprite an instance of {@link TextureRegion}
     * @param worldUnits an instance of {@link WorldUnits}
     */
    public void setSprite(TextureRegion sprite, WorldUnits worldUnits) {
        this.sprite = sprite;
        this.worldUnits = worldUnits;
        setWorldSize();
    }

    /**
     * Sets the sprite ({@link TextureRegion}) for this renderer. The host game object will be resized to fit the dimensions of the sprite.
     * The default {@link WorldUnits} provided during construction will be used for converting the sprite dimension to world units.
     * @param sprite an instance of {@link TextureRegion}
     */
    public void setSprite(TextureRegion sprite)
    { setSprite(sprite, worldUnits); }

    /**
     *
     * @return the sprite used for rendering
     */
    public TextureRegion getSprite()
    { return sprite; }

    /**
     * Use this to tint the color of the rendered sprite. Setting the color to {@link Color#WHITE} renders the original sprite with no tint
     * @param color the color to used for tinting the sprite
     */
    public void setColor(Color color)
    { this.color = color; }

    /**
     *
     * @return the color to used for tinting the sprite
     */
    public Color getColor()
    { return color; }

    /**
     * Sets the opacity of the rendered sprite on a scale of (0 - 1) where 0 means 0% opaque (completely transparent) and 1 means 100% opaque
     * @param opacity the opacity
     */
    public void setOpacity(float opacity)
    { color.a = opacity; }

    /**
     *
     * @return the opacity of the rendered sprite
     */
    public float getOpacity()
    { return color.a; }

    /**
     * Determines whether the sprite should be flipped horizontally. Useful for mirroring sprites
     * @param flipX whether the sprite should be flipped horizontally
     */
    public void setFlipX(boolean flipX)
    { this.flipX = flipX; }

    /**
     *
     * @return whether the sprite is flipped horizontally
     */
    public boolean isFlipX()
    { return flipX; }

    /**
     * Determines whether the sprite should be flipped vertically. Useful for mirroring sprites
     * @param flipY whether the sprite should be flipped vertically
     */
    public void setFlipY(boolean flipY)
    { this.flipY = flipY; }

    /**
     *
     * @return whether the sprite is flipped vertically
     */
    public boolean isFlipY()
    { return flipY; }

    /**
     * Converts the dimensions of the sprite to world units then use it as the dimension for the host game object.
     * The origin is also shifted to the center of the game object.
     * No change is made if there is currently no host game object.
     */
    public void setWorldSize() {
        if (gameObject == null)
            return;

        Vector2 worldSize = worldUnits.toWorldUnit(sprite);
        gameObject.transform.size.set(worldSize, 0);
        gameObject.transform.origin.set(worldSize.x * .5f,
                worldSize.y * .5f, 0);
    }

    /**
     * Once this component is attached, the host game object will be resized to fit the dimensions of the sprite.
     * {@inheritDoc}
     */
    @Override
    public void attach() {
        // Setup the world size of this sprite once we have a game object
        setWorldSize();
    }

    @Override
    public void render(GameCamera gameCamera) {
        // Render only if visible
        if (!visible)
            return;

        // If culling, the sprite should be rendered only if it can be seen by the camera
        if (cull) {
            if (gameObject.transform.isInCameraFrustum(gameCamera.getCamera())) {
                drawSprite(gameCamera);
            }
        }
        else {
            drawSprite(gameCamera);
        }
    }

    /**
     * Renders the sprite to the screen.
     * @param gameCamera the camera used by the scene where the host game object resides
     */
    protected void drawSprite(GameCamera gameCamera) {
        // The game camera must be a GameCamera2d instance
        if (!(gameCamera instanceof GameCamera2d))
            return;

        SpriteBatch batch = ((GameCamera2d)gameCamera).getSpriteBatch();
        batch.setColor(color);

        Vector3 pos    = temp.set(gameObject.transform.position);
        Vector3 size   = gameObject.transform.size;
        Vector3 scale  = gameObject.transform.scale;
        Vector3 origin = gameObject.transform.origin;
        float rotation = gameObject.transform.getRotation();

        // Draw the sprite
        batch.draw(sprite.getTexture(),
                pos.x, pos.y, origin.x, origin.y,
                size.x, size.y, scale.x, scale.y,
                rotation, sprite.getRegionX(), sprite.getRegionY(),
                sprite.getRegionWidth(), sprite.getRegionHeight(),
                flipX, flipY);
    }
}
