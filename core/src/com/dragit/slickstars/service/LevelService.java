package com.dragit.slickstars.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.entity.Border;
import com.dragit.slickstars.entity.Hint;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.GameStatus;
import com.dragit.slickstars.game.MainGame.ObjectType;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.TextUtil;
import com.dragit.slickstars.util.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class LevelService implements Disposable {
	
	private final String CLASS_NAME = "LevelService";
	
	private final int COUNT_LEVEL_BALLS = 25;
	private final int CREATING_PERIOD = 800;
	private final int COUNT_OBJ_TYPES = 2;
	private final int DRAGS_FOR_COMBO = 5;
	private final int DRAG_DELAY = 200;
	private final String COMBO_LABEL = "COMBO X";

	protected CopyOnWriteArrayList<Ball> balls;
	protected ArrayList<Border> sides;

	private MainGame game;
	private Timer delayTimer;
	private boolean timerState;
	private float currPos;
	private float end;
	private float offset;
	private int maxBalls;
	private int comboCount;
	private long lastDragTime;
	private Direction direction;
	private long ballCreationTime;

	private BitmapFont gameFont;

	public LevelService(MainGame game) {
		this.game = game;
		this.balls = new CopyOnWriteArrayList<Ball>();

		if(game != null) {
			getResources();
			createSides();
		}

		this.comboCount = 0;
		setBallCreationTime(CREATING_PERIOD);
		this.maxBalls = COUNT_LEVEL_BALLS;
		this.timerState = false;

		Logger.log(CLASS_NAME, "started");
	}
	
	public void generate(float startPos, float endPos, float offsetPos, Direction ballDirection) {
		
		this.end = endPos;
		this.offset = offsetPos;
		this.direction = ballDirection;
		this.currPos = startPos;
		timerState = true;
	}

	public void setBallCreationTime(long time) {
		ballCreationTime = time;

		if(delayTimer != null) delayTimer.cancel();
		delayTimer = new Timer();
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
					pushBall(currPos, true);
				}
			}
		}, 0, ballCreationTime);
	}

	public long getBallCreationTime() {
		return ballCreationTime;
	}

	protected void createSides() {
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

	protected void pushBall(float x, boolean useEffect) {

		ObjectType type = Util.getRandObjectType(COUNT_OBJ_TYPES);
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
			if(useEffect) {
				ball.setEffect(game.res.getBallEffect());
			}
			game.ballGroup.addActor(ball);
			balls.add(ball);
		}
	}

	public void render(float delta) {
		for(Border side : sides) {
			game.shapeRenderer.rect(side.position.x, side.position.y, side.getWidth(), side.getHeight(), side.getColor(), side.getColor(), side.getColor(), side.getColor());
		}
	}
	
	public void update(float delta) {
		for(Ball ball : balls) {
			drag(ball);
			ballUpdate(ball);
		}
	}

	private void drag(Ball ball) {
		if(Gdx.input.isTouched() && !MainGame.isPause) {
			if(!ball.isDragged) {
				Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				game.camera.unproject(pos);

				boolean isDragged = checkDrag(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), pos);

				if(isDragged) {
					Direction direction = Util.getDragDirection(game.DRAG_POWER);
					if(direction != Direction.NONE) {
						ball.setDirection(direction);
						ball.isDragged = true;
						lastDragTime = System.currentTimeMillis();
					}
				}
			}
		}
	}

	protected boolean checkDrag(float x, float y, float w, float h, Vector3 pos) {
		if((System.currentTimeMillis() - lastDragTime) >= DRAG_DELAY) {
			if((pos.x > x && pos.x < (x + w) && (pos.y > y && pos.y < (y + h)))) {
				return true;
			}
		}
		return false;
	}

	private void checkCombo() {
		if(comboCount >= DRAGS_FOR_COMBO) {
			comboCount = 0;
			game.setCombo(game.getCombo() + 1);
			showCombo(game.WIDTH / 2, game.HEIGHT / 2);
		} else {
			comboCount++;
		}
		pointAction(game.WIDTH / 2, game.UI_LABEL_OFFSET * 2, true, game.getCombo(), "x" + game.getCombo());
	}

	private void showCombo(float x, float y) {
		comboAction(game.getCombo(), x, y);
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

	private void comboAction(int combo, float x, float y) {
		Hint comboHint = new Hint(x - TextUtil.getHalfWidth(gameFont, COMBO_LABEL), y, 6.5f, Color.BLUE, COMBO_LABEL + combo, gameFont);
		game.stage.addActor(comboHint);
		comboHint.startAction();
	}

	protected boolean isBallOut(Ball ball) {
		if(ball.getY() < (0 - game.BALL_SIZE))
			return true;

		return false;
	}

	protected int getResources() {
		if(game.res == null) return 0;

		gameFont = game.res.gameFont;
		return 1;
	}

	public void setMaxBalls(int max) {
		this.maxBalls = max;
	}

	public int getMaxBalls() {
		return this.maxBalls;
	}

	@Override
	public void dispose() {
		for(Ball b : balls) {
			b.dispose();
		}
		sides.clear();
		balls.clear();
		delayTimer.cancel();
	}
}
