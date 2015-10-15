package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.game.MainGame.Direction;

public class Line {
	private int level;
	private Sprite sprite;
	private Direction direction;
	public Vector2 target;
	
	public Line(float x, float y, float w, float h, Sprite sprite) {
		this.sprite = sprite;
		this.sprite.setBounds(x, y, w, h);
		this.direction = Direction.NONE;
		this.target = new Vector2();
	}
	
	public void draw(Batch batch) {
		this.sprite.draw(batch);
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public void setX(float x) {
		sprite.setX(x);
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public void setY(float y) {
		sprite.setY(y);
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
