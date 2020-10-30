package com.isoterik.mgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.isoterik.mgdx.m2d.scenes.transition.ISceneTransition;

/**
 * Your starter class must extend this class to make it a minGDX game.
 * <code>MinGdxGame</code> initializes all the core systems and start your game.
 * <p>
 *
 * If you need to override any of the life cycle methods in your starter classes, don't forget to call the implementation of the super class.
 * @author isoteriksoftware
 */
public abstract class MinGdxGame implements ApplicationListener {
    protected MinGdx minGdx;
    protected ISceneTransition splashTransition = null;

    /** Log nothing */
    public static final int LOG_NONE = Application.LOG_NONE;

    /** Log errors only */
    public static final int LOG_ERROR = Application.LOG_ERROR;

    /** Log info only */
    public static final int LOG_INFO = Application.LOG_INFO;

    /** Log everything */
    public static final int LOG_DEBUG = Application.LOG_DEBUG;

    @Override
    public void create() {
        MinGdx.__init();
        minGdx = MinGdx.instance();
        minGdx.setScene(initGame(), splashTransition);
    }

    /**
     * Implement this method to tell minGdx the initial scene of your game. This is where you'll typically initialize your splash scene
     * and return it. You can optionally set {@link #splashTransition} to the transition you want to animate the scene with.
     * @return the initial scene of your game
     */
    protected abstract Scene initGame();

    /**
     * Sets the log level that libGDX uses for logging.
     * @param logLevel the log level. Should be one of {@link #LOG_DEBUG}, {@link #LOG_ERROR}, {@link #LOG_INFO}, {@link #LOG_NONE}
     */
    public void setLogLevel(int logLevel)
    { Gdx.app.setLogLevel(logLevel); }

    @Override
    public void resume()
    { minGdx.__resume(); }

    @Override
    public void pause()
    { minGdx.__pause(); }

    @Override
    public void resize(int width, int height)
    { minGdx.__resize(width, height); }

    @Override
    public void render()
    { minGdx.__render(); }

    @Override
    public void dispose()
    { minGdx.__dispose(); }
}
