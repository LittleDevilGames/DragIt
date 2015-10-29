package com.dragit.slickstars.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {
	
	public static final String SOUND_PATH = "data/audio";
	
	public static Music mainthemeMusic;
	
	public static void load() {
		try {
			mainthemeMusic = loadMusic(SOUND_PATH + File.separator + "music.mp3");
			
			Logger.log("Audio", "audio loaded");
		}
		catch(GdxRuntimeException e) {
			System.err.println("Error loading audio file - " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error loading audio file - " + e);
		}
	}
	
	public static Sound loadSound(String path) {
		 Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		 return sound;
	}
	
	public static Music loadMusic(String path) {
		 Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
		 return music;
	}
	
	public static void dispose() {
		mainthemeMusic.dispose();
	}
}
