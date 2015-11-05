package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Font;
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
		
		createUI();
		
		Logger.log(CLASS_NAME, "started");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}
	
	private void createUI() {
		Logger.log(CLASS_NAME, "creating ui");
		Skin skin = new Skin(Gdx.files.internal(game.UI_SKIN_PATH));
		
		float pad = 30f;
		float bh = game.HEIGHT / 2 - pad * 2;
		
		TextButton start = createButton("Start", skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh);
		TextButton records = createButton("Records", skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad);
		TextButton quit = createButton("Quit", skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad);
		game.stage.addActor(start);
		game.stage.addActor(records);
		game.stage.addActor(quit);
		
		//button.addListener(new TextTooltip("This is a tooltip!", skin));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		Font.titleFont.draw(game.batch, game.GAME_TITLE, (game.WIDTH / 2) - 90f, game.HEIGHT - 200f);
		Font.mainFont.draw(game.batch, "Tap to start", (game.WIDTH / 2) - 45f, (game.HEIGHT / 2) + 40f);
		game.batch.end();
		
		if(Gdx.input.isTouched()) {
			game.setGameScreen(new GameScreen(game));
		}
		
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
	
	public TextButton createButton(String text, Skin skin, float x, float y) {
		TextButton button = new TextButton(text, skin, "toggle");
		button.setBounds(x, y, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		button.padTop(game.BUTTON_HEIGHT / 2);
		return button;
	}

	@Override
	public void dispose() {
		Logger.log(CLASS_NAME, "disposed");
	}
}
