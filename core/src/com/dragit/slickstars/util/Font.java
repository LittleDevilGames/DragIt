package com.dragit.slickstars.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Font {
	
	public static final String FONT_PATH = "data/font";
	
	public static BitmapFont mainFont;
	public static BitmapFont titleFont;
	
	public static void load() {
		try {
			mainFont = loadFont(FONT_PATH + File.separator + "px.fnt");
			titleFont = loadFont(FONT_PATH + File.separator + "px.fnt");
			
			titleFont.getData().setScale(3.5f);
			titleFont.setColor(Color.SKY);
			
			Logger.log("Font", "fonts loaded");
		}
		catch(GdxRuntimeException e) {
			System.err.println("Error loading font file - " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error loading font file - " + e);
		}
	}
	
	public static BitmapFont loadFont(String path) {
		 BitmapFont font = new BitmapFont(Gdx.files.internal(path));
		 return font;
	}
	
	public static void dispose() {
		mainFont.dispose();
	}
}
