package com.dragit.slickstars.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.dragit.slickstars.game.Countdown;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.screen.GameScreen;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.TextUtil;
import com.dragit.slickstars.util.Util;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameService implements Disposable {

	private final String CLASS_NAME = "GameService";
	
	private MainGame game;

	private final int GENERATES_COUNT = 5;
	private final int DIFFICULT_BALLS = 3;
	private final int START_POINTS = 3;
	private final int MIN_TIME_CREATE_BALL = 1000;
	private final int MAX_TIME_CREATE_BALL = 5000;
	private final int DECREASE_CREATION_PERIOD = 35;

	protected Countdown countdown;

	private LevelService levelService;
	private Timer ballTimer;
	private Timer countDownTimer;
	private int partOfTime;
	private int generateCount;

	private BitmapFont gameFont;

	public GameService(MainGame game) {
		this.game = game;

		levelService = new LevelService(game);

		game.setDifficult(1);
		game.setCombo(1);
		game.dragged = 0;
		game.maxCombo = 1;
		startCountdown();
		pause(false);

		getResources();
		
		generateCount = GENERATES_COUNT;
		startBallTimer();

		game.ballSpeed = game.DEFAULT_BALL_SPEED;

		if(game.score != null) {
			game.score.set(0);
		}

		game.points = START_POINTS;
		game.status = GameStatus.GAME_PLAY;
		
		Logger.log(CLASS_NAME, "started");
	}
	
	private void startCountdown() {
		Logger.log(CLASS_NAME, "starting countdown");
		partOfTime = game.GAME_TIME / 4;
		countdown = new Countdown(game.GAME_TIME, partOfTime);
		countDownTimer = new Timer();
		countDownTimer.schedule(countdown, 0, 1000);
	}

	private void startBallTimer() {
		ballTimer = new Timer();
		int timeCreateBall = Util.getRandomRange(MIN_TIME_CREATE_BALL, MAX_TIME_CREATE_BALL);

		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!MainGame.isPause) {
					generateCount--;
					generateLevel();
				}
			}
		}, 0, timeCreateBall);
	}
	
	protected void generateLevel() {
		float start, end;
		Direction direction;
		
		Random rand = new Random();
		int method = rand.nextInt(3);
		switch(method) {
			case 0:
				start = game.WIDTH - game.BALL_SIZE;
				end = game.BALL_SIZE;
				direction = Direction.LEFT;
				break;
			case 1:
				start = game.BALL_SIZE;
				end = game.WIDTH - game.BALL_SIZE;
				direction = Direction.RIGHT;
				break;
			default:
				start = Util.getRandomRange((int) game.BALL_SIZE, (int) (game.WIDTH - game.BALL_SIZE));
				end = 0f;
				direction = Direction.NONE;
				break;
		}
		
		levelService.generate(start, end, game.BALL_SIZE + 5f, direction);
	}
	
	public void render(float delta) {
		levelService.render(delta);
	}
	
	public int update(float delta) {

		if(game.status == GameStatus.GAME_END) {
			TextUtil.drawText(gameFont, game.FONT_MID_SIZE, Color.SKY,
					"GAME OVER\nYour score: " + game.score.get()
							+ "\nDragged: " + game.dragged
							+ "\nMax combo: x" + game.maxCombo, game.WIDTH / 3.5f, game.HEIGHT / 1.5f, game.batch);

			if(Gdx.input.isTouched()) {
				game.score.writeRecord(game.score.get());
				restart();
				return 0;
			}
		}
			
		if(game.points <= 0) {
			game.status = GameStatus.GAME_END;
		}

		levelService.update(delta);

		if(generateCount < 1) {
			ballTimer.cancel();
			startBallTimer();
			generateCount = GENERATES_COUNT;
		}
		
		if(countdown.getPartOfTime() < 1 && !countdown.isPause()) {

			long period = levelService.getBallCreationTime();

			game.setDifficult(game.getDifficult() + 1);
			game.ballSpeed += game.ACCELERATE_VALUE;
			period -= DECREASE_CREATION_PERIOD;
			levelService.setBallCreationTime(period);
			levelService.setMaxBalls(levelService.getMaxBalls() + DIFFICULT_BALLS);

			countdown.setPartOfTime(partOfTime);

			Logger.log(CLASS_NAME, "difficult changed to " + game.getDifficult());
			Logger.log(CLASS_NAME, "speed changed to " + game.ballSpeed);
			Logger.log(CLASS_NAME, "ball creation time changed to " + levelService.getBallCreationTime());
		}

		if(game.points < 0) {
			game.points = 0;
		}
		TextUtil.drawText(gameFont, game.FONT_DEFAULT_SIZE, Color.WHITE, "Score " + game.score.get(), Util.getPos(Util.Centering.POS_LEFT, game.HEIGHT - Util.MIN_PADDING), game.batch);
		TextUtil.drawText(gameFont, game.FONT_DEFAULT_SIZE, Color.WHITE, "Lives " + game.points, Util.getPos(Util.Centering.POS_LEFT, game.HEIGHT - Util.MIN_PADDING * 2), game.batch);
		TextUtil.drawText(gameFont, game.FONT_DEFAULT_SIZE, Color.WHITE, "Combo x" + game.getCombo(), Util.getPos(Util.Centering.POS_LEFT, game.HEIGHT - Util.MIN_PADDING * 3), game.batch);
		return 1;
	}
	
	public void pause(boolean pause) {
		MainGame.isPause = pause;
		countdown.setPause(pause);
		
		if(pause) {
			game.status = GameStatus.GAME_PAUSE;
		}
		else {
			game.status = GameStatus.GAME_PLAY;
		}
		Logger.log(CLASS_NAME, "game pause " + pause);
	}
	
	public void restart() {
		game.ballGroup.clearChildren();
		dispose();
		game.setGameScreen(new GameScreen(game));
	}

	private int getResources() {
		if(game.res == null) return 0;

		gameFont = game.res.gameFont;
		return  1;
	}

	@Override
	public void dispose() {
		levelService.dispose();
		countDownTimer.cancel();
		ballTimer.cancel();

		Logger.log(CLASS_NAME, "disposed");
	}
}
