package com.dragit.slickstars.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.dragit.slickstars.game.MainGame;

import java.util.Random;

public class Util {

	public final static float DEFAULT_PADDING = 30f;
	public final static float MIN_PADDING = 20f;

	public enum Centering {
		POS_CENTER,
		POS_LEFT,
		POS_RIGHT,
		POS_TOP,
		POS_BOTTOM
	}

	public static Vector2 getPos(Centering centering, float vertical) {
		Vector2 pos = new Vector2();
		switch (centering) {
			case POS_CENTER: {
				pos.x = MainGame.WIDTH / 2;
				pos.y = MainGame.HEIGHT / 2;
				break;
			}
			case POS_LEFT: {
				pos.x = DEFAULT_PADDING;
				pos.y = vertical;
				break;
			}
			case POS_RIGHT: {
				pos.x = MainGame.WIDTH - DEFAULT_PADDING;
				pos.y = vertical;
				break;
			}
			case POS_TOP: {
				pos.x = MainGame.WIDTH / 2;
				pos.y = MainGame.HEIGHT - DEFAULT_PADDING;
				break;
			}
			case POS_BOTTOM: {
				pos.x = MainGame.WIDTH / 2;
				pos.y = DEFAULT_PADDING;
				break;
			}
		}

		return pos;
	}

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
}
