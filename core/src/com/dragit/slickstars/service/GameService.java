package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Border;
import com.dragit.slickstars.entity.Hint;
import com.dragit.slickstars.game.Countdown;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.game.MainGame.ObjectType;
import com.dragit.slickstars.screen.GameScreen;
import com.dragit.slickstars.util.Font;
import com.dragit.slickstars.util.Logger;

public class GameService {
	private final String CLASS_NAME = "GameService";
	
	private MainGame game;
	
	private final int TIME_CREATE_BALL = 5000;
	private final int DRAG_SCORE = 50;
	private final int COUNT_OBJ_TYPES = 2;
	private final float UI_LABEL_SIZE = 120f;
	private final float UI_LABEL_OFFSET = 30f;
	
	private LevelService level;
	private Timer ballTimer;
	private Timer countDownTimer;
	protected Countdown countdown;
	private int partOfTime;
	private ArrayList<Border> sides;
	
	public GameService(MainGame game) {
		this.game = game;
		
		level = new LevelService(game);
		game.setDifficult(1);
		startCountdown();
		this.ballTimer = new Timer();
		//this.maxBalls = game.getDifficult() * 5;
		createSides();
		
		Gdx.input.setInputProcessor(game.stage);
		
		pause(false);
		startBallTimer();
		game.score = 0;
		game.points = 3;
		game.status = GameStatus.GAME_PLAY;
		
		Logger.log(CLASS_NAME, "started");
	}
	
	private void startCountdown() {
		partOfTime = game.GAME_TIME / 4;
		countdown = new Countdown(game.GAME_TIME, partOfTime);
		countDownTimer = new Timer();
		countDownTimer.schedule(countdown, 0, 1000);
	}
	
	private void createSides() {
		this.sides = new ArrayList<Border>();
		this.sides.add(new Border(new Vector2(0, 0), 10, game.HEIGHT, Direction.LEFT, ObjectType.GREEN));
		this.sides.add(new Border(new Vector2(game.WIDTH - 10, 0), 10, game.HEIGHT, Direction.RIGHT, ObjectType.RED));
	}
	
	private int changeSides() {
		if(sides.isEmpty()) return 0;
		
		for(Border side : sides) {
			switch(side.getState()) {
				case LEFT: {
					side.setState(Direction.RIGHT);
					side.position.x = game.WIDTH - 10;
					side.position.y = 0;
					break;
				}
				case RIGHT: {
					side.setState(Direction.LEFT);
					side.position.x = 0;
					side.position.y = 0;
					break;
				}
				default:
					break;
			}
		}
		return 1;
	}

	private void startBallTimer() {
		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				startGenerateStrategy();
			}
		}, 0, TIME_CREATE_BALL);
		
		Logger.log(CLASS_NAME, "balls creating..");
	}
	
	private void startGenerateStrategy() {
		float start, end;
		Direction direction;
		
		Random rand = new Random();
		if(rand.nextInt(2) == 0) {
			start = game.WIDTH;
			end = game.BALL_SIZE + 5f;
			direction = Direction.LEFT;
		}
		else {
			start = game.BALL_SIZE + 5f;
			end = game.WIDTH;
			direction = Direction.RIGHT;
		}
		
		level.generate(start, end, game.BALL_SIZE + 5f, direction);
	}
	
	private void pointAction(float x, float y, boolean take) {
		Hint pointHint = new Hint(x, y, (take) ? "+1" : "-1", Font.mainFont);
		game.stage.addActor(pointHint);
		pointHint.startAction();
		if(take) {
			game.points++;
		}
		else {
			game.points--;
		}
	}
	
	private void scoreAction(int score, float x, float y) {
		Hint scoreHint = new Hint(x, y, "+" + score, Font.mainFont);
		game.stage.addActor(scoreHint);
		scoreHint.startAction();
		game.score += score;
	}
	
	private int ballCheckSide(Ball ball) {
		if(!ball.isDragged) return 0;
		
		for(Border side : sides) {
			if((ball.getX() > side.position.x && side.getState() == Direction.RIGHT) 
				|| (ball.getX() < side.position.x && side.getState() == Direction.LEFT)) {
				
				ball.isAlive = false;
				
				if(ball.getType() == side.getType()) {
					scoreAction(DRAG_SCORE * game.getDifficult(), ball.getX(), ball.getY());
					pointAction(game.WIDTH / 2, game.HEIGHT, true);
					return 1;
				}
				else {
					changeSides();
					pointAction(game.WIDTH / 2, game.HEIGHT, false);
					return 1;
				}
			}
		}
		return 1;
	}
	
	private int ballUpdate(Ball ball) {
		
		if(ball == null) return 0;
		if(game.status != GameStatus.GAME_PLAY) return 0;
		
		if(ball.isAlive && ball.isDragged == false) {
			ball.setY(ball.getY() - game.BALL_SPEED);
		}
		
		if(ball.isDragged && ball.getDirection() != Direction.NONE) {
			if(ball.getDirection() == Direction.LEFT) {
				ball.setX(ball.getX() - game.DRAG_SPEED);
			}
			if(ball.getDirection() == Direction.RIGHT) {
				ball.setX(ball.getX() + game.DRAG_SPEED);
			}
		}
		
		if(ball.isAlive) {
			ballCheckSide(ball);
			
			if(isBallOut(ball)) {
				//pointAction(game.WIDTH / 2, game.HEIGHT, false);
				ball.isDragged = false;
				ball.isAlive = false;
			}
		}
		return 1;
	}

	private int getRandomRange(int min, int max) {
		int pos = 0;
		pos = new Random().nextInt(max - min) + min;
		return pos;
	}
	
	private boolean isBallOut(Ball ball) {
		if(ball.getY() < (0 - game.BALL_SIZE))
			return true;
		
		return false;
	}
	
	public void render(float delta) {
		game.shapeRenderer.begin(ShapeType.Filled);
		for(Border side : sides) {
			game.shapeRenderer.rect(side.position.x, side.position.y, side.getWidth(), side.getHeight(), side.getColor(), side.getColor(), side.getColor(), side.getColor());
		}
		game.shapeRenderer.end();
	}
	
	public int update(float delta) {
		/*Particle.fireParticle.draw(game.batch, delta);
		
		if(Particle.fireParticle.isComplete()) {
			Particle.fireParticle.reset();
		}*/
		
		if(game.status == GameStatus.GAME_END) {
			Font.mainFont.draw(game.batch, "GAME OVER\nYour score: " + game.score, game.WIDTH / 3, game.HEIGHT / 2);
			if(Gdx.input.isTouched()) {
				restart();
				return 0;
			}
		}
			
		if(game.points < 1) {
			game.status = GameStatus.GAME_END;
		}
		
		for(Ball ball : level.balls) {
			ballUpdate(ball);
		}
		
		if(countdown.getPartOfTime() < 1 && !countdown.isPause()) {
			game.setDifficult(game.getDifficult() + 1);
			countdown.setPartOfTime(partOfTime);
			Logger.log(CLASS_NAME, "difficult changed to " + game.getDifficult());
		}
		
//		Font.mainFont.draw(game.batch, "Time " + countdown.getTime(), game.WIDTH / 6, game.HEIGHT - 30f);
		Font.mainFont.draw(game.batch, "Score " + game.score, UI_LABEL_OFFSET, game.HEIGHT - UI_LABEL_OFFSET);
		Font.mainFont.draw(game.batch, "Points " + game.points, UI_LABEL_SIZE + (UI_LABEL_OFFSET * 2) , game.HEIGHT - UI_LABEL_OFFSET);
		
		if(game.status == GameStatus.GAME_PAUSE) {
			Font.mainFont.draw(game.batch, "Pause", game.WIDTH / 2, game.HEIGHT / 2);
		}
		
		return 1;
	}
	
	private ObjectType getRandObjectType(int max) {
		int type = getRandomRange(0, max);
		switch(type) {
			case 0: return ObjectType.RED;
			case 1: return ObjectType.GREEN;
		}
		return ObjectType.GREEN;
	}
	
	public void pause(boolean pause) {
		MainGame.isPause = pause;
		countdown.setPause(pause);
		game.status = GameStatus.GAME_PAUSE;
		
		Logger.log(CLASS_NAME, "game pause " + pause);
	}
	
	public void restart() {
		game.stage.clear();
		game.batch.flush();
		game.setGameScreen(new GameScreen(game));
	}
	
	public void dispose() {
		//balls.clear();
		countDownTimer.cancel();
		ballTimer.cancel();
		Logger.log(CLASS_NAME, "disposed");
	}
}
