package com.dragit.slickstars.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.dragit.slickstars.game.MainGame;

import java.util.ArrayList;

public abstract class BaseScreen implements Screen {
	
	protected MainGame game;
	protected ArrayList<Button> buttons;

	protected BitmapFont gameFont;

	public BaseScreen(MainGame game) {
		this.game = game;
		buttons = new ArrayList<Button>();
		getResources();
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(21/225f, 46/225f, 66/225f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.camera.update();
	}
	
	public TextButton createButton(String name, String text, Skin skin, float x, float y, float w, float h) {
		TextButton button = new TextButton(text, skin, "toggle");
		button.setBounds(x, y, w, h);
		button.padTop(h / 1.8f);
		button.setName(name);
		return button;
	}
	
	protected void createUI() {
		
	}

	protected void createEffects() {

	}

	protected void getResources() {
		gameFont = game.res.gameFont;
	}
}
