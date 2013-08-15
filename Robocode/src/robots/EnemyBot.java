package robots;

import robocode.ScannedRobotEvent;

public class EnemyBot {
	
	private String name;
	private double bearing;
	private double distance;
	private double energy;
	private double heading;
	private double velocity;
	
	
	public EnemyBot(String name, double bearing, double distance,
			double energy, double heading, double velocity) {
		super();
		this.name = name;
		this.bearing = bearing;
		this.distance = distance;
		this.energy = energy;
		this.heading = heading;
		this.velocity = velocity;
	}
	
	public EnemyBot(){
		reset();
	}
	
	public void update(ScannedRobotEvent e){
		this.name = e.getName();
		this.bearing = e.getBearing();
		this.distance = e.getDistance();
		this.energy = e.getEnergy();
		this.heading = e.getHeading();
		this.velocity = e.getVelocity();
	}
	
	public void reset(){
		this.name = "";
		this.bearing = 0;
		this.distance = 0;
		this.energy = 0;
		this.heading = 0;
		this.velocity = 0;
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
