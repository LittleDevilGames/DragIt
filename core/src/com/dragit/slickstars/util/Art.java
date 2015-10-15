package com.dragit.slickstars.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Art {
	public static final String TEXTURE_PATH = "data/tex";
	
	public static Texture ballTexture;
	public static Texture lineTexture;
	
	public static void load() {
		try {
			ballTexture = loadTexture(TEXTURE_PATH + File.separator + "ball.png");
			lineTexture = loadTexture(TEXTURE_PATH + File.separator + "line.png");
			
			Logger.log("Art", "textures loaded");
		}
		catch(GdxRuntimeException e) {
			System.err.println("Error loading texture file - " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error loading texture file - " + e);
		}
	}
	
	public static Texture loadTexture(String path) {
		 Texture texture = new Texture(Gdx.files.internal(path));
		 return texture;
	}
	
	public static void dispose() {
		ballTexture.dispose();
		lineTexture.dispose();
	}
}
