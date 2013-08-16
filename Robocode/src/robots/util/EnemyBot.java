package robots.util;

import java.awt.geom.Point2D;

import robocode.ScannedRobotEvent;

public class EnemyBot {
	
	public String name;
	public double bearing;
	public double distance;
	public double energy;
	public double heading;
	public double changehead;
	public double velocity;
	public Point2D location;
	public long scanTime;
	public boolean alive;
	
	public EnemyBot(){
		reset();
	}
	
	public void update(ScannedRobotEvent e){
		
	}
	
	public void reset(){
		this.name = "";
		this.bearing = 0;
		this.distance = 0;
		this.energy = 0;
		this.heading = 0;
		this.velocity = 0;
		this.location = new Point2D.Double();
	}
	
	public boolean none(){
		return this.name.equals("");
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
	
	
	
	/*
	In a new file called "EnemyBot.java" make a public class called EnemyBot. (Remember: the filename must be the same as the class name + a .java extension.)
	Add the following private variables to the class: bearing, distance, energy, heading, name, velocity. All of these will be of type double except for name which will be of type String.
	Add the following public accessor methods to the class: getBearing(), getDistance(), getEnergy(), getHeading(), getName(), getVelocity(). These will all return the values in the private variables.
	Implement a state-change method called update which takes a ScannedRobotEvent as a parameter. Call the ScannedRobotEvent's methods (same names as the ones in step #3) to set your private variables (step #2). The update method will return void.
	Implement another state-change method called reset which sets the name variable to the empty string ("") and all the variables of type double to 0.0. The reset method will also return void.
	Implement a (state-reporting) accessor method called none which will return true if name is "" or false otherwise. (Remember to use the equals() method of the String class.) Basically, this method will return true if the reset method was just called.
	Lastly, implement a public constructor which just calls reset. Note: the constructor must be the same name as the class. Also, constructors never specify a return value.
	*/
}
