package com.dragit.slickstars.listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.game.MainGame.Direction;

public class DragingListener extends DragListener {
	
	private final int DRAG_POWER = 5;
	
	public DragingListener() {
		this.setTapSquareSize(128f);
	}
	
	public void drag (InputEvent event, float x, float y, int pointer) {
		super.drag(event, x, y, pointer);
		Ball b = (Ball) event.getTarget();
		
		if(!b.isDragged) {
			Direction direction;
			direction = getDragDirection(b, DRAG_POWER);
			if(direction != Direction.NONE) {
				b.setDirection(direction);
				b.isDragged = true;
			}
		}
		Gdx.app.log("DEBUG", "Pressed: " + pointer);
	}
	
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		super.touchUp(event, x, y, pointer, button);
	}
	
	private Direction getDragDirection(Ball ball, int power) {
		if(getDeltaX() > power) {
			return Direction.LEFT;
		}
		else if(getDeltaX() < -power) {
			return Direction.RIGHT;
		}
		return Direction.NONE;
	}
}
