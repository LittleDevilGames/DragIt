package com.dragit.slickstars.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.ObjectType;
import com.dragit.slickstars.listener.DragingListener;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Util;

public class LevelService {
	
	private final String CLASS_NAME = "LevelService";
	
	private final int COUNT_LEVEL_BALLS = 25;
	private final int CREATING_PERIOD = 600;
	private final int COUNT_OBJ_TYPES = 2;

	public ArrayList<Ball> balls;
	private MainGame game;
	private Timer delayTimer;
	private float currPos;
	private float end;
	private float offset;
	private Direction direction;
	private int maxBalls;
	private boolean timerState;
	
	public LevelService(MainGame game) {
		this.game = game;
		this.balls = new ArrayList<Ball>();
		this.delayTimer = new Timer();
		
		this.maxBalls = COUNT_LEVEL_BALLS;
		initTimer();
		this.timerState = false;
		Logger.log(CLASS_NAME, "started");
	}
	
	public void generate(float startPos, float endPos, float offsetPos, Direction ballDirection) {
		
		Logger.log(CLASS_NAME, "generating started");
		this.end = endPos;
		this.offset = offsetPos;
		this.direction = ballDirection;
		
		this.currPos = startPos;
		timerState = true;
	}
	
	private void initTimer() {
		delayTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				if(timerState && !MainGame.isPause) {
					if(direction == Direction.LEFT) {
						if(currPos <= end) timerState = false;
						currPos -= offset;
					}
					else if(direction == Direction.RIGHT) {
						if(currPos >= end) timerState = false;
						currPos += offset;
					}
					pushBall(currPos);
				}
			}
		}, 0, CREATING_PERIOD);
	}
	
	private void pushBall(float x) {
		Sprite sprite = new Sprite(Art.get("ballTexture"));
		
		ObjectType type = getRandObjectType(COUNT_OBJ_TYPES);
		if(balls.size() >= maxBalls) {
			for(Ball b : balls) {
				if(b.isAlive == false) {
					b.setPosition(x, game.HEIGHT + game.BALL_SIZE * 2);
					b.isAlive = true;
					b.isDragged = false;
					b.setDirection(Direction.NONE);
					b.setType(type);
					break;
				}
			}
		}
		else {
			Ball ball = new Ball(x, game.HEIGHT + game.BALL_SIZE * 2, game.BALL_SIZE, game.BALL_SIZE, type, sprite);
			ball.addListener(new DragingListener()); 
			game.ballGroup.addActor(ball);
			balls.add(ball);
		}
	}
	
	private ObjectType getRandObjectType(int max) {
		int type = Util.getRandomRange(0, max);
		switch(type) {
			case 0: return ObjectType.RED;
			case 1: return ObjectType.GREEN;
		}
		return ObjectType.GREEN;
	}
	
	public void dispose() {
		balls.clear();
		delayTimer.cancel();
	}
}
