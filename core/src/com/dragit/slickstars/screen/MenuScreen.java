package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;

public class MenuScreen extends BaseScreen implements Screen {
		
	private final String CLASS_NAME = "MenuScreen";
	
	public MenuScreen(MainGame game) {
		super(game);
		
		this.game.status = GameStatus.GAME_NONE;
		
		createUI();
		
		Logger.log(CLASS_NAME, "started");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void createUI() {
		Logger.log(CLASS_NAME, "creating ui");
		float pad = 30f;
		float bh = game.HEIGHT / 2 - pad * 2;
		
		final TextButton start = createButton(CLASS_NAME, "Start", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		final TextButton records = createButton(CLASS_NAME, "Records", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		final TextButton quit = createButton(CLASS_NAME, "Quit", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);

		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.uiGroup.clearChildren();
				game.setGameScreen(new GameScreen(game));
				start.setChecked(false);
			}
		});
		
		records.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.uiGroup.clearChildren();
				game.setGameScreen(new RecordsScreen(game));
				records.setChecked(false);
			}
		});
		
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		game.uiGroup.addActor(start);
		game.uiGroup.addActor(records);
		game.uiGroup.addActor(quit);
		
		//button.addListener(new TextTooltip("This is a tooltip!", skin));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		Font.titleFont.draw(game.batch, game.GAME_TITLE, (game.WIDTH / 2) - 110f, game.HEIGHT - 200f);
		Font.mainFont.draw(game.batch, "Alpha", game.UI_PADDING, game.UI_PADDING);
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
	public void dispose() {
		Logger.log(CLASS_NAME, "disposed");
	}
}
