package com.dragit.slickstars.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.io.File;

/**
 * Created by slick on 25.11.15.
 */
public class Res implements Disposable {

    private final String CLASS_NAME = "Res";

    private final String TEXTURE_PATH = "data" + File.separator + "tex" + File.separator;
    private final String FONT_PATH = "data" + File.separator + "font" + File.separator;
    private final String PARTICLE_PATH = "data" + File.separator + "particle" + File.separator;

    private AssetManager manager;
    private ParticleEffectPool effects;

    public ParticleEffect ballParticle;
    public Texture ballTexture;
    public BitmapFont gameFont;
    public ParticleEffect pixelParticle;

    public boolean isLoaded;

    public Res() {
        this.manager = new AssetManager();
        isLoaded = false;
    }

    public void load() {
        Logger.log(CLASS_NAME, "Loading assets..");

        manager.load(PARTICLE_PATH + "ball.p", ParticleEffect.class);
        manager.load(TEXTURE_PATH + "ball.png", Texture.class);
        manager.load(FONT_PATH + "px.fnt", BitmapFont.class);
        manager.load(PARTICLE_PATH + "pixel.p", ParticleEffect.class);
    }

    private void getResources() {
        ballParticle = get(PARTICLE_PATH + "ball.p", ParticleEffect.class);
        pixelParticle = get(PARTICLE_PATH + "pixel.p", ParticleEffect.class);
        ballTexture = get(TEXTURE_PATH + "ball.png", Texture.class);
        gameFont = get(FONT_PATH + "px.fnt", BitmapFont.class);

        effects = new ParticleEffectPool(ballParticle, 15, 50);

        isLoaded = true;
    }

    public void update() {
        if(manager.update() && !isLoaded) {
            getResources();
        }
    }

    public ParticleEffectPool.PooledEffect getBallEffect() {
        return effects.obtain();
    }

    public void freeBallEffect(ParticleEffectPool.PooledEffect effect) {
        if(effect != null) {
            effects.free(effect);
        }
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public AssetManager getManager() {
        return manager;
    }

    public <T> T get(String path, Class<T> type) {
        if(!manager.isLoaded(path)) {
            Logger.log(CLASS_NAME, "Asset (" + path + ") not loaded");
            return null;
        }

        return (T) manager.get(path, type);
    }

    public <T> T load(String path, Class<T> type) {
        if(path.isEmpty()) return null;

        manager.load(path, type);
        return get(path, type);
    }
    @Override
    public void dispose() {
        manager.dispose();
    }
}
