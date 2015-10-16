package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.service.GameService;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;

public class GameScreen implements Screen {
	private final String CLASS_NAME = "GameScreen";
	
	private MainGame game;
	private OrthographicCamera camera;
	private GameService gameService;
	
	public GameScreen(MainGame game) {
		this.game = game;
		game.status = GameStatus.GAME_PLAY;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		Art.load();
		//Particle.load();
		
		gameService = new GameService(game);
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
		
		gameService.update(delta);

		game.batch.end();
		
		game.stage.act(delta);
		game.stage.draw();
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
		gameService.dispose();
		Logger.log(CLASS_NAME, "disposed");
	}
	
}
