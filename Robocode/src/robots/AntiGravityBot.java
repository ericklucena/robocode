package robots;

import java.awt.geom.Point2D;

import robocode.*;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */
public class AntiGravityBot extends AdvancedRobot
{
	Bot[] enemies;				//all enemies are stored in the hashtable
	Bot enemy;
	
	int count;
	double midpointstrength = 0;	//The strength of the gravity point in the middle of the field
	int midpointcount = 0;			//Number of turns since that strength was changed.
	
	private 
	
	static Point2D.Double[] wallPoints = new Point2D.Double[5];
		
		
	public void run() {
		
		enemies = new Bot[5];
		
		//Independência das partes do robô
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
		wallPoints[0] = new Point2D.Double(0,0);
		wallPoints[1] = new Point2D.Double(0,1000);
		wallPoints[2] = new Point2D.Double(1000,0);
		wallPoints[3] = new Point2D.Double(1000,1000);
		
	    do {

	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        if ( getRadarTurnRemaining() == 0.0 )
	            setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
	        antiGravMove();
	        execute();
	    } while ( true );
	 

	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
		int i;
		
		for (i = 0; enemies[i] != null; i++) {
			if(enemies[i].name.equals(e.getName())){
				updateEnemy(enemies[i], e);
				return;
			}
		}
		
		enemies[i] = new Bot();
		updateEnemy(enemies[i], e);		
	}
	
	void antiGravMove() {
   		double xforce = 0;
	    double yforce = 0;
	    double force;
	    double ang;
	    GravPoint p;
	    	    
		for (int i=0;i<5;i++){
			if(enemies[i]!= null){
				Bot e = enemies[i];
				if (e.alive) {
					p = new GravPoint(e.location() , -1000);
			        force = p.power/Math.pow(RobotUtils.getRange(new Point2D.Double(getX(), getY()) ,p.location),2);
			        //Find the bearing from the point to us
			        ang = RobotUtils.normaliseBearing(Math.PI/2 - Math.atan2(getY() - p.location.getY(), getX() - p.location.getX())); 
			        //Add the components of this force to the total force in their respective directions
			        xforce += Math.sin(ang) * force;
			        yforce += Math.cos(ang) * force;
				}
			}
	    }
	    

	   
	    /**The following four lines add wall avoidance.  They will only affect us if the bot is close 
	    to the walls due to the force from the walls decreasing at a power 3.**/
	    Point2D here = new Point2D.Double(getX(), getY());
	    xforce += 5000/Math.pow(RobotUtils.getRange(here, new Point2D.Double(getBattleFieldWidth(), getY())), 3);
	    xforce -= 5000/Math.pow(RobotUtils.getRange(here, new Point2D.Double(0, getY())), 3);
	    yforce += 5000/Math.pow(RobotUtils.getRange(here, new Point2D.Double(getX(), getBattleFieldHeight())), 3);
	    yforce -= 5000/Math.pow(RobotUtils.getRange(here, new Point2D.Double(getX(), 0)), 3);
	    
	    //Move in the direction of our resolved force.
	    goTo(getX()-xforce,getY()-yforce);
	}
	
	void goTo(double x, double y) {
	    double dist = 20; 
	    double angle = Math.toDegrees(RobotUtils.absbearing( new Point2D.Double(getX(),getY()),new Point2D.Double(x,y)));
	    double r = turnTo(angle);
	    setAhead(dist * r);
	}
	
	int turnTo(double angle) {
	    double ang;
    	int dir;
	    ang = RobotUtils.normaliseBearing(getHeading() - angle);
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
	
	
	//Quando um robô é destruido, verifica se é o atual alvo.
	public void onRobotDeath(RobotDeathEvent e){
		if(e.getName().equals(enemy.getName())){
			System.out.println("Yippee ki-yay, motherfucker!");
			enemy.reset();
		}
	}
		 
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		
	}
	
	public void updateEnemy(Bot enemy, ScannedRobotEvent e){
		double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*Math.PI);
		enemy.name = e.getName();
		enemy.energy = e.getEnergy();
//		double h = RobotUtils.normaliseBearing(e.getHeadingRadians() - enemy.heading);
//		h = h/(getTime() - enemy.scanTime);
//		enemy.changehead = h;
		double x = getX()+Math.sin(absbearing_rad)*e.getDistance();
		double y = getY()+Math.cos(absbearing_rad)*e.getDistance();
		enemy.location().setLocation(x, y);
		enemy.bearing = e.getBearingRadians();
		enemy.heading = e.getHeadingRadians();
		enemy.scanTime = getTime();				
		enemy.velocity = e.getVelocity();
		enemy.distance = e.getDistance();	
		enemy.alive = true;
	}
	
}
								