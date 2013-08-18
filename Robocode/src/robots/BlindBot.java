package robots;



import java.awt.geom.Point2D;
import robocode.*;
import robocode.util.Utils;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */
public class BlindBot extends TeamRobot implements Droid
{
	private EnemyBot enemy = new EnemyBot();
	boolean direction = false;
		
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
	    do {

	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        actions();
	        execute();
	    } while ( true );
	 

	}
	 
	public void actions() {
		
		double angleToEnemy = RobotUtils.absbearing(new Point2D.Double(getX(), getY()), enemy.location());	
	    double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
	    double bodyTurn = (Math.PI/2) + getGunHeadingRadians();
	    bodyTurn = Utils.normalRelativeAngle(bodyTurn -getHeadingRadians());
	 
	    setTurnRadarRightRadians(gunTurn);
	    setTurnGunRightRadians(gunTurn);
	    setTurnRightRadians(bodyTurn);

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
	
	@Override
	public void onMessageReceived(MessageEvent event) {
		
		enemy= (EnemyBot) event.getMessage();
		
		
		
	}
	
	
}
								