package com.dragit.slickstars.screen;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.dragit.slickstars.game.MainGame;

public abstract class BaseScreen {
	
	protected MainGame game;
	protected ArrayList<Button> buttons;
	
	public BaseScreen(MainGame game) {
		this.game = game;
		buttons = new ArrayList<Button>();
	}
	
	public TextButton createButton(String name, String text, Skin skin, float x, float y, float w, float h) {
		TextButton button = new TextButton(text, skin, "toggle");
		button.setBounds(x, y, w, h);
		button.padTop(h / 2);
		button.setName(name);
		return button;
	}
	
	protected void createUI() {
		
	}
}
