package com.dragit.slickstars.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dragit.slickstars.screen.MenuScreen;
import com.dragit.slickstars.util.Art;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Particle;

public class MainGame extends Game {
	private final String CLASS_NAME = "MainGame";
	
	public final int WIDTH = 480;
	public final int HEIGHT = 800;
	public final float BALL_SIZE = 48f;
	public final int BALL_SPEED = 4;
	public final int DRAG_SPEED = 15;
	
	private OrthographicCamera camera;
	
	private int difficult;
	
	public SpriteBatch batch;
	public Stage stage;
	public BitmapFont font;
	
	public enum GameStatus {
		GAME_NONE,
		GAME_PLAY,
		GAME_PAUSE,
		GAME_END
	}
	
	public enum Direction {
		NONE,
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	public GameStatus status = GameStatus.GAME_NONE;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		stage = new Stage();
		
		this.setScreen(new MenuScreen(this));
		Logger.log(CLASS_NAME, "started");
	}

	@Override
	public void render () {
		super.render();
	}
	
	public int getDifficult() {
		return difficult;
	}

	public void setDifficult(int difficult) {
		this.difficult = difficult;
	}
	
	public void dispose() {
		super.dispose();
		
		batch.dispose();
		font.dispose();
		stage.dispose();
		Art.dispose();
		//Particle.dispose();
		
		Logger.log(CLASS_NAME, "disposed");
	}
}
