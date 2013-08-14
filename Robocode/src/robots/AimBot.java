package robots;



import robocode.*;
import robocode.util.Utils;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Kamikaze - a robot by (your name here)
 */
public class AimBot extends AdvancedRobot
{
	/**
	 * run: Kamikaze's default behavior
	 */
	public void run() {
	    // ...
	 
	    do {
	        // ...
	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        if ( getRadarTurnRemaining() == 0.0 )
	            setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
	        execute();
	    } while ( true );
	 
	    // ...
	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
	 
		/**
		 * Módulo de scan perfeito para um robô.
		 */
	    double angleToEnemy = getHeadingRadians() + e.getBearingRadians();	 
	    double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );
	    double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
	    double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );
	 
	    radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
	 
	    setTurnRadarRightRadians(radarTurn);
	    setTurnGunRightRadians(gunTurn);
	    
	}

	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		
	}	
}
								