package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Border;
import com.dragit.slickstars.entity.Line;
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
	private ArrayList<Line> lines;
	
	private final float LINE_HEIGHT = 48f;
	private final int TIME_CREATE_BALL = 1000;
	private final int TIME_CREATE_LINE = 4500;
	private final int DRAG_SCORE = 50;
	
	private Timer ballTimer;
	private Timer lineTimer;
	private Timer countDownTimer;
	private int maxBalls;
	protected Countdown countdown;
	private int partOfTime;
	private ArrayList<Border> sides;
	
	public GameService(MainGame game) {
		this.game = game;
		this.balls = new ArrayList<Ball>();
		this.lines = new ArrayList<Line>();
		
		game.setDifficult(1);
		startCountdown();
		this.ballTimer = new Timer();
		this.lineTimer = new Timer();
		this.maxBalls = game.getDifficult() * 5;
		
		this.sides = new ArrayList<Border>();
		this.sides.add(new Border(new Vector2(0, 0), 10, game.HEIGHT, Direction.LEFT, ObjectType.GREEN));
		this.sides.add(new Border(new Vector2(game.WIDTH - 10, 0), 10, game.HEIGHT, Direction.RIGHT, ObjectType.RED));
		
		Gdx.input.setInputProcessor(game.stage);
		
		//lineAdd();
		//lineTimer();
		ballTimer();
		
		pause(false);
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

	private Ball ballPush() {
		Ball ball = null;
		if(balls.size() < maxBalls) {
			Sprite sprite = new Sprite(Art.get("ballTexture"));
			ball = new Ball(getRandomPos(0, (int) (game.WIDTH - game.BALL_SIZE)), game.HEIGHT + game.BALL_SIZE * 2, game.BALL_SIZE, game.BALL_SIZE, ObjectType.GREEN, sprite);
			ball.addListener(new DragingListener()); 
			game.stage.addActor(ball);
			balls.add(ball);
		}

		return ball;
	}
	
	private Ball ballPush(Ball ball) {
		ball.setPosition(getRandomPos(0, (int) (game.WIDTH - game.BALL_SIZE)), game.HEIGHT + game.BALL_SIZE * 2);
		ball.isAlive = true;
		ball.isDragged = false;
		ball.setDirection(Direction.NONE);
		return ball;
	}
	
	private int ballReset() {
		if(lines.isEmpty()) return 0;
		
		try {
			Line line = lines.get(lines.size()-1);
			for(Ball ball : balls) {
				if(!ball.isDragged) {
					if((line.getY() + LINE_HEIGHT) > ball.getY()) {
						ball.isAlive = false;
						//Particle.explossionEffect.getEmitters().first().setPosition(ball.getX(), ball.getY());
						ball.setY((0 - game.BALL_SIZE) * 2);
					}
				}
			}
		}
		catch(NullPointerException e) {
			Gdx.app.error("ERROR", "Error null: " + e);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			Gdx.app.error("ERROR", "Error array: " + e);
		}
		catch(Exception e) {
			Gdx.app.error("ERROR", "Error: " + e);
		}
		return 1;
	}
	
	private void ballTimer() {
		ballTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ballPush();
				
				for(Ball ball : balls) {
					if(ball.isAlive == false) { 
						ballPush(ball);
						break;
					}
				}
			}
		}, 0, TIME_CREATE_BALL);
		
		Logger.log(CLASS_NAME, "balls creating..");
	}
	
	private int ballCheckSide(Ball ball) {
			if(!ball.isDragged) return 0;
			
			for(Border side : sides) {
				if(ball.getX() > side.position.x && side.getSide() == Direction.RIGHT) {
					if(ball.getType() == side.getType()) {
						game.score += DRAG_SCORE * game.getDifficult();
					}
					ball.isAlive = false;
				}
				else if(ball.getX() < side.position.x && side.getSide() == Direction.LEFT) {
					if(ball.getType() == side.getType()) {
						game.score += DRAG_SCORE * game.getDifficult();
					}
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
		
		if(ball.isDragged && ball.isAlive) {
			
			if(isBallOut(ball)) {
				ball.isDragged = false;
				ball.isAlive = false;
			}
			
			ballCheckSide(ball);
		}
		return 1;
	}
	
	private void lineAdd() {
		Line line = new Line(0f, 0f, game.WIDTH, LINE_HEIGHT, new Sprite(Art.get("lineTexture")));
		line = lineUpdate(line);
		lines.add(line);
	}
	
	private void lineTimer() {
		lineTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				for(Line line : lines) {
					lineUpdate(line);
				}
				ballReset();
			}
		}, 0, TIME_CREATE_LINE);
		
		Logger.log(CLASS_NAME, "line creating..");
	}
	
	private Line lineUpdate(Line line) {
		float linePos = 0f;
		int minPos = (int) (game.HEIGHT / 4);
		int maxPos = (int) ((game.HEIGHT - (game.HEIGHT / 4)) - LINE_HEIGHT);
		
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
		pos = new Random().nextInt(max - min) + min;
		return pos;
	}
	
	private boolean isBallOut(Ball ball) {
		if(ball.getY() < (0 - game.BALL_SIZE))
			return true;
		
		if(ball.getX() > (game.WIDTH + game.BALL_SIZE) || ball.getX() < (0 - game.BALL_SIZE)) 
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
	
	public void update(float delta) {
		/*Particle.explossionEffect.draw(game.batch, delta);
		
		if(Particle.explossionEffect.isComplete()) {
			Particle.explossionEffect.reset();
		}*/
		
		
		
		for(Ball ball : balls) {
			ballUpdate(ball);
		}
		
		/*for(Line line : lines) {
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
		}*/
			
		if(countdown.getPartOfTime() < 1 && !countdown.isPause()) {
			game.setDifficult(game.getDifficult() + 1);
			countdown.setPartOfTime(partOfTime);
			Logger.log(CLASS_NAME, "difficult changed to " + game.getDifficult());
		}
		
		game.font.draw(game.batch, "Score " + game.score, game.WIDTH - (game.WIDTH / 4), game.HEIGHT - 30f);
		game.font.draw(game.batch, "Time " + countdown.getTime(), game.WIDTH / 4, game.HEIGHT - 30f);
		
	}
	
	public void pause(boolean pause) {
		MainGame.isPause = pause;
		countdown.setPause(pause);
		Logger.log(CLASS_NAME, "game pause " + pause);
	}
	
	public void dispose() {
		balls.clear();
		lines.clear();
		Logger.log(CLASS_NAME, "disposed");
	}
}
