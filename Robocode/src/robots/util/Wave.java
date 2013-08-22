package robots.util;

import java.awt.geom.Point2D;
import robots.util.*;

public class Wave {
	
	Point2D position;
	Point2D enemyPosition;
	double velocity;
	double traveled;
	double distance;
	double bearing;
	EnemyBot target;
	boolean broken;
	
	public Wave() {//empty wave
		position = new Point2D.Double(0,0);
		enemyPosition = new Point2D.Double(0,0);
		velocity = 0;
		traveled = 0;
		distance = 0;
		bearing = 0;
		target = null;
		this.broken = false;
	}
	
	public Wave(double x, double y, double velocity, EnemyBot target){
		this.target = target;
		this.position = new Point2D.Double(x,y);
		this.enemyPosition = new Point2D.Double(target.x,target.y);
		this.velocity = velocity;
		this.traveled = 0;
		this.distance = target.getDistance();
		this.bearing = target.getBearing();
		this.broken = false;
	}
	
	void update(){
		traveled += velocity;
		if(RobotUtils.getRange(position,target.location()) < traveled){
			double newBearing = RobotUtils.absbearing(position, target.location());
			bearing = newBearing - bearing;
			broken = true;
		}
		
	}
	
	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Point2D getEnemyPosition() {
		return enemyPosition;
	}

	public void setEnemyPosition(Point2D enemyPosition) {
		this.enemyPosition = enemyPosition;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getTraveled() {
		return traveled;
	}

	public void setTraveled(double traveled) {
		this.traveled = traveled;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public EnemyBot getTarget() {
		return target;
	}

	public void setTarget(EnemyBot target) {
		this.target = target;
	}

	public boolean hasBroken() {
		return broken;
	}

	public void setBroken(boolean hasBroken) {
		this.broken = hasBroken;
	}

	
}
