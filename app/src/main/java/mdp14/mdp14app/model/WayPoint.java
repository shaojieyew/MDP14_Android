package mdp14.mdp14app.model;

import java.util.ArrayList;

public class WayPoint {
	public static WayPoint wp=null;
	private Position position=null;
	public static WayPoint getInstance(){
		if(wp==null){
			wp= new WayPoint();
		}
		return wp;
	}


	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
