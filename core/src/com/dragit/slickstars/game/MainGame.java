package com.dragit.slickstars.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dragit.slickstars.screen.LoadScreen;
import com.dragit.slickstars.util.Logger;
import com.dragit.slickstars.util.Res;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;

public class MainGame extends Game {
	private final String CLASS_NAME = getClass().getName();
	
	public final int WIDTH = 480;
	public final int HEIGHT = 800;
	public final String VERSION = "0.8 alpha";
	
	public final int BUTTON_WIDTH = 200;
	public final int BUTTON_HEIGHT = 65;
	public final int MIN_BUTTON_WIDTH = 100;
	public final int MIN_BUTTON_HEIGHT = 30;

	public final float DRAG_POWER = 0.5f;
	public final float BALL_SIZE = 64f;
	public final int BALL_OUT_POINT = 2;
	public final float DEFAULT_BALL_SPEED = 1.5f;
	public final int DRAG_SCORE = 50;
	public final int DRAG_SPEED = 15;
	public final float ACCELERATE_VALUE = 0.5f;
	public final int GAME_TIME = 60;
	public final int MAX_DIFFICULTS = 4;
	public final int CHANGE_SIDE_POINT = 2;
	
	public final static String COLOR_RED = "f8212e";
	public final static String COLOR_GREEN = "00a8b6";
	
	public final float UI_PADDING = 30f;
	public final float UI_LABEL_SIZE = 120f;
	public final float UI_LABEL_OFFSET = 30f;
	public final float FONT_TITLE_SIZE = 3.5f;
	public final float FONT_MID_SIZE = 1.5f;
	public final float FONT_DEFAULT_SIZE = 1f;
	
	public final String GAME_TITLE = "DragIt";
	public final String UI_SKIN_PATH = "data/skin/uiskin.json";
	public final String UI_SKINATLAS_PATH = "data/skin/uiskin.atlas";

	public OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public Stage stage;
	public Screen screen;
	public Skin skin;
	public Group effectsGroup;
	public Group uiGroup;
	public Group ballGroup;
	public GDXDialogs dialogs;
	public Res res;
	
	public static boolean isPause;
	public int points;
	public Score score;
	public int dragged;
	public int maxCombo;
	public float ballSpeed = 1f;
	private int combo;
	private int difficult;
	
	public enum ObjectType {
		RED,
		GREEN
	}
	
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
	
	public void init() {
		res = new Res();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		stage = new Stage();
		uiGroup = new Group();
		ballGroup = new Group();
		effectsGroup = new Group();
		dialogs = GDXDialogsSystem.install();
		shapeRenderer = new ShapeRenderer();
		score = new Score();
		
		score.loadRecords();

		stage.addActor(uiGroup);
		stage.addActor(ballGroup);
		stage.addActor(effectsGroup);

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(UI_SKINATLAS_PATH));
		skin = new Skin(Gdx.files.internal(UI_SKIN_PATH), atlas);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void create () {
		init();
		
		setGameScreen(new LoadScreen(this));
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
	
	public void setGameScreen(Screen screen) {
		if(this.screen != null) {
			this.screen.dispose();
		}
		this.screen = screen;
		this.setScreen(this.screen);
	}
	
	public void setCombo(int value) {
		if(maxCombo < value) {
			maxCombo = value;
		}
		this.combo = value;
	}
	
	public int getCombo() {
		return this.combo;
	}
	
	public void dispose() {
		super.dispose();
		score.dispose();
		batch.dispose();
		stage.dispose();
		shapeRenderer.dispose();
		res.dispose();

		Logger.log(CLASS_NAME, "disposed");
	}
}
