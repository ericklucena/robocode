package robots.util;

import java.awt.geom.Point2D;

public class RobotUtils {
	
	
	/**
	 * Returns the distance between two Points
	 * @param a
	 * @param b
	 * @return The distance between a and b
	 */
	public static double getRange( Point2D a, Point2D b)
	{
		double xo = b.getX()-a.getX();
		double yo = b.getY()-a.getY();
		double h = Math.sqrt( xo*xo + yo*yo );
		return h;	
	}
	
	/**
	 * If a bearing is not within the -pi to pi range, alters it to provide the shortest angle
	 * @param ang
	 * @return The new bearing angle
	 */
	public static double normaliseBearing(double ang) {
		if (ang > Math.PI)
			ang -= 2*Math.PI;
		if (ang < -Math.PI)
			ang += 2*Math.PI;
		return ang;
	}
		
	/**
	 * If a heading is not within the 0 to 2pi range, alters it to provide the shortest angle
	 * @param ang
	 * @return The new heading angle
	 */
	public static double normaliseHeading(double ang) {
		if (ang > 2*Math.PI)
			ang -= 2*Math.PI;
		if (ang < 0)
			ang += 2*Math.PI;
		return ang;
	}
	
	/**
	 * Gets the absolute bearing between to x,y coordinates
	 * @param a
	 * @param b
	 * @return
	 */
	public static double absbearing( Point2D a, Point2D b){
		double xo = b.getX() - a.getX();
		double yo = b.getY() - a.getY();
		double h = getRange( a, b);
		if( xo > 0 && yo > 0 )
		{
			return Math.asin( xo / h );
		}
		if( xo > 0 && yo < 0 )
		{
			return Math.PI - Math.asin( xo / h );
		}
		if( xo < 0 && yo < 0 )
		{
			return Math.PI + Math.asin( -xo / h );
		}
		if( xo < 0 && yo > 0 )
		{
			return 2.0*Math.PI - Math.asin( -xo / h );
		}
		return 0;
	}
	
	/**
	 * Turns the shortest angle possible to come to a heading, then returns the direction the bot needs to move in.
	 * @param angle
	 * @return Direction which the robot must go: 1(Foward) or -1(Backward)
	 */
	/*	Copie e cole no robô em que deseja usar
	int turnTo(double angle) {
	    double ang;
    	int dir;
	    ang = normaliseBearing(getHeading() - angle);
	    if (ang > 90) {
	        ang -= 180;
	        dir = -1;
	    }
	    else if (ang < -90) {
	        ang += 180;
	        dir = -1;
	    }
	    else {
	        dir = 1;
	    }
	    setTurnLeft(ang);
	    return dir;
	}
	*/
	
	/**
	 * Move towards an x and y coordinate
	 * @param x
	 * @param y
	 */
	/* Copie e cole no robô em que deseja usar
	void goTo(double x, double y) {
	    double dist = 20; 
	    double angle = Math.toDegrees(absbearing(getX(),getY(),x,y));
	    double r = turnTo(angle);
	    setAhead(dist * r);
	}
	*/
	
	
	/**
	 * Updates the EnemyBot with the informations in ScannedRobotEvent
	 * @param enemy
	 * @param e
	 */
	/*	Copie e cole no robô em que deseja usar
	public void updateEnemy(EnemyBot enemy, ScannedRobotEvent e){
		
		double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*Math.PI);
		enemy.name = e.getName();
		enemy.energy = e.getEnergy();
		double h = RobotUtils.normaliseBearing(e.getHeadingRadians() - enemy.heading);
		h = h/(getTime() - enemy.scanTime);
		enemy.changehead = h;
		double x = getX()+Math.sin(absbearing_rad)*e.getDistance(); //works out the x coordinate of where the target is
		double y = getY()+Math.cos(absbearing_rad)*e.getDistance(); //works out the y coordinate of where the target is
		enemy.location.setLocation(x, y);
		enemy.bearing = e.getBearingRadians();
		enemy.heading = e.getHeadingRadians();
		enemy.scanTime = getTime();				//game time at which this scan was produced
		enemy.velocity = e.getVelocity();
		enemy.distance = e.getDistance();	
		enemy.alive = true;
	}
	*/
	
	
}
