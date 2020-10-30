package com.isoterik.mgdx.m2d.scenes.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * A scene transition that fades out the current scene and fade in the next scene simultaneously.
 *
 * @author isoteriksoftware
 */
public class Fade implements ISceneTransition {
    private static final Fade instance = new Fade();

    private float duration;

    /**
     * Creates a new instance given the duration of the transition.
     * @param duration the duration of the transition in seconds
     * @return the created instance
     */
    public static Fade init(float duration) {
        instance.duration = duration;
        return instance;
    }

    @Override
    public float getDuration()
    { return duration; }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        float w = currScreen.getWidth();
        float h = currScreen.getHeight();
        alpha = Interpolation.fade.apply(alpha);

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, 1 - alpha);
        batch.draw(currScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, currScreen.getWidth(), currScreen.getHeight(), false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, nextScreen.getWidth(), nextScreen.getHeight(), false, true);
        batch.end();
    }
}
