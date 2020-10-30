package com.isoterik.mgdx.m2d.scenes.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * A scene transition that splits the current scene in half
 *
 * @author isoteriksoftware
 */
public class Split implements ISceneTransition {
    private static Split instance = new Split();

    private float duration;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;

    /**
     * Creates a new instance given a duration, direction, number of slices and an interpolation
     * @param duration the duration of the transition in seconds
     * @param direction the direction of the transition. Must be one of {@link TransitionDirection#LEFT_RIGHT}, {@link TransitionDirection#UP_DOWN},
     *                  {@link TransitionDirection#LEFT_RIGHT_DIAGONAL}, {@link TransitionDirection#UP_DOWN_DIAGONAL}
     * @param slideOut if true, the current scene will slide out else the current scene will slide in. The slided out scene will be at the top
     * @param easing an interpolation to use.
     * @return the created instance
     */
    public static Split init( float duration, int direction,
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
        float x1 = 0;
        float x2 = 0;
        float y1 = 0;
        float y2 = 0;

        if (easing != null)
            alpha = easing.apply(alpha);

        // calculate position offset
        switch (direction) {
            case TransitionDirection.LEFT_RIGHT:
                x1 = -w/2 * alpha;
                x2 =  (w/2 * alpha) + w/2;
                if(!slideOut) {
                    x1 = (w/2 * alpha) + w/2;
                    x2 = -w/2 * alpha;
                }
                break;
            case TransitionDirection.UP_DOWN:
                y1 = -h/2 * alpha;
                y2 = (h/2 * alpha) + h/2;
                if(!slideOut) {
                    y1 = (h/2 * alpha) + h/2;
                    y2 = -h/2 * alpha;
                }
                break;
            case TransitionDirection.LEFT_RIGHT_DIAGONAL:
            case TransitionDirection.UP_DOWN_DIAGONAL:
                x1 = -w/2 * alpha;
                x2 =  (w/2 * alpha) + w/2;
                if(!slideOut) {
                    x1 = (w/2 * alpha) + w/2;
                    x2 = -w/2 * alpha;
                }
                y1 = -h/2 * alpha;
                y2 = (h/2 * alpha) + h/2;
                if(!slideOut) {
                    y1 = (h/2 * alpha) + h/2;
                    y2 = -h/2 * alpha;
                }
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

        if(direction == TransitionDirection.LEFT_RIGHT
                || direction == TransitionDirection.LEFT_RIGHT_DIAGONAL) {
            //draw the first half
            batch.draw(texTop, x1, y1, 0, 0, w/2, h, 1, 1, 0, 0, 0,
                    currScreen.getWidth()/2, currScreen.getHeight(),
                    false, true);
            //draw the second half
            batch.draw(texTop, x2, y2, 0, 0, w/2, h, 1, 1, 0,
                    /*srcX, srcY*/   (int)w/2, 0,
                    currScreen.getWidth()/2, currScreen.getHeight(),
                    false, true);
        }
        else {
            //draw the first half
            batch.draw(texTop, x1, y1, 0, 0, w, h/2, 1, 1, 0, 0, 0,
                    currScreen.getWidth(), currScreen.getHeight()/2,
                    false, true);
            //draw the second half
            batch.draw(texTop, x2, y2, 0, 0, w, h/2, 1, 1, 0,
                    /*srcX, srcY*/   0, (int)h/2,
                    currScreen.getWidth(), currScreen.getHeight()/2,
                    false, true);
        }
        batch.end();
    }
}