package com.dragit.slickstars.screen;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
	
	public TextButton createButton(String text, Skin skin, float x, float y) {
		TextButton button = new TextButton(text, skin, "toggle");
		button.setBounds(x, y, game.BUTTON_WIDTH, game.BUTTON_HEIGHT);
		button.padTop(game.BUTTON_HEIGHT / 2);
		return button;
	}
	
	protected void createUI() {
	}
	
	protected void visibleUI(boolean isVisible) {
		for(Actor a : game.stage.getActors()) {
			if(a instanceof Button) {
				a.setVisible(false);
			}
		}
	}
}
