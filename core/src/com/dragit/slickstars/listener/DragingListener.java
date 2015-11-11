package com.dragit.slickstars.listener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;

public class DragingListener extends DragListener {
	
	private final float DRAG_POWER = 0.1f;
	
	public DragingListener() {
		this.setTapSquareSize(128f);
	}
	
	public void drag (InputEvent event, float x, float y, int pointer) {
		if(!MainGame.isPause) {
			super.drag(event, x, y, pointer);
			Ball b = (Ball) event.getTarget();
			
			if(!b.isDragged) {
				Direction direction = getDragDirection(b, DRAG_POWER);
				if(direction != Direction.NONE) {
					b.setDirection(direction);
					b.isDragged = true;
				}
			}
		}
	}
	
	public void dragStart (InputEvent event, float x, float y, int pointer) {
		if(!MainGame.isPause) {
			super.dragStart(event, x, y, pointer);
		}
	}
	
	private Direction getDragDirection(Ball ball, float power) {
		if(getDeltaX() > power) {
			return Direction.LEFT;
		}
		else if(getDeltaX() < -power) {
			return Direction.RIGHT;
		}
		return Direction.NONE;
	}
}
