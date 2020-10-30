package com.isoterik.mgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.m2d.scenes.transition.ISceneTransition;

/**
 * A SceneManager manages the scenes in a game. It maintains a stack of {@link Scene#setStackable(boolean) stackable} scenes that can be revisited using {@link #revertToPreviousScene(ISceneTransition)}.
 * Scenes can be transitioned immediately or using a {@link ISceneTransition} to animate the transition.
 *
 * @author isoteriksoftware
 */
public final class SceneManager {
    private boolean init;
    private Scene currScene;
    private Scene nextScene;
    private Scene prevScene;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float elapsedTime;
    private ISceneTransition sceneTransition;

    private Array<Scene> sceneStack
            = new Array<>();

    private static SceneManager instance;

    /**
     * Initializes the manager.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public static void __init()
    { instance = new SceneManager(); }

    /**
     *
     * @return a reference to the singleton instance
     */
    public static SceneManager instance()
    { return instance; }

    private SceneManager() {}

    /**
     * Make the given scene the current scene displayed without any transition animation.
     * @param scene the scene to transition to.
     */
    public void setCurrentScene(Scene scene)
    { setCurrentScene(scene, null); }

    /**
     * Make the given the scene the current scene displayed. The transition is animated if a non-null {@link ISceneTransition} is given.
     * @param scene the scene to transition to.
     * @param sceneTransition a scene transition to animate the transition with. Can be null
     */
    public void setCurrentScene (Scene scene, ISceneTransition sceneTransition) {
        if (!init) {
            initVars();
            init = true;
        }

        // start new transition
        nextScene = scene;
        nextScene.__resume(); // activate next scene
        nextScene.__update(0); // let next scene update() once
        nextScene.__render();

        if (currScene != null)
            currScene.pauseForTransition();

        Gdx.input.setInputProcessor(null); // disable input
        this.sceneTransition = sceneTransition;
        elapsedTime = 0;

        prevScene = currScene;

        if (scene.isStackable())
            pushScene(scene);
    }

    private void initVars() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        if(currFbo != null) {
            currFbo.dispose();
            nextFbo.dispose();
            batch.dispose();
        }

        currFbo = new FrameBuffer(Format.RGB888, w, h, false);
        nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
        batch = new SpriteBatch();
    }

    /**
     * Called when the game needs to render. Frame rate is capped at 60 frames per second.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __render() {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = MinGdx.instance().getDeltaTime();

        // update the AI clock
        GdxAI.getTimepiece().update(deltaTime);

        if (nextScene == null) {    // no ongoing transition
            if (currScene != null) {
                currScene.__update(deltaTime);
                currScene.__render();
            }
        }
        else {    // ongoing transition
            float duration = 0;
            if (sceneTransition != null)
                duration = sceneTransition.getDuration();

            // update progress of ongoing transition
            elapsedTime = Math.min(elapsedTime + deltaTime, duration);

            if (sceneTransition == null || elapsedTime >= duration) {
                //no transition effect set or transition has just finished
                if (currScene != null)
                    currScene.__pause();
                nextScene.__resume();

                // enable input for next screen
                Gdx.input.setInputProcessor(nextScene.getInputManager().getInputMultiplexer());

                //notify the scenes of
                //transition completion
                if(sceneTransition != null) {
                    if (currScene != null)
                        currScene.transitionedFromThisScene(nextScene);
                    if (nextScene != null)
                        nextScene.transitionedToThisScene(currScene);
                }

                // dispose the current scene if it cannot be stacked
                if (currScene != null && !currScene.isStackable())
                    currScene.__destroy();

                // switch screens
                currScene = nextScene;
                nextScene = null;
                sceneTransition = null;
            }
            else {
                // render screens to FBOs
                currFbo.begin();
                if (currScene != null) {
                    currScene.__render();
                }

                currFbo.end();
                nextFbo.begin();
                nextScene.__render();
                nextFbo.end();

                // render transition effect to screen
                float alpha = elapsedTime / duration;
                sceneTransition.render(batch,   currFbo.getColorBufferTexture(), nextFbo.getColorBufferTexture(),   alpha);
            }
        }
    }

    /**
     * Called when the game should pause.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __pause() {
        if (currScene != null)
            currScene.__pause();
    }

    /**
     * Called when the screen is resized.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     * @param width the new width
     * @param height the new height
     */
    public void __resize(int width, int height) {
        if (currScene != null)
            currScene.__resize(width, height);
    }

    /**
     * Called when the game should resume.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __resume() {
        if (currScene != null) {
            currScene.__resume();
            Gdx.input.setInputProcessor(currScene.getInputManager().getInputMultiplexer());
        }
    }

    /**
     * Called when the game is getting destroyed.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __dispose () {
        if (currScene != null)
            currScene.__destroy();
        if (nextScene != null)
            nextScene.__destroy();

        if (sceneStack.contains(currScene, true))
            sceneStack.removeValue(currScene, true);

        if (sceneStack.contains(nextScene, true))
            sceneStack.removeValue(nextScene, true);

        for (Scene scene : sceneStack)
            scene.__destroy();

        sceneStack.clear();

        if (init) {
            currFbo.dispose();
            currScene = null;
            nextFbo.dispose();
            nextScene = null;
            batch.dispose();
            init = false;
        }
    }

    /**
     *
     * @return the previous scene. null if there is no previous scene.
     */
    public Scene getPreviousScene()
    { return(prevScene); }

    /**
     *
     * @return the current scene. null if there is no current scene.
     */
    public Scene getCurrentScene()
    { return currScene; }

    /**
     * Reverts to the previous scene.
     * <strong>Note:</strong> Only {@link Scene#setStackable(boolean) stackable} scenes can be reverted to.
     * @param transition a transition animation to use. can be null
     */
    public void revertToPreviousScene(ISceneTransition transition) {
        if(!canRevertToPreviousScene())
            return;

        // remove the current scene from the stack
        popScene();

        // get the new current scene
        Scene temp = popScene();

        if(temp == null)
            return;

        setCurrentScene(temp, transition);
    }

    /**
     * Reverts to the previous scene.
     */
    public void revertToPreviousScene()
    { revertToPreviousScene(null); }

    /**
     * Adds a scene to the stack
     * @param scene a scene to add
     */
    protected void pushScene(Scene scene) {
        if(sceneStack.contains(scene, true))
            return;

        sceneStack.add(scene);
    }

    /**
     *
     * @return a scene removed from the bottom of the stack
     */
    protected Scene popScene() {
        if(sceneStack.isEmpty())
            return null;

        return sceneStack.pop();
    }

    /**
     *
     * @return whether it's possible to revert to a previous scene
     */
    public boolean canRevertToPreviousScene()
    { return(sceneStack.size >= 2); }
}
