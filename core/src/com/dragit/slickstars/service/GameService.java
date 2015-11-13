package com.dragit.slickstars.service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.dragit.slickstars.game.Countdown;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.screen.GameScreen;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Util;

public class GameService {
	private final String CLASS_NAME = "GameService";
	
	private MainGame game;
	
	
	private final int GENERATES_COUNT = 5;
	private int generateCount;
	private LevelService levelService;
	
	private Timer ballTimer;
	private Timer countDownTimer;
	protected Countdown countdown;
	private int partOfTime;
	private int timeCreateBall;
	
	public GameService(MainGame game) {
		this.game = game;
		
		levelService = new LevelService(game);
		game.setDifficult(1);
		game.setCombo(1);
		game.dragged = 0;
		game.maxCombo = 1;
		startCountdown();
		pause(false);
		
		generateCount = GENERATES_COUNT;
		startBallTimer();

		game.ballSpeed = game.DEFAULT_BALL_SPEED;
		game.score.set(0);
		game.points = 3;
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
		timeCreateBall = Util.getRandomRange(1000, 5000);

		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!MainGame.isPause) {
					generateCount--;
					generateLevel();
				}
			}
		}, 0, timeCreateBall);
	}
	
	private void generateLevel() {
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
			case 2:
				start = Util.getRandomRange((int) game.BALL_SIZE, (int) (game.WIDTH - game.BALL_SIZE));
				end = 0f;
				direction = Direction.NONE;
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
		/*Particle.fireParticle.draw(game.batch, delta);
		
		if(Particle.fireParticle.isComplete()) {
			Particle.fireParticle.reset();
		}*/
		
		if(game.status == GameStatus.GAME_END) {
			Font.scoreFont.draw(game.batch, "GAME OVER\nYour score: " + game.score.get() + "\nDragged: " + game.dragged + "\nMax combo: x" + game.maxCombo, game.WIDTH / 3.5f, game.HEIGHT / 1.5f);
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

		/*try{
		for(Ball ball : levelService.balls) {
			ballUpdate(ball);
		}
		}
		catch(ConcurrentModificationException e) {
			Gdx.app.error("ERROR", "Error: " + e.getMessage());
		}*/
		
		if(countdown.getPartOfTime() < 1 && !countdown.isPause()) {
			game.setDifficult(game.getDifficult() + 1);
			game.ballSpeed += game.ACCELERATE_VALUE;
			countdown.setPartOfTime(partOfTime);
			Logger.log(CLASS_NAME, "difficult changed to " + game.getDifficult());
			Logger.log(CLASS_NAME, "speed changed to " + game.ballSpeed);
		}
		
		Font.mainFont.draw(game.batch, "Score " + game.score.get(), game.UI_LABEL_OFFSET, game.HEIGHT - game.UI_LABEL_OFFSET);
		Font.mainFont.draw(game.batch, "Points " + game.points, game.UI_LABEL_OFFSET , game.HEIGHT - (game.UI_LABEL_OFFSET * 2));
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
		game.setGameScreen(new GameScreen(game));
	}
	
	public void dispose() {
		levelService.dispose();
		countDownTimer.cancel();
		ballTimer.cancel();
		Logger.log(CLASS_NAME, "disposed");
	}
}
