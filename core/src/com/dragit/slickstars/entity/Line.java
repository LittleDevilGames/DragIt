package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Line {
	private int level;
	private Sprite sprite;
	
	public Line(float x, float y, float w, float h, Sprite sprite) {
		this.sprite = sprite;
		this.sprite.setBounds(x, y, w, h);
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
}
