package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Logger;

public class MenuScreen implements Screen {
		
	private final String CLASS_NAME = "MenuScreen";
	
	private MainGame game;
	private OrthographicCamera camera;
	
	public MenuScreen(MainGame game) {
		this.game = game;
		this.game.status = GameStatus.GAME_NONE;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, this.game.WIDTH, this.game.HEIGHT);
		Logger.log(CLASS_NAME, "started");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.font.draw(game.batch, "Main menu screen", game.WIDTH / 2, game.HEIGHT / 2);
		game.batch.end();
		
		if(Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Logger.log(CLASS_NAME, "disposed");
	}
}
