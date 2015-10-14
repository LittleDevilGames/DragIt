package com.dragit.slickstars.util;

import com.badlogic.gdx.Gdx;

public class Logger {
	public static String LOG_TAG = "INFO";
	
	public static void log(String tag, String message) {
		Gdx.app.log(LOG_TAG, "[" + tag + "] " + message);
	}
}
