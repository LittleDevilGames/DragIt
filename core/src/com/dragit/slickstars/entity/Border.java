package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.game.MainGame.Direction;

public class Border {
	private Color color;
	private float width;
	private float height;
	public Vector2 position;
	private Direction side;
	
	public Border(Vector2 position, float w, float h, Direction side, Color color) {
		this.position = position;
		this.setWidth(w);
		this.setHeight(h);
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Direction getSide() {
		return side;
	}

	public void setSide(Direction side) {
		this.side = side;
	}
}
