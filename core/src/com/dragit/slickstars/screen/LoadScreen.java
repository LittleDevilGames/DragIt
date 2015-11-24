package com.dragit.slickstars.screen;

import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by slick on 23.11.15.
 */
public class LoadScreen extends BaseScreen {

    private final String CLASS_NAME = "LoadScreen";

    private final int LOAD_TIME = 3000;

    private MainGame game;

    public LoadScreen(MainGame game) {
        super(game);

        this.game = game;
        startProgressTimer();
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

        game.batch.begin();
        Font.mainFont.draw(game.batch, "Loading...", (game.WIDTH / 2) - (Font.mainFont.getSpaceWidth() * 2), game.HEIGHT - (game.HEIGHT / 3));
        game.batch.end();

        game.stage.getViewport().setCamera(game.camera);
        game.stage.act(delta);
        game.stage.draw();
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
