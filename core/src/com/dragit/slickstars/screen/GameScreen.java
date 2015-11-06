package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.service.GameService;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;

public class GameScreen extends BaseScreen implements Screen {
	private final String CLASS_NAME = "GameScreen";
	
	private GameService gameService;
	
	public GameScreen(MainGame game) {
		super(game);
		
		game.status = GameStatus.GAME_PLAY;

		createUI();
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
		
		game.camera.update();
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		
		if(!MainGame.isPause) {
			gameService.update(delta);
		}
		else {
			Font.mainFont.draw(game.batch, "PAUSE", game.WIDTH / 2, game.HEIGHT / 2);
		}
		
		Font.mainFont.draw(game.batch, "Alpha", game.WIDTH - game.UI_PADDING * 2, game.HEIGHT - game.UI_PADDING * 2);
		game.batch.end();
		
		gameService.render(delta);
		
		game.stage.getViewport().setCamera(game.camera);
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
	protected void createUI() {
		final TextButton pauseBtn = createButton("pause", game.skin, game.WIDTH - game.UI_PADDING * 2, game.HEIGHT - game.UI_PADDING * 2);
		pauseBtn.setVisible(true);
		pauseBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameService.pause(!MainGame.isPause);
				pauseBtn.setChecked(false);
			}
		});
		
		game.stage.addActor(pauseBtn);
	}

	@Override
	public void dispose() {
		gameService.dispose();
		Logger.log(CLASS_NAME, "disposed");
	}
	
}
