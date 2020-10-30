package com.isoterik.mgdx.m2d.scenes.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * A scene transition that slides scenes
 *
 * @author isoteriksoftware
 */
public class Slide implements ISceneTransition {
    private static final Slide instance =   new Slide();

    private float duration;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;

    /**
     * Creates a new instance given a duration, direction, number of slices and an interpolation
     * @param duration the duration of the transition in seconds
     * @param direction the direction of the transition. Must be one of {@link TransitionDirection#LEFT}, {@link TransitionDirection#RIGHT},
     *                  {@link TransitionDirection#UP}, {@link TransitionDirection#DOWN}
     * @param slideOut if true, the current scene will slide out else the current scene will slide in. The slided out scene will be at the top
     * @param easing an interpolation to use.
     * @return the created instance
     */
    public static Slide init( float duration, int direction,
                              boolean slideOut, Interpolation easing) {
        instance.duration = duration;
        instance.direction = direction;
        instance.slideOut = slideOut;
        instance.easing = easing;

        return instance;
    }

    @Override
    public float getDuration()
    {   return duration;   }

    @Override
    public void render( SpriteBatch batch, Texture currScreen,
                        Texture nextScreen, float alpha) {
        float w = currScreen.getWidth();
        float h = currScreen.getHeight();
        float x = 0;
        float y = 0;

        if (easing != null) alpha = easing.apply(alpha);

        // calculate position offset
        switch (direction) {
            case TransitionDirection.LEFT:
                x = -w * alpha;
                if (!slideOut) x += w;
                break;
            case TransitionDirection.RIGHT:
                x = w * alpha;
                if (!slideOut) x -= w;
                break;
            case TransitionDirection.UP:
                y = h * alpha;
                if (!slideOut) y -= h;
                break;
            case TransitionDirection.DOWN:
                y = -h * alpha;
                if (!slideOut) y += h;
                break;
        }

        // drawing order depends on slide type ('in' or 'out')
        Texture texBottom = slideOut ? nextScreen : currScreen;
        Texture texTop = slideOut ? currScreen : nextScreen;

        // finally, draw both screens
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texBottom, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0,
                currScreen.getWidth(), currScreen.getHeight(),
                false, true);
        batch.draw(texTop, x, y, 0, 0, w, h, 1, 1, 0, 0, 0,
                nextScreen.getWidth(), nextScreen.getHeight(),
                false, true);
        batch.end();
    }
}
