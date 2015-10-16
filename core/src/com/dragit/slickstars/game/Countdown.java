package com.dragit.slickstars.game;

import java.util.TimerTask;

public class Countdown extends TimerTask {
	
	private int currTime;
	private int partOfTime;
	private boolean pauseState;
	
	public Countdown(int time, int partOfTime) {
		this.currTime = time;
		this.partOfTime = partOfTime;
		this.pauseState = false;
	}
	
	@Override
	public void run() {
		if(!pauseState) {
			if(currTime > 0) {
				currTime--;
				
				if(partOfTime > 0) {
					partOfTime--;
				}
			}
		}
	}
	
	public void setPause(boolean state) {
		this.pauseState = state;
	}

	public int getTime() {
		return currTime;
	}

	public void setTime(int currTime) {
		this.currTime = currTime;
	}

	public int getPartOfTime() {
		return partOfTime;
	}

	public void setPartOfTime(int partOfTime) {
		this.partOfTime = partOfTime;
	}
}
