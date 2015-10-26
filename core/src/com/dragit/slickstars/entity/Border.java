package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.game.MainGame.Direction;
import com.dragit.slickstars.game.MainGame.ObjectType;

public class Border {
	private Color color;
	private float width;
	private float height;
	public Vector2 position;
	private Direction side;
	private ObjectType type;
	
	public Border(Vector2 position, float w, float h, Direction side, ObjectType type) {
		this.position = position;
		this.setWidth(w);
		this.setHeight(h);
		this.type = type;
		this.side = side;
		setColor(type);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setColor(ObjectType type) {
		switch(type) {
		case RED: color = Color.RED;
			break;
		case GREEN: color = Color.GREEN;
			break;
		}
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

	public Direction getState() {
		return side;
	}

	public void setState(Direction side) {
		this.side = side;
	}
	
	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}
}
