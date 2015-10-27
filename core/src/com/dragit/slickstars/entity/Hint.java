package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Hint extends Actor {
	private BitmapFont font;
	private String text;
	
	public Hint(float x, float y, String text, BitmapFont font) {
		this.setX(x);
		this.setY(y);
		this.font = font;
		this.text = text;
	}
	
	public void startAction() {
		this.addAction(Actions.sequence(Actions.moveTo(getX(), getY()+40, 2f), Actions.delay(0.7f), Actions.hide(), Actions.removeActor(this)));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, text, getX(), getY());
	}
	
	@Override
	public void positionChanged() {
		super.positionChanged();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
