package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Line;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.BallDirection;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.listener.DragingListener;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;

public class GameService {
	private final String CLASS_NAME = "GameService";
	
	private MainGame game;
	private ArrayList<Ball> balls;
	private ArrayList<Line> lines;
	
	private final int LINES_COUNT = 3;
	private final float LINE_HEIGHT = 48f;
	private final int TIME_CREATE_BALL = 1000;
	
	private int lineGap;
	private Timer ballTimer;
	private int maxBalls;
	
	public GameService(MainGame game) {
		this.game = game;
		this.balls = new ArrayList<Ball>();
		this.lines = new ArrayList<Line>();
		
		this.ballTimer = new Timer();
		this.lineGap = this.game.HEIGHT / 4;
		game.setDifficult(1);
		this.maxBalls = game.getDifficult() * 5;
		
		Gdx.input.setInputProcessor(game.stage);
		
		levelCreate();
		ballCreate();
		
		game.status = GameStatus.GAME_PLAY;
		Logger.log(CLASS_NAME, "started");
	}
	
	private Ball ballPush() {
		Ball ball = null;
		if(balls.size() < maxBalls) {
			ball = new Ball(getRandomX(), game.HEIGHT + game.BALL_SIZE, game.BALL_SIZE, game.BALL_SIZE, new Sprite(Art.ballTexture));
			ball.addListener(new DragingListener()); 
			game.stage.addActor(ball);
			balls.add(ball);
		}

		return ball;
	}
	
	private Ball ballPush(Ball ball) {
		ball.setPosition(getRandomX(), game.HEIGHT + game.BALL_SIZE);
		ball.isAlive = true;
		ball.setDirection(BallDirection.NONE);
		return ball;
	}
	
	private void ballCreate() {
		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ballPush();
			}
		}, 0, TIME_CREATE_BALL);
		
		Logger.log(CLASS_NAME, "balls creating..");
	}
	
	private int ballUpdate(Ball ball) {
		
		if(game.status != GameStatus.GAME_PLAY) return 0;
		
		if(ball.isAlive && ball.isDragged == false) {
			ball.setY(ball.getY() - game.BALL_SPEED);
		}
		
		if(ball.isDragged && ball.getDirection() != BallDirection.NONE) {
			if(ball.getDirection() == BallDirection.LEFT) {
				ball.setX(ball.getX() - game.DRAG_SPEED);
			}
			
			if(ball.getDirection() == BallDirection.RIGHT) {
				ball.setX(ball.getX() + game.DRAG_SPEED);
			}
		}
		
		if(isBallOut(ball)) { 
			ball.isAlive = false;
			ball.isDragged = false;
			ballPush(ball);
		}
		return 1;
	}
	
	private void levelCreate() {
		float linePos = 0f;
		Line line = null;
		for(int i = 0; i < LINES_COUNT; i++) {
			linePos = lineGap * (i + 1);
			line = new Line(0f, linePos, game.WIDTH, LINE_HEIGHT, new Sprite(Art.lineTexture));
			line.setLevel(LINES_COUNT - i);
			lines.add(line);
		}
		Logger.log(CLASS_NAME, "level crated");
	}
	
	private float getRandomX() {
		float pos = 0;
		pos = new Random().nextInt(game.WIDTH);
		if((pos + game.BALL_SIZE) >= game.WIDTH) {
			pos -= game.BALL_SIZE;
		}
		return pos;
	}
	
	private boolean isBallOut(Ball ball) {
		if(ball.getY() < (0 - game.BALL_SIZE))
			return true;
		
		if(ball.getX() > (game.WIDTH + game.BALL_SIZE) || ball.getX() < (0 - game.BALL_SIZE)) 
			return true;
		
		return false;
	}
	
	public void update(float delta) {
		for(Ball ball : balls) {
			ballUpdate(ball);
		}
		
		for(Line line : lines) {
			line.draw(game.batch);
		}
	}
	
	public void dispose() {
		balls.clear();
		lines.clear();
		Logger.log(CLASS_NAME, "disposed");
	}
}
