package com.dragit.slickstars.game;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.dragit.slickstars.util.Logger;


public class Score {
	
	private final String CLASS_NAME = "Score";
	
	private final String RECORDS_FILE = "records.json";
	private final int MAX_SCORES = 10;
	
	private int score;
	private ArrayList<Integer> scoreList;
	
	public Score() {
		this.score = 0;
		this.scoreList = new ArrayList<Integer>();
		Logger.log(CLASS_NAME, "started");
	}
	
	public void set(int score) {
		this.score = score;
	}
	
	public int get() {
		return this.score;
	}
	
	public ArrayList<Integer> getList() {
		return scoreList;
	}
	
	public boolean loadRecords() {
		Json json = new Json();
		FileHandle file = Gdx.files.local(RECORDS_FILE);
		Logger.log(CLASS_NAME, "Loading records from file " + file.name());
		if(!file.exists()) {
			Logger.log(CLASS_NAME, "Records file " + file.name() + " not found");
			return false;
		}
		
		scoreList = json.fromJson(ArrayList.class, Integer.class, file);
		Logger.log(CLASS_NAME, "Records file " + file.name() + " successfuly loaded!");
		return true;
	}
	
	public boolean writeRecord(int value) {
		FileHandle file = Gdx.files.local(RECORDS_FILE);
		Logger.log(CLASS_NAME, "Writing new score to records file " + file.name());
		if(value < 1) return false;
		scoreList.add(value);
		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		if(scoreList.size() > MAX_SCORES) {
			for(int i = MAX_SCORES; i < scoreList.size(); i++) {
				scoreList.remove(i);
			}
		}
		
		Json json = new Json();
		try {
			file.writeString(json.toJson(scoreList, ArrayList.class, Integer.class), false);
		}
		catch(GdxRuntimeException ex) {
			Logger.log(CLASS_NAME, "GdxRuntimeException error: " + ex.getMessage());
			return false;
		}
		catch(Exception ex) {
			Logger.log(CLASS_NAME, "Unknown error: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	public void dispose() {
		scoreList.clear();
		Logger.log(CLASS_NAME, "disposed");
	}
}
