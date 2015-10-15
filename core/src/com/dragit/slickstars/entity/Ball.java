package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.dragit.slickstars.game.MainGame.Direction;;

public class Ball extends Actor {
	public boolean isDragged;
	private Color defaultColor;
	private Sprite sprite;
	public boolean isAlive;
	private Direction direction;
	
	public Ball(float x, float y, float w, float h, Sprite sprite) {
		this.sprite = sprite;
		this.sprite.setBounds(x, y, w, h);
		this.setBounds(x, y, w, h);
		this.defaultColor = sprite.getColor();
		this.isDragged = false;
		this.isAlive = true;
		this.setTouchable(Touchable.enabled);
		this.direction = Direction.NONE;
		//this.debug();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(isDragged) {
			sprite.setColor(Color.GREEN);
		}
		else {
			sprite.setColor(defaultColor);
		}
		sprite.draw(batch);
	}
	
	@Override
	public void positionChanged() {
		super.positionChanged();
		sprite.setPosition(getX(), getY());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
