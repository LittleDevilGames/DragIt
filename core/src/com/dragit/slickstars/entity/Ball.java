package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.ObjectType;

public class Ball extends Actor {
	public boolean isDragged;
	private Sprite sprite;
	public boolean isAlive;
	private Direction direction;
	private Color color;
	private ObjectType type;
	
	public Ball(float x, float y, float w, float h, ObjectType type, Sprite sprite) {
		this.sprite = sprite;
		this.sprite.setBounds(x, y, w, h);
		setType(type);
		this.setBounds(x, y, w, h);
		this.isDragged = false;
		this.isAlive = true;
		this.setTouchable(Touchable.enabled);
		this.direction = Direction.NONE;
		//this.debug();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setColor(color);
		if(isDragged) {
			sprite.setAlpha(0.5f);
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
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
		
		switch(type) {
		case RED: color = Color.valueOf(MainGame.COLOR_RED);
			break;
		case GREEN: color = Color.valueOf(MainGame.COLOR_GREEN);
			break;
		}
	}
}
