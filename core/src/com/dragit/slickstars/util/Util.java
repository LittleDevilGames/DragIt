package com.dragit.slickstars.util;

import java.util.Random;

public class Util {
	
	public static int getRandomRange(int min, int max) {
		int pos = 0;
		pos = new Random().nextInt(max - min) + min;
		return pos;
	}
}
