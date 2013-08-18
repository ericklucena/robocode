package robots;

import java.io.IOException;
import robocode.*;
import robocode.util.Utils;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */
public class TeamBot extends TeamRobot
{
	private EnemyBot enemy = new EnemyBot();
	boolean direction = false;
		
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
	    do {

	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        if ( getRadarTurnRemaining() == 0.0 )
	            setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
	        execute();
	    } while ( true );
	 

	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
		/**
		 * Módulo de scan perfeito para um robô.
		 */
		if(isTeammate(e.getName())){
			return;
		}
		
		//Caso não haja nenhum robô como alvo...
		if(enemy.none()){
			System.out.println("A");
			enemy.update(e);
		}
		
		if(enemy.getName().equals(e.getName())){
		    double angleToEnemy = getHeadingRadians() + e.getBearingRadians();	
		    double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );
		    double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
		    double bodyTurn = (Math.PI/2) + getGunHeadingRadians();
		    bodyTurn = Utils.normalRelativeAngle(bodyTurn -getHeadingRadians());
		    double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );
		 
		    radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
		    setTurnRadarRightRadians(radarTurn);
		    setTurnGunRightRadians(gunTurn);
		    setTurnRightRadians(bodyTurn);
		    evaluateScan(e);
		    
		    double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*Math.PI);
		    double x = getX()+Math.sin(absbearing_rad)*e.getDistance();
			double y = getY()+Math.cos(absbearing_rad)*e.getDistance();
			enemy.x = x;
			enemy.y = y;
			
		    
		    enemy.update(e);
		    //fire(1);
		    
		    try {
		    	System.out.println(enemy);
				this.broadcastMessage(enemy);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
		}
	}
	
	public void evaluateScan(ScannedRobotEvent e){
		if(e.getEnergy()<enemy.getEnergy()){
			if(direction){
				setAhead(50);
			}else{
				setBack(50);
			}
			direction = !direction;
		}
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
	
}
								