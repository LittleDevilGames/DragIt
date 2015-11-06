package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;

public class RecordsScreen extends BaseScreen implements Screen {

	private final String CLASS_NAME = "RecordsScreen";
	
	private String scoreList;
	
	public RecordsScreen(MainGame game) {
		super(game);
		
		this.game.status = GameStatus.GAME_NONE;
		
		String delimiter = ".........................";
		scoreList = "Username" + delimiter + "1234567" + "\nUsername" + delimiter + "12345"
				+ "\nUsername" + delimiter + "1234123" + "\nUsername" + delimiter + "1234512345";
		createUI();
		
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
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		Font.titleFont.draw(game.batch, game.GAME_TITLE, (game.WIDTH / 2) - 90f, game.HEIGHT - 200f);
		Font.mainFont.draw(game.batch, "RECORDS", (game.WIDTH / 2) - 45f, game.HEIGHT - 250f);
		Font.mainFont.draw(game.batch, scoreList, 200f, game.HEIGHT - 290f);
		game.batch.end();
		
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
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
		Logger.log(CLASS_NAME, "disposed");
	}

}
