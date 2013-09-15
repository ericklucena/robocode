package robots.util;

import java.awt.geom.Point2D;
import java.io.Serializable;

import robocode.ScannedRobotEvent;

public class Bot implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public double bearing;
	public double distance;
	public double energy;
	public double heading;
	public double changehead;
	public double velocity;
	public double x;
	public double y;
	public long scanTime;
	public boolean alive;
	public boolean accelerating;

	public Bot() {
		reset();
	}

	public void update(ScannedRobotEvent e, double x, double y) {
		this.accelerating = alive && e.getVelocity() > velocity;
		// se está freiando ou parando de dar r�. (nao deve ser levado MUITO em
		// conta, pode dar merda.
		this.name = e.getName();
		this.bearing = e.getBearingRadians();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeadingRadians();
		this.velocity = e.getVelocity();
		this.scanTime = e.getTime();
		this.alive = true;
		this.x = x;
		this.y = y;
	}
	
	

	public Bot(String name, double bearing, double distance, double energy,
			double heading, double changehead, double velocity, double x,
			double y, long scanTime, boolean alive, boolean accelerating) {
		super();
		this.name = name;
		this.bearing = bearing;
		this.distance = distance;
		this.energy = energy;
		this.heading = heading;
		this.changehead = changehead;
		this.velocity = velocity;
		this.x = x;
		this.y = y;
		this.scanTime = scanTime;
		this.alive = alive;
		this.accelerating = accelerating;
	}

	public void update(ScannedRobotEvent e) {
		this.name = e.getName();
		this.bearing = e.getBearingRadians();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeadingRadians();
		this.velocity = e.getVelocity();
		this.scanTime = e.getTime();
		this.alive = true;
	}

	public void reset() {
		this.name = "";
		this.bearing = 0;
		this.distance = 0;
		this.energy = 0;
		this.heading = 0;
		this.velocity = 0;
		this.x = 0;
		this.y = 0;
	}

	public boolean none() {
		if (this.name == null) {
			return false;
		} else {
			return this.name.equals("");
		}
	}

	public String getName() {
		return name;
	}

	public double getBearing() {
		return bearing;
	}

	public double getDistance() {
		return distance;
	}

	public double getEnergy() {
		return energy;
	}

	public double getHeading() {
		return heading;
	}

	public double getVelocity() {
		return velocity;
	}

	public Point2D location() {
		return new Point2D.Double(x, y);
	}

	@Override
	public String toString() {
		return  name+" [x=" + x + ", y=" + y + ", bearing=" + bearing
				+ ", distance=" + distance + ", energy=" + energy
				+ ", heading=" + heading + ", changehead=" + changehead
				+ ", velocity=" + velocity + ", scanTime=" + scanTime + ", alive=" + alive + "]";
	}

}
