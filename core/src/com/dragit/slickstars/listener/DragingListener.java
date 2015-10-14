package com.dragit.slickstars.listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.dragit.slickstars.entity.Ball;

public class DragingListener extends DragListener {
		
	
	public DragingListener() {
		this.setTapSquareSize(48f);
	}
	
	public void drag (InputEvent event, float x, float y, int pointer) {
		super.drag(event, x, y, pointer);
		Ball b = (Ball) event.getTarget();
		b.isDragged = true;
		
		Gdx.app.log("DEBUG", "Pressed: " + pointer);
	}
	
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		super.touchUp(event, x, y, pointer, button);
		Ball b = (Ball) event.getTarget();
		b.isDragged = false;
	}
}
