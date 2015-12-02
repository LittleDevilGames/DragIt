package com.dragit.slickstars.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Border;
import com.dragit.slickstars.entity.Effect;
import com.dragit.slickstars.entity.Hint;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.game.MainGame.ObjectType;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Res;
import com.dragit.slickstars.util.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class LevelService implements Disposable {
	
	private final String CLASS_NAME = "LevelService";
	
	private final int COUNT_LEVEL_BALLS = 25;
	private final int CREATING_PERIOD = 600;
	private final int COUNT_OBJ_TYPES = 2;
	private final int DRAGS_FOR_COMBO = 5;
	private final int DRAG_DELAY = 200;

	public CopyOnWriteArrayList<Ball> balls;
	private ArrayList<Ball> tempBalls;
	private MainGame game;
	private Timer delayTimer;
	private float currPos;
	private float end;
	private float offset;
	private Direction direction;
	private int maxBalls;
	private boolean timerState;
	private ArrayList<Border> sides;
	private int comboCount;
	private long lastDragTime;

	private BitmapFont gameFont;
	private Texture ballTexture;
	
	public LevelService(MainGame game) {
		this.game = game;
		this.balls = new CopyOnWriteArrayList<Ball>();
		this.tempBalls = new ArrayList<Ball>();
		this.delayTimer = new Timer();

		getResources();

		this.comboCount = 0;
		createSides();
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

				if (timerState && !MainGame.isPause) {
					if (direction == Direction.LEFT) {
						if (currPos <= end) timerState = false;
						currPos -= offset;
					} else if (direction == Direction.RIGHT) {
						if (currPos >= end) timerState = false;
						currPos += offset;
					}
					pushBall(currPos);
				}
			}
		}, 0, CREATING_PERIOD);
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
	
	private int ballUpdate(Ball ball) {
		
		if(ball == null) return 0;
		if(game.status != GameStatus.GAME_PLAY) return 0;
		
		if(ball.isAlive && ball.isDragged == false) {
			ball.setY(ball.getY() - game.ballSpeed);
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
				game.setCombo(1);
				pointAction(game.WIDTH / 2, game.UI_LABEL_OFFSET * 2, false, game.BALL_OUT_POINT);
				ball.isDragged = false;
				ball.isAlive = false;

			}
		}
		return 1;
	}

	private int ballCheckSide(Ball ball) {
		if(!ball.isDragged) return 0;
		
		for(Border side : sides) {
			if((ball.getX() > side.position.x && side.getState() == Direction.RIGHT) 
				|| (ball.getX() < side.position.x && side.getState() == Direction.LEFT)) {
				
				ball.isAlive = false;
				
				if(ball.getType() == side.getType()) {
					checkCombo();
					showScore(ball.getX(), ball.getY(), side);
					game.dragged++;
					return 1;
				}
				else {
					comboCount = 0;
					game.setCombo(1);
					changeSides();
					pointAction(game.WIDTH / 2, game.UI_LABEL_OFFSET * 2, false, game.CHANGE_SIDE_POINT);
					return 1;
				}
			}
		}
		return 1;
	}
	
	private void checkCombo() {
		if(comboCount >= DRAGS_FOR_COMBO) {
			comboCount = 0;
			game.setCombo(game.getCombo() + 1);
			pointAction(game.WIDTH / 2, game.UI_LABEL_OFFSET * 2, true, game.getCombo(), "x" + game.getCombo());
		}
		else {
			pointAction(game.WIDTH / 2, game.UI_LABEL_OFFSET * 2, true, 1);
			comboCount++;
		}
	}
	
	private void showScore(float x, float y, Border side) {
		if(side.getState() == Direction.RIGHT) {
			x -= game.UI_LABEL_OFFSET * 2.2f;
		}
		else if(side.getState() == Direction.LEFT) {
			x += game.UI_LABEL_OFFSET;
		}
		
		scoreAction(game.DRAG_SCORE * game.getDifficult(), x, y);
	}
	
	private void pointAction(float x, float y, boolean take, int value, String str) {
		Hint pointHint = new Hint(x, y, str, gameFont);
		game.stage.addActor(pointHint);
		pointHint.startAction();
		if(take) {
			game.points += value;
		}
		else {
			game.points -= value;
		}
	}
	
	private void pointAction(float x, float y, boolean take, int value) {
		String pointMessage = (take) ? ("+" + value) : ("-" + value);
		Hint pointHint = new Hint(x, y, pointMessage, gameFont);
		game.stage.addActor(pointHint);
		pointHint.startAction();
		if(take) {
			game.points += value;
		}
		else {
			game.points -= value;
		}
	}
	
	private void scoreAction(int score, float x, float y) {
		Hint scoreHint = new Hint(x, y, "+" + score, gameFont);
		game.stage.addActor(scoreHint);
		scoreHint.startAction();
		game.score.set(game.score.get() + score);
	}

	private boolean isBallOut(Ball ball) {
		if(ball.getY() < (0 - game.BALL_SIZE))
			return true;
		
		return false;
	}
	
	private void pushBall(float x) {

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
			Ball ball = new Ball(x, game.HEIGHT + game.BALL_SIZE * 2, game.BALL_SIZE, game.BALL_SIZE, type, new Sprite(game.res.ballTexture));
			//ball.addListener(new DragingListener());
			ball.setEffect(game.res.getBallEffect());
			game.ballGroup.addActor(ball);
			balls.add(ball);
			//tempBalls.add(ball);
		}
	}
	
	public void render(float delta) {
		for(Border side : sides) {
			game.shapeRenderer.rect(side.position.x, side.position.y, side.getWidth(), side.getHeight(), side.getColor(), side.getColor(), side.getColor(), side.getColor());
		}
	}
	
	public void update(float delta) {
		for(Ball ball : balls) {
			checkDrag(ball);
			ballUpdate(ball);
		}
		/*if(!tempBalls.isEmpty()) {
			balls.addAll(tempBalls);
			tempBalls.clear();
		}*/
	}
	
	private ObjectType getRandObjectType(int max) {
		int type = Util.getRandomRange(0, max);
		switch(type) {
			case 0: return ObjectType.RED;
			case 1: return ObjectType.GREEN;
		}
		return ObjectType.GREEN;
	}

	private Direction getDragDirection(float power) {
		if(Gdx.input.getDeltaX() > power) {
			return Direction.RIGHT;
		}
		else if(Gdx.input.getDeltaX() < -power) {
			return Direction.LEFT;
		}
		return Direction.NONE;
	}
	
	private void checkDrag(Ball ball) {
		if(Gdx.input.isTouched() && !MainGame.isPause) {
			if(!ball.isDragged) {
				Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				game.camera.unproject(pos);
				if((System.currentTimeMillis() - lastDragTime) >= DRAG_DELAY) {
					if((pos.x > ball.getX() && pos.x < (ball.getX() + ball.getWidth()) && (pos.y > ball.getY() && pos.y < (ball.getY() + ball.getHeight())))) {
						Direction direction = getDragDirection(0.5f);
						if(direction != Direction.NONE) {
							ball.setDirection(direction);
							ball.isDragged = true;
							lastDragTime = System.currentTimeMillis();
						}
					}
				}
			}
		}
	}

	private void getResources() {
		gameFont = game.res.gameFont;
		ballTexture = game.res.ballTexture;
	}

	@Override
	public void dispose() {
		balls.clear();
		delayTimer.cancel();
	}
}
