package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.dragit.slickstars.listener.DragingListener;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;

public class GameService {
	private final String CLASS_NAME = "GameService";
	
	private MainGame game;
	private ArrayList<Ball> balls;
	
	private final int TIME_CREATE_BALL = 1000;
	private final int DRAG_SCORE = 50;
	private final int COUNT_OBJ_TYPES = 2;
	
	private Timer ballTimer;
	private Timer countDownTimer;
	private int maxBalls;
	protected Countdown countdown;
	private int partOfTime;
	private ArrayList<Border> sides;
	
	public GameService(MainGame game) {
		this.game = game;
		this.balls = new ArrayList<Ball>();
		
		game.setDifficult(1);
		startCountdown();
		this.ballTimer = new Timer();
		this.maxBalls = game.getDifficult() * 5;
		createSides();
		
		Gdx.input.setInputProcessor(game.stage);
		
		pause(false);
		startBallTimer();
		game.score = 0;
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
				}
			default:
				break;
			}
		}
		return 1;
	}

	private Ball ballPush() {
		Ball ball = null;
		if(balls.size() < maxBalls) {
			Sprite sprite = new Sprite(Art.get("ballTexture"));
			ball = new Ball(getRandomRange(0, (int) (game.WIDTH - game.BALL_SIZE)), game.HEIGHT + game.BALL_SIZE * 2, game.BALL_SIZE, game.BALL_SIZE, getRandObjectType(COUNT_OBJ_TYPES), sprite);
			ball.addListener(new DragingListener()); 
			game.stage.addActor(ball);
			balls.add(ball);
		}

		return ball;
	}
	
	private Ball ballPush(Ball ball) {
		ball.setPosition(getRandomRange(0, (int) (game.WIDTH - game.BALL_SIZE)), game.HEIGHT + game.BALL_SIZE * 2);
		ball.isAlive = true;
		ball.isDragged = false;
		ball.setDirection(Direction.NONE);
		ball.setType(getRandObjectType(COUNT_OBJ_TYPES));
		return ball;
	}
	
	private void startBallTimer() {
		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ballPush();
				
				for(Ball ball : balls) {
					if(ball.isAlive == false) { 
						ballPush(ball);
						
					}
				}
			}
		}, 0, TIME_CREATE_BALL);
		
		Logger.log(CLASS_NAME, "balls creating..");
	}
	
	private void scoreAction(float score, float x, float y) {
		Hint scoreHint = new Hint(x, y, "+" + score, game.font);
		game.stage.addActor(scoreHint);
		scoreHint.startAction();
		game.score += score;
		
	}
	
	private int ballCheckSide(Ball ball) {
			if(!ball.isDragged) return 0;
			
			for(Border side : sides) {
				if(ball.getX() > side.position.x && side.getState() == Direction.RIGHT) {
					if(ball.getType() == side.getType()) {
						scoreAction(DRAG_SCORE * game.getDifficult(), ball.getX(), ball.getY());
					}
					else 
						changeSides();
					ball.isAlive = false;
				}
				else if(ball.getX() < side.position.x && side.getState() == Direction.LEFT) {
					if(ball.getType() == side.getType()) {
						scoreAction(DRAG_SCORE * game.getDifficult(), ball.getX(), ball.getY());
					}
					else 
						changeSides();
					ball.isAlive = false;
				}
			}
			return 1;
	}
	
	private int ballUpdate(Ball ball) {
		
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
		
		/*if(ball.getX() > (game.WIDTH + game.BALL_SIZE) || ball.getX() < (0 - game.BALL_SIZE)) 
			return true;*/
		
		return false;
	}
	
	public void render(float delta) {
		game.shapeRenderer.begin(ShapeType.Filled);
		for(Border side : sides) {
			game.shapeRenderer.rect(side.position.x, side.position.y, side.getWidth(), side.getHeight(), side.getColor(), side.getColor(), side.getColor(), side.getColor());
		}
		game.shapeRenderer.end();
	}
	
	public void update(float delta) {
		/*Particle.explossionEffect.draw(game.batch, delta);
		
		if(Particle.explossionEffect.isComplete()) {
			Particle.explossionEffect.reset();
		}*/
		
		for(Ball ball : balls) {
			ballUpdate(ball);
		}
		
		if(countdown.getPartOfTime() < 1 && !countdown.isPause()) {
			game.setDifficult(game.getDifficult() + 1);
			countdown.setPartOfTime(partOfTime);
			Logger.log(CLASS_NAME, "difficult changed to " + game.getDifficult());
		}

		game.font.draw(game.batch, "Score " + game.score, game.WIDTH - (game.WIDTH / 5), game.HEIGHT - 30f);
		game.font.draw(game.batch, "Time " + countdown.getTime(), game.WIDTH / 6, game.HEIGHT - 30f);
		
	}
	
	private ObjectType getRandObjectType(int max) {
		int type = getRandomRange(0, max);
		switch(type) {
			case 0: return ObjectType.RED;
			case 1: return ObjectType.GREEN;
			default: return ObjectType.GREEN;
		}
	}
	
	public void pause(boolean pause) {
		MainGame.isPause = pause;
		countdown.setPause(pause);
		Logger.log(CLASS_NAME, "game pause " + pause);
	}
	
	public void dispose() {
		balls.clear();
		Logger.log(CLASS_NAME, "disposed");
	}
}
