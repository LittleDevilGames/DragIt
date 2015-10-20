package com.dragit.slickstars.util;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Art {
	public static final String TEXTURE_PATH = "data/tex";
	
	public static HashMap<String, Texture> textures;
	
	public static void load() {
		textures = new HashMap<String, Texture>();
		try {
			textures.put("ballTexture", loadTexture(TEXTURE_PATH + File.separator + "ball.png"));
			textures.put("lineTexture", loadTexture(TEXTURE_PATH + File.separator + "line.png"));
			
			Logger.log("Art", "textures loaded");
		}
		catch(GdxRuntimeException e) {
			System.err.println("Error loading texture file - " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error loading texture file - " + e);
		}
	}
	
	public static Texture get(String textureName) {
		return textures.get(textureName);
	}
	
	public static Texture loadTexture(String path) {
		 Texture texture = new Texture(Gdx.files.internal(path));
		 return texture;
	}
	
	public static void dispose() {
		for(Texture tex : textures.values()) {
			tex.dispose();
		}
	}
}
