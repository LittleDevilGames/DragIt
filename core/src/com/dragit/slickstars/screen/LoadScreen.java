package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.util.Logger;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by slick on 23.11.15.
 */
public class LoadScreen extends BaseScreen {

    private final String CLASS_NAME = "LoadScreen";

    private final int LOAD_TIME = 3000;

    private MainGame game;

    private BitmapFont preloadFont;

    public LoadScreen(MainGame game) {
        super(game);

        this.game = game;
        preloadFont = new BitmapFont(Gdx.files.internal("data" + File.separator + "font" + File.separator + "px.fnt"));
        //startProgressTimer();
        Logger.log(CLASS_NAME, "started");
    }

    private void startProgressTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.log(CLASS_NAME, "loaded");
                game.setGameScreen(new MenuScreen(game));
            }
        }, LOAD_TIME);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.res.update();

        game.batch.begin();
        preloadFont.draw(game.batch, "Loading... (" + game.res.getProgress() + "%)", (game.WIDTH / 2) - (preloadFont.getSpaceWidth() * 2), game.HEIGHT - (game.HEIGHT / 3));
        game.batch.end();

        game.stage.getViewport().setCamera(game.camera);
        game.stage.act(delta);
        game.stage.draw();

        if(game.res.isLoaded) {
            Logger.log(CLASS_NAME, "Assets successfully loaded");
            game.setGameScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    protected void createUI() {

    }

    @Override
    public void dispose() {
        Logger.log(CLASS_NAME, "disposed");
    }
}
