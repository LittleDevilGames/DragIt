package com.dragit.slickstars.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Font {
public static final String TEXTURE_PATH = "data/font";
	
	public static BitmapFont mainFont;
	
	public static void load() {
		try {
			mainFont = loadFont(TEXTURE_PATH + File.separator + "px.fnt");
			
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
