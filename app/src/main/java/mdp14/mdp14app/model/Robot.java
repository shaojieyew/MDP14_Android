package mdp14.mdp14app.model;

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
	private float posX=-1f;
	private float posY=-1f;
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
	public void rotate(float degree){

		direction = (direction + degree)%360;
	}
	public void rotateRight(){
		rotate(90);
	}
	public void rotateLeft(){
		rotate(-90);
	}

	public boolean rotateToNorth(){
		if(direction == 0){
			return false;
		}
		float degree = (int) degreeToRotateToDirection(direction,0);
		rotate(degree);
		return true;
	}
	public boolean rotateToSouth(){
		if(direction == 180){
			return false;
		}
		float degree = (int) degreeToRotateToDirection(direction,180);
		rotate(degree);
		return true;
	}
	public boolean rotateToEast(){
		if(direction == 90){
			return false;
		}
		float degree = (int) degreeToRotateToDirection(direction,90);
		rotate(degree);
		return true;
	}
	public boolean rotateToWest(){
		if(direction == 270){
			return false;
		}
		float degree = (int) degreeToRotateToDirection(direction,270);
		rotate(degree);
		return true;
	}

	public void moveForward(int distance){
		//int distance = 10;
		double radians = Math.toRadians(direction);
		float moveX =  ((distance/10f)*(float)Math.sin(radians));
		float moveY =  ((distance/10f)*(float)Math.cos(radians));
		if(getPosX()+moveX>13){
			setPosX(13);
		}else{
			if(getPosX()+moveX<1){
				setPosX(1);
			}else{
				setPosX(getPosX()+moveX);
			}
		}

		if(getPosY()+moveY>18){
			setPosY(18);
		}else{
			if(getPosY()+moveY<1){
				setPosY(1);
			}else{
				setPosY(getPosY()+moveY);
			}
		}
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



	private float degreeToRotateToDirection(float currentDirection, float inDirection){
		float difference = inDirection-currentDirection;
		if(Math.abs(Math.round(difference))==180){
			return 180;
		}
		if(difference<180){
			if(Math.abs(difference)>180){
				return difference+360;
			}else{
				return difference;
			}
		}else{
			//return (-(currentDirection+360-difference));

			return (-(360-difference));
		}
	};
}
