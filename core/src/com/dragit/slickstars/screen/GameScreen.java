package com.dragit.slickstars.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.service.GameService;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Util;

public class GameScreen extends BaseScreen {
	private final String CLASS_NAME = getClass().getName();

	private GameService gameService;
	private ParticleEffect pixelParticle;
	
	public GameScreen(MainGame game) {
		super(game);
		
		game.status = GameStatus.GAME_PLAY;
		resetEffects();
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
		super.render(delta);
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		game.batch.setProjectionMatrix(game.camera.combined);

		game.stage.getViewport().setCamera(game.camera);
		game.stage.act(delta);
		game.stage.draw();
		
		if(!MainGame.isPause) {
			game.batch.begin();
			gameService.update(delta);
			game.batch.end();
			
			game.shapeRenderer.begin(ShapeType.Filled);
			gameService.render(delta);
			game.shapeRenderer.end();
		}
		else {
			game.batch.begin();
			Util.drawText(gameFont, game.FONT_MID_SIZE, Color.WHITE, "PAUSE", (game.WIDTH / 2) - (gameFont.getSpaceWidth() * 5), game.HEIGHT / 2, game.batch);
			game.batch.end();
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
	protected void createUI() {
		float bx = (game.WIDTH - (game.MIN_BUTTON_WIDTH * 2)) - (game.UI_PADDING * 2);
		final TextButton pauseBtn = createButton(CLASS_NAME, "pause", game.skin, bx, game.HEIGHT - (game.UI_PADDING * 1.5f), game.MIN_BUTTON_WIDTH, game.MIN_BUTTON_HEIGHT);
		final TextButton menuBtn = createButton(CLASS_NAME, "menu", game.skin, bx += (game.MIN_BUTTON_WIDTH + game.UI_PADDING), game.HEIGHT - (game.UI_PADDING * 1.5f), game.MIN_BUTTON_WIDTH, game.MIN_BUTTON_HEIGHT);
		
		pauseBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameService.pause(!MainGame.isPause);
				pauseBtn.setChecked(false);
			}
		});
		
		menuBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.uiGroup.clearChildren();
				game.ballGroup.clearChildren();
				gameService.dispose();
				game.setGameScreen(new MenuScreen(game));
				menuBtn.setChecked(false);
			}
		});
		game.uiGroup.setZIndex(2);
		game.uiGroup.addActor(pauseBtn);
		game.uiGroup.addActor(menuBtn);
	}

	private void resetEffects() {
		pixelParticle.setPosition(-500, -500);
	}

	@Override
	protected void getResources() {
		super.getResources();
		pixelParticle = game.res.pixelParticle;
	}

	@Override
	public void dispose() {
		gameService.dispose();
		Logger.log(CLASS_NAME, "disposed");
	}
}
