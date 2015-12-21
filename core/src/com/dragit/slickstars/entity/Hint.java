package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;

public class Hint extends Actor implements Disposable {
	private BitmapFont font;
	private String text;
	
	public Hint(float x, float y, String text, BitmapFont font) {
		this.setX(x);
		this.setY(y);
		this.font = font;
		this.text = text;

		this.font.getData().setScale(1.5f);
		this.font.setColor(Color.WHITE);
	}

	public Hint(float x, float y, float size, Color color, String text, BitmapFont font) {
		this.setX(x);
		this.setY(y);
		this.font = font;
		this.text = text;

		this.font.getData().setScale(size);
		this.font.setColor(color);
	}
	
	public void startAction() {
		this.addAction(Actions.sequence(Actions.scaleBy(10f, 2f, 0.5f), Actions.moveTo(getX(), getY()+40, 2f), Actions.delay(0.7f), Actions.hide(), Actions.removeActor(this)));
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


	@Override
	public void dispose() {

	}
}
