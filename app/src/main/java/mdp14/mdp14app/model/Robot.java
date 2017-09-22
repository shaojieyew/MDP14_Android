package mdp14.mdp14app.model;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Robot {
	public static Robot robot=null;
	public static Robot getInstance(){
		if(robot==null){
			robot= new Robot();
		}
		return robot;
	}

	public Robot(){
	}


	private long exploringStartTime = 0;
	private long exploringEndTime = 0;
	private boolean isExploring =false;
	private boolean isMoving =false;
	private float posX=1f;
	private float posY=1f;
	private float direction =0;
	public static final float DIRECTION_NORTH =0;
	public static final float DIRECTION_EAST =90;
	public static final float DIRECTION_SOUTH =180;
	public static final float DIRECTION_WEST =270;

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public boolean isExploring() {
		return isExploring;
	}
	public void setExploring(boolean isExploring) {
		if(isExploring){
			exploringStartTime = System.currentTimeMillis();
		}else{
			exploringEndTime = System.currentTimeMillis();
		}
		this.isExploring = isExploring;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public float getDirection() {
		return direction;
	}
	public void setDirection(float direction) {
		this.direction = direction%360;
	}
	public long getExploringStartTime() {
		return exploringStartTime;
	}
	public void setExploringStartTime(long exploringStartTime) {
		this.exploringStartTime = exploringStartTime;
	}
	public long getExploringEndTime() {
		return exploringEndTime;
	}
	public void setExploringEndTime(long exploringEndTime) {
		this.exploringEndTime = exploringEndTime;
	}


}
