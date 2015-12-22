package com.dragit.slickstars.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.TextUtil;
import com.dragit.slickstars.util.Util;

public class RecordsScreen extends BaseScreen {

	private final String CLASS_NAME = "RecordsScreen";
	private final String RECORDS_LABEL = "RECORDS";

	private StringBuilder scoreList;
	
	public RecordsScreen(MainGame game) {
		super(game);
		
		this.game.status = GameStatus.GAME_NONE;
		this.scoreList = new StringBuilder();
		printScores();
		createUI();
		Logger.log(CLASS_NAME, "started");
	}
	
	private void printScores() {
		String delimiter = ". . . . . . . . . . . . . . . . . . . . .";
		int idx = 1;
		for(int v : game.score.getList()) {
			scoreList.append(idx + delimiter + v + "\n");
			idx++;
		}
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		TextUtil.drawText(gameFont, game.FONT_TITLE_SIZE, Color.SKY, game.GAME_TITLE, (game.WIDTH / 2) - TextUtil.getHalfWidth(gameFont, game.GAME_TITLE), game.HEIGHT - Util.DEFAULT_PADDING * 2, game.batch);
		TextUtil.drawText(gameFont, game.FONT_DEFAULT_SIZE, Color.WHITE, RECORDS_LABEL, (game.WIDTH / 2) - TextUtil.getHalfWidth(gameFont, RECORDS_LABEL), game.HEIGHT - 230f, game.batch);
		TextUtil.drawText(gameFont, game.FONT_MID_SIZE, Color.SKY, scoreList.toString(), 50f, game.HEIGHT - 300f, game.batch, false);
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
		Logger.log(CLASS_NAME, "creating ui");
		final TextButton backButton = createButton(CLASS_NAME, "back", game.skin, (game.WIDTH - game.MIN_BUTTON_WIDTH) - game.UI_PADDING, game.HEIGHT - (game.UI_PADDING * 2), game.MIN_BUTTON_WIDTH, game.MIN_BUTTON_HEIGHT);

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.uiGroup.clearChildren();
				game.setGameScreen(new MenuScreen(game));
				backButton.setChecked(false);
			}
		});
		
		game.uiGroup.addActor(backButton);
	}

	@Override
	public void dispose() {
		Logger.log(CLASS_NAME, "disposed");
	}
}
