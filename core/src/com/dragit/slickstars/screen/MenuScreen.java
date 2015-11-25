package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dragit.slickstars.entity.Effect;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Particle;
import com.dragit.slickstars.util.Util;

import java.util.Timer;
import java.util.TimerTask;

import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

public class MenuScreen extends BaseScreen {
		
	private final String CLASS_NAME = "MenuScreen";
	private GDXButtonDialog exitDialog;
	private Timer effectsTimer;

	public MenuScreen(MainGame game) {
		super(game);
		
		this.game.status = GameStatus.GAME_NONE;
		createUI();

		this.effectsTimer = new Timer();

		createEffects();
		moveEffects();

		Logger.log(CLASS_NAME, "started");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub2
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		game.batch.setProjectionMatrix(game.camera.combined);

		game.stage.getViewport().setCamera(game.camera);
		game.stage.act(delta);
		game.stage.draw();

		game.batch.begin();
		Font.titleFont.draw(game.batch, game.GAME_TITLE, (game.WIDTH / 2) - (game.UI_LABEL_OFFSET * 4), game.HEIGHT - 150f);
		Font.mainFont.draw(game.batch, "ver. " + game.VERSION, game.UI_PADDING, game.UI_PADDING);
		game.batch.end();
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
		float pad = 30f;
		float bh = game.HEIGHT / 1.7f - pad * 2;
		
		final TextButton start = createButton(CLASS_NAME, "Start", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		final TextButton records = createButton(CLASS_NAME, "Records", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		final TextButton quit = createButton(CLASS_NAME, "Quit", game.skin, (game.WIDTH / 2) - (game.BUTTON_WIDTH / 2), bh -= game.BUTTON_HEIGHT + pad, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);

		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.uiGroup.clearChildren();
				dispose();
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
				exitDialog = game.dialogs.newDialog(GDXButtonDialog.class);
				exitDialog.setTitle("Quit");
				exitDialog.setMessage("Do you want quit?");

				exitDialog.setClickListener(new ButtonClickListener() {
					@Override
					public void click(int button) {
						if (button == 0) {
							Gdx.app.exit();
						} else {
							exitDialog.dismiss();
						}
					}
				});

				exitDialog.addButton("Quit");
				exitDialog.addButton("Cancel");
				exitDialog.build().show();
			}
		});

		game.uiGroup.setZIndex(2);
		game.uiGroup.addActor(start);
		game.uiGroup.addActor(records);
		game.uiGroup.addActor(quit);

		//button.addListener(new TextTooltip("This is a tooltip!", skin));
	}

	protected void createEffects() {
		game.effectsGroup.addActor(new Effect(Particle.pixelParticle));
		game.effectsGroup.addActor(new Effect(Particle.pixelParticle));
		game.effectsGroup.addActor(new Effect(Particle.pixelParticle));
	}

	private void moveEffects() {

		effectsTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				for(Actor e : game.effectsGroup.getChildren()) {
					int ax = Util.getRandomRange(0, game.WIDTH);
					int ay = Util.getRandomRange(0, game.HEIGHT);

					e.addAction(Actions.sequence(Actions.moveTo(ax, ay, 1.5f), Actions.delay(0.5f)));
				}
			}
		}, 0, 2000);
	}

	@Override
	public void dispose() {
		effectsTimer.cancel();
		game.effectsGroup.clearChildren();

		Logger.log(CLASS_NAME, "disposed");
	}
}
