package com.dragit.slickstars.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dragit.slickstars.game.MainGame;

import java.util.Random;


public class Util {
	
	public static int getRandomRange(int min, int max) {
		int pos = 0;
		pos = new Random().nextInt(max - min) + min;
		return pos;
	}

	public static MainGame.Direction getDragDirection(float power) {
		if(Gdx.input.getDeltaX() > power) {
			return MainGame.Direction.RIGHT;
		}
		else if(Gdx.input.getDeltaX() < -power) {
			return MainGame.Direction.LEFT;
		}
		return MainGame.Direction.NONE;
	}

	public static MainGame.ObjectType getRandObjectType(int max) {
		int type = Util.getRandomRange(0, max);
		switch(type) {
			case 0: return MainGame.ObjectType.RED;
			case 1: return MainGame.ObjectType.GREEN;
		}
		return MainGame.ObjectType.GREEN;
	}

	public static void drawText(BitmapFont font, float size, Color color, String text, float x, float y, SpriteBatch batch, boolean autoOffset) {
		font.getData().setScale(size);
		font.setColor(color);

		float offset = 0f;

		if(autoOffset) {
			offset = getTextWidth(font, text) / 2;
		}
		font.draw(batch, text, x - offset, y);
	}

	public static void drawText(BitmapFont font, float size, Color color, String text, float x, float y, SpriteBatch batch) {
		drawText(font, size, color, text, x, y, batch, true);
	}

	public static float getTextWidth(BitmapFont font, String text) {
		GlyphLayout layout = new GlyphLayout(font, text);
		return layout.width;
	}
}
