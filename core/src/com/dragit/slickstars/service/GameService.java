package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Line;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.listener.DragingListener;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Particle;

public class GameService {
	private final String CLASS_NAME = "GameService";
	
	private MainGame game;
	private ArrayList<Ball> balls;
	private ArrayList<Line> lines;
	
	private final float LINE_HEIGHT = 48f;
	private final int TIME_CREATE_BALL = 1000;
	private final int TIME_CREATE_LINE = 4500;
	
	private Timer ballTimer;
	private Timer lineTimer;
	private int maxBalls;
	
	public GameService(MainGame game) {
		this.game = game;
		this.balls = new ArrayList<Ball>();
		this.lines = new ArrayList<Line>();
		
		this.ballTimer = new Timer();
		this.lineTimer = new Timer();
		game.setDifficult(1);
		this.maxBalls = game.getDifficult() * 5;
		
		Gdx.input.setInputProcessor(game.stage);
		
		lineAdd();
		lineTimer();
		ballTimer();
		
		game.status = GameStatus.GAME_PLAY;
		Logger.log(CLASS_NAME, "started");
	}
	
	private Ball ballPush() {
		Ball ball = null;
		if(balls.size() < maxBalls) {
			ball = new Ball(getRandomPos(0, game.WIDTH), game.HEIGHT - game.BALL_SIZE, game.BALL_SIZE, game.BALL_SIZE, new Sprite(Art.ballTexture));
			ball.addListener(new DragingListener()); 
			game.stage.addActor(ball);
			balls.add(ball);
		}

		return ball;
	}
	
	private Ball ballPush(Ball ball) {
		ball.setPosition(getRandomPos(0, game.WIDTH), game.HEIGHT - game.BALL_SIZE);
		ball.isAlive = true;
		ball.setDirection(Direction.NONE);
		return ball;
	}
	
	private void ballReset() {
		for(Ball ball : balls) {
			if(!ball.isDragged) {
				ball.isAlive = false;
				//Particle.explossionEffect.getEmitters().first().setPosition(ball.getX(), ball.getY());
				ballPush(ball);
			}
		}
	}
	
	private void ballTimer() {
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
		
		if(ball.isDragged && ball.getDirection() != Direction.NONE) {
			if(ball.getDirection() == Direction.LEFT) {
				ball.setX(ball.getX() - game.DRAG_SPEED);
			}
			
			if(ball.getDirection() == Direction.RIGHT) {
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
	
	private void lineAdd() {
		Line line = new Line(0f, 0f, game.WIDTH, LINE_HEIGHT, new Sprite(Art.lineTexture));
		line = lineUpdate(line);
		lines.add(line);
	}
	
	private void lineTimer() {
		lineTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ballReset();
				for(Line line : lines) {
					lineUpdate(line);
				}
			}
		}, 0, TIME_CREATE_LINE);
		
		Logger.log(CLASS_NAME, "line creating..");
	}
	
	private Line lineUpdate(Line line) {
		float linePos = 0f;
		int minPos = (int) game.BALL_SIZE * 2;
		int maxPos = (int) (game.HEIGHT - LINE_HEIGHT);
		
		linePos = getRandomPos(minPos, maxPos);
		
		moveLine(line, linePos);
		return line;
	}
	
	private void moveLine(Line line, float y) {
		float currPosY = line.getY();
		
		line.target.y = y;
		
		if(currPosY > y) {
			line.setDirection(Direction.DOWN);
		}
		else if(currPosY < y) {
			line.setDirection(Direction.UP);
		}
	}
	
	private float getRandomPos(int min, int max) {
		float pos = 0;
		pos = new Random().nextInt(max - min + 1);
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
		/*Particle.explossionEffect.draw(game.batch, delta);
		
		if(Particle.explossionEffect.isComplete()) {
			Particle.explossionEffect.reset();
		}*/
		
		for(Ball ball : balls) {
			ballUpdate(ball);
		}
		
		for(Line line : lines) {
			line.draw(game.batch);
			
			if(line.getDirection() == Direction.UP) {
				if(line.getY() < line.target.y) {
					line.setY(line.getY() + (game.DRAG_SPEED * 2));
				}
				else {
					line.setDirection(Direction.NONE);
				}
			}
			else if(line.getDirection() == Direction.DOWN) {
				if(line.getY() > line.target.y) {
					line.setY(line.getY() - (game.DRAG_SPEED * 2));
				}
				else {
					line.setDirection(Direction.NONE);
				}
			}
		}
	}
	
	public void dispose() {
		balls.clear();
		lines.clear();
		Logger.log(CLASS_NAME, "disposed");
	}
}
