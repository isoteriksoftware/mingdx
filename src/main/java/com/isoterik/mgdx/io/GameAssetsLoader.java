package com.isoterik.mgdx.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * For loading and managing game assets. This class uses {@link AssetManager} internally for handling assets.
 * Assets can be loaded either synchronously or asynchronously.
 *
 * @author isoteriksoftware
 */
public final class GameAssetsLoader {
    protected AssetManager assetManager;
    
	protected boolean loadAssetsInBackground;
	protected Runnable onLoadAssets;
	
	private static GameAssetsLoader instance;

	/**
	 * Initialized the loader. DO NOT CALL THIS METHOD
	 */
	public static void __init()
	{ instance = new GameAssetsLoader(); }

	/**
	 *
	 * @return a single instance of the loader
	 */
	public static GameAssetsLoader instance()
	{ return instance; }
	
    private GameAssetsLoader() {
        assetManager = new AssetManager();
        setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

	/**
	 * Sets a custom asset loader. Loaders for common assets like {@link Texture}s, {@link BitmapFont}s etc are already set for you automatically.
	 * Use this to set loaders for your custom asset.
	 * @param assetClass the class of the asset
	 * @param loader the loader
	 * @param <T> the type of asset
	 * @param <P> the type of the loader parameters
	 */
	public <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> assetClass, AssetLoader<T, P> loader) {
		assetManager.setLoader(assetClass, loader);
	}

	/**
	 * Returns the default loader for the given asset class.
	 * @param assetClass the asset class
	 * @param <T> the asset type
	 * @return The loader capable of loading the type, or null if none exists
	 */
	public <T> AssetLoader getLoader(Class<T> assetClass)
	{ return assetManager.getLoader(assetClass); }

	/**
	 * Returns the loader for the given type and the specified filename. If no loader exists for the specific filename, the
	 * 	default loader for that type is returned.
	 * @param assetClass the asset class
	 * @param filePath The filePath of the asset to get a loader for, or null to get the default loader
	 * @param <T> The loader capable of loading the type and filename, or null if none exists
	 * @return the loader for the asset or null if none exists
	 */
	public <T> AssetLoader getLoader(Class<T> assetClass, String filePath)
	{ return assetManager.getLoader(assetClass, filePath); }

	/**
	 * Sets the reference count of an asset.
	 * @param filePath the filePath of the asset
	 * @param refCount the reference count
	 */
	public void setReferenceCount(String filePath, int refCount)
	{ assetManager.setReferenceCount(filePath, refCount); }

	/**
	 *
	 * @param filePath the filePath of the asset
	 * @return the reference count
	 */
	public int getReferenceCount(String filePath)
	{ return assetManager.getReferenceCount(filePath); }

	/**
	 * Sets an {@link AssetErrorListener} to be invoked in case loading an asset failed
	 * @param listener the listener. Can be null
	 */
	public void setErrorListener(AssetErrorListener listener)
	{ assetManager.setErrorListener(listener); }

	/**
	 *
	 * @return the number of loaded assets
	 */
	public int getLoadedAssetsCount()
	{ return assetManager.getLoadedAssets(); }

	/**
	 * Loads the queued assets asynchronously in the background. Takes a runnable that will be executed when the loading is completed.
	 * After calling this method you need to continuously call {@link #update()} to progress loading and invoke the passed runnable when done with loading.
	 * @param onLoadAssets a runnable to invoke the number of loaded assets. Can be null
	 */
	public void loadAssetsInBackground(Runnable onLoadAssets) {
		this.loadAssetsInBackground = true;
		this.onLoadAssets = onLoadAssets;
	}

	/**
	 * Calling this method progresses the loading process.
	 * You'll typically call this method after calling {@link #loadAssetsInBackground}.
	 */
	public void update() {
		if (tickLoader() && loadAssetsInBackground) {
			if (onLoadAssets != null)
				onLoadAssets.run();
				
			loadAssetsInBackground = false;
		}
	}

	/**
	 * Adds an asset to the queue of assets that needs to be loaded.
	 * <strong>Note:</strong> this doesn't load the asset, this simply enqueue the asset, loading is deferred until either {@link #loadAssetsInBackground(Runnable)},
	 * {@link #loadAssetsNow()} or {@link #loadAssetNow(String)} is called.
	 * @param filePath a path to the asset file
	 */
	public void enqueueTexture(String filePath)
	{ enqueueAsset(filePath, Texture.class); }

	/**
	 * A convenient method for enqueuing {@link Music} and {@link Sound} assets inside a folder.
	 * Files with '.mp3' extensions are assumed to be {@link Music} files and others are assumed to be {@link Sound} files.
	 * Assets are not enqueued recursively, only the top level assets are considered. For a recursive approach use {@link #enqueueFolderContents(String, Class)} instead.
	 *
	 * <strong>Note:</strong> this doesn't load the assets, this simply enqueue the assets, loading is deferred until either {@link #loadAssetsInBackground(Runnable)},
	 * {@link #loadAssetsNow()} or {@link #loadAssetNow(String)} is called.
	 * @param folderPath path to the folder containing the assets
	 */
    public void enqueueSfxFolder(String folderPath) {
        for (FileHandle file : Gdx.files.internal(folderPath).list()) {
            Class c = Sound.class;
            if (file.extension().equals("mp3"))
               c = Music.class;
               
            assetManager.load(file.path(), c);
        }
    }

	/**
	 * A convenient method for loading all the similar assets in a folder.
	 * Assets are enqueued recursively so any sub-folder must have assets of a similar type.
	 * <strong>Note:</strong> this doesn't load the assets, this simply enqueue the assets, loading is deferred until either {@link #loadAssetsInBackground(Runnable)},
	 * {@link #loadAssetsNow()} or {@link #loadAssetNow(String)} is called.
	 * @param folderPath path to the folder
	 * @param assetClass the asset class
	 * @param <T> type of asset
	 */
	public <T> void enqueueFolderContents(String folderPath, Class<T> assetClass) {
    	FileHandle dir = Gdx.files.internal(folderPath);

    	if (!dir.isDirectory())
    		assetManager.load(dir.path(), assetClass);
    	else {
			for(FileHandle file : Gdx.files.internal(folderPath).list())
				enqueueFolderContents(file.path(), assetClass);
		}
    }

	/**
	 * Enqueues an asset of the specified type.
	 * @param path path to asset
	 * @param assetClass asset class
	 * @param <T> asset type
	 */
	public <T> void enqueueAsset (String path, Class<T> assetClass)
    { assetManager.load(path, assetClass); }

	/**
	 * Convenient method for enqueueing {@link Skin} assets
	 * @param jsonPath path to the JSON file of the skin
	 */
	public void enqueueSkin (String jsonPath)
    { enqueueAsset(jsonPath, Skin.class); }

	/**
	 * Convenient method for enqueuing {@link TextureAtlas} assets
	 * @param path path to the atlas pack file
	 */
	public void enqueueAtlas(String path)
	{ enqueueAsset(path, TextureAtlas.class); }

	/**
	 * Updates the loader for a single task. Returns if the current task is still being processed or there are no tasks,
	 * otherwise it finishes the current task and starts the next task.
	 * @return true if all loading is finished
	 */
    public boolean tickLoader()
    { return(assetManager.update()); }

	/**
	 *
	 * @return the progress in percent of completion on a scale of [0 - 100]
	 */
	public float getProgressPercentage() {
       float t = assetManager.getProgress();
       return(t * 100);
    }

	/**
	 * Blocks until all queued assets are loaded.
	 * This loads synchronously. For asynchronous loading use {@link #loadAssetsInBackground(Runnable)} instead.
	 */
	public void loadAssetsNow()
    { assetManager.finishLoading(); }

	/**
	 * Blocks until the specified asset is loaded.
	 * This wont load other queued assets. Use {@link #loadAssetsNow()} to load all queued assets.
	 * This loads synchronously. For asynchronous loading use {@link #loadAssetsInBackground(Runnable)} instead.
	 * @param assetName the name/path of the asset file to load
	 */
	public void loadAssetNow(String assetName)
	{ assetManager.finishLoadingAsset(assetName); }

	/**
	 *
	 * @return true when all assets are loaded. Can be called from any thread but note {@link #tickLoader()} or related methods must be called to process tasks
	 */
	public boolean isFinishedLoading()
	{ return assetManager.isFinished(); }

	/**
	 *
	 * @param filePath path to the asset file
	 * @return true if the specified asset is loaded, false otherwise
	 */
	public boolean isLoaded(String filePath)
	{ return assetManager.isLoaded(filePath); }

	/**
	 *
	 * @param filePath path to the asset file
	 * @param assetClass the asset class
	 * @return true if the specified asset is loaded, false otherwise
	 */
	public boolean isLoaded(String filePath, Class assetClass)
	{ return assetManager.isLoaded(filePath, assetClass); }

	/**
	 *
	 * @param filePath path to the asset file
	 * @return true if an asset with the specified name is loading, queued to be loaded, or has been loaded
	 */
	public boolean contains(String filePath)
	{ return assetManager.contains(filePath); }

	/**
	 *
	 * @param filePath path to the asset file
	 * @param assetClass the asset class
	 * @return true if an asset with the specified name is loading, queued to be loaded, or has been loaded
	 */
	public boolean contains(String filePath, Class assetClass)
	{ return assetManager.contains(filePath, assetClass); }

	/**
	 * Convenient method for retrieving loaded {@link Texture}s.
	 * @param name path to the asset file
	 * @param applyLinearFilter if true linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied, else nearest will be applied
	 * @return the texture
	 */
    public Texture getTexture(String name, boolean applyLinearFilter) {
        Texture texture = assetManager.get(name, Texture.class);
        if (texture == null)
        	return null;

        if(applyLinearFilter)
           texture.setFilter( Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear );
        else
           texture.setFilter( Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest );
        return(texture);
    }

	/**
	 * Convenient method for retrieving loaded {@link Texture}s. Linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied
	 * @param name path to the asset file
	 * @return the texture
	 */
	public Texture getTexture(String name)
    { return(getTexture(name, true)); }

	/**
	 * Convenient method for retrieving loaded {@link TextureRegion}s.
	 * @param name path to the asset file
	 * @return the texture region
	 */
    public TextureRegion getRegion(String name)
    { return(assetManager.get(name, TextureRegion.class)); }

	/**
	 * Convenient method for retrieving loaded {@link TextureAtlas}s.
	 * @param name path to the asset file
	 * @return the texture atlas
	 */
    public TextureAtlas getAtlas(String name)
    { return(assetManager.get(name, TextureAtlas.class)); }

	/**
	 * Convenient method for creating {@link TextureRegion}s from loaded {@link Texture}s.
	 * @param name path to the asset file
	 * @param applyLinearFilter if true linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied, else nearest will be applied
	 * @return the texture region
	 */
    public TextureRegion regionForTexture(String name, boolean applyLinearFilter)
    { return(new TextureRegion(getTexture(name, applyLinearFilter))); }

	/**
	 * Convenient method for creating {@link TextureRegion}s from loaded {@link Texture}s. Linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied
	 * @param name path to the asset file
	 * @return the texture region
	 */
    public TextureRegion regionForTexture(String name)
    { return(regionForTexture(name, true)); }

	/**
	 * Convenient method for creating {@link Drawable}s from loaded {@link Texture}s.
	 * @param name path to the asset file
	 * @param applyLinearFilter if true linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied, else nearest will be applied
	 * @return a drawable
	 */
    public Drawable drawableForTexture(String name, boolean applyLinearFilter)
    { return(new TextureRegionDrawable(regionForTexture(name, applyLinearFilter))); }

	/**
	 * Convenient method for creating {@link TextureRegion}s from loaded {@link Texture}s. Linear {@link com.badlogic.gdx.graphics.Texture.TextureFilter} will be applied
	 * @param name path to the asset file
	 * @return a drawable
	 */
	public Drawable drawableForTexture(String name)
    { return drawableForTexture(name, true); }

	/**
	 * Convenient method for creating {@link NinePatchDrawable}s from loaded {@link TextureAtlas}es.
	 * @param regionName name of the region
	 * @param atlasName the name/path of the loaded atlas file
	 * @return a drawable
	 */
	public Drawable patchDrawableForRegion(String regionName, String atlasName)
	{ return new NinePatchDrawable(getAtlas(atlasName).createPatch(regionName)); }

	/**
	 * Convenient method for getting loaded {@link BitmapFont}s
	 * @param name path to the asset file
	 * @return a bitmap font
	 */
    public BitmapFont getFont(String name)
    { return(assetManager.get(name, BitmapFont.class)); }

	/**
	 * Gets a loaded asset
	 * @param path path to the asset file
	 * @param assetClass the asset class
	 * @param <T> the type of asset
	 * @return the asset
	 */
	public <T> T getAsset(String path, Class<T> assetClass)
    { return assetManager.get(path, assetClass); }

	/**
	 * Convenient method for getting loaded {@link Skin} files
	 * @param jsonPath path to the skin json file
	 * @return the skin
	 */
	public Skin getSkin(String jsonPath)
    { return getAsset(jsonPath, Skin.class); }

	/**
	 * Convenient method for getting loaded {@link Music} files
	 * @param filePath path to the asset file
	 * @return the music
	 */
	public Music getMusic(String filePath)
	{ return getAsset(filePath, Music.class); }

	/**
	 * Convenient method for getting loaded {@link Sound} files
	 * @param filePath path to the asset file
	 * @return the sound
	 */
	public Sound getSound(String filePath)
	{ return getAsset(filePath, Sound.class); }

	/**
	 *
	 * @return the asset manager
	 */
	public AssetManager getAssetManager()
	{ return assetManager; }

	/**
	 * This method is called internally to dispose the asset manager. Do not call this method!
	 */
	public void __dispose()
    { assetManager.dispose(); }
}
