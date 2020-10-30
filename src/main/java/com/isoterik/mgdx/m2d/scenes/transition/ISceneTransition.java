package com.isoterik.mgdx.m2d.scenes.transition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A scene transition animates the entrance and exit of scenes.
 *
 * @author isoteriksoftware
 */
public interface ISceneTransition {
    /**
     *
     * @return the duration (in seconds) this transition will take
     */
    float getDuration();

    /**
     * Renders the current state of the transition.
     * @param batch a sprite batch for rendering
     * @param currentScreen the current scene
     * @param nextScreen the next scene
     * @param alpha the progress of the transition in a range of [0, 1]
     */
    void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha);
}
