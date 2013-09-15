package robots.util;

import java.awt.geom.Point2D;

import robocode.BulletHitEvent;
import robocode.util.Utils;
import robots.util.*;

public class Wave {
	
	Point2D position = new Point2D.Double(0,0);;
	Point2D enemyPosition = new Point2D.Double(0,0);;
	double velocity = 0;
	double traveled = 0;
	double distance = 0;
	double bearing = 0;
	Bot target = null;
	boolean broken = false;
	double escapeAngleForward;
	double escapeAngleBackward;
	double enemyDirection;
	double startHeading;
	public boolean clockWise;
	
	
	public Wave() {//empty wave
		
	}
	
	public Wave(double x, double y, double velocity, Bot target){
		
		this.target = target;
		this.position = new Point2D.Double(x,y);
		this.enemyPosition = new Point2D.Double(target.x,target.y);
		this.velocity = velocity;
		this.traveled = 0;
		this.distance = target.getDistance();
		this.bearing = target.getBearing();
		this.broken = false;
		this.startHeading = target.getHeading();
		
		
		double maxForward = 0;
		double maxBackward = 0;
		
		
		this.escapeAngleForward = Math.asin(maxForward);
		this.escapeAngleBackward = Math.asin(maxBackward);
		
	}
	
	void update(){
		traveled += velocity;
		if(RobotUtils.getRange(position,target.location()) < traveled){
			double newBearing = RobotUtils.absbearing(position, target.location());
			bearing = newBearing - bearing;
			broken = true;
			
			//codigo para quando acertou
		}
		
	}
	public double hit(){
		//qual foi o angulo relativo, dividido por escapeAngle
		return 0.0;
	}
	public boolean hitMe(BulletHitEvent bhe){
		return false;
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

	public Bot getTarget() {
		return target;
	}

	public void setTarget(Bot target) {
		this.target = target;
	}

	public boolean hasBroken() {
		return broken;
	}

	public void setBroken(boolean hasBroken) {
		this.broken = hasBroken;
	}

	
}
