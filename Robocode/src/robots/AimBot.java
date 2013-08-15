package robots;



import robocode.*;
import robocode.util.Utils;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */
public class AimBot extends AdvancedRobot
{
	
	private String target = null;
	
	public void run() {
	 
	    do {

	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        if ( getRadarTurnRemaining() == 0.0 )
	            setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
	        execute();
	    } while ( true );
	 

	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
	 
		/**
		 * M�dulo de scan perfeito para um rob�.
		 */
		
		//Caso n�o haja nenhum rob� como alvo...
		if(target == null){
			target = e.getName();
			System.out.println("Target acquired!");
		}
		
		if(e.getName().equals(target)){
		    double angleToEnemy = getHeadingRadians() + e.getBearingRadians();	 
		    double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );
		    double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
		    double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );
		 
		    radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
		 
		    setTurnRadarRightRadians(radarTurn);
		    setTurnGunRightRadians(gunTurn);
		}
	}
	
	
	//Quando um rob� � destruido, verifica se � o atual alvo.
	public void onRobotDeath(RobotDeathEvent e){
		if(e.getName().equals(target)){
			System.out.println("Yippee ki-yay, motherfucker!");
			target = null;
		}
	}

	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		
	}	
}
								