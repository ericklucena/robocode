package robots;



import java.awt.geom.Point2D;
import java.util.Enumeration;
import java.util.Hashtable;

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
	Hashtable <String, EnemyBot> enemies;
	boolean direction = false;
		
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		enemies = new Hashtable<String, EnemyBot>();
		
	    do {

	        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
	        avaliation();
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
	
	//Itera sobre o hastable e faz considerações sobre o que fazer
	public void avaliation(){
		
		Enumeration<EnemyBot> enemies = this.enemies.elements();
		
		while(enemies.hasMoreElements()){
			EnemyBot enemy = enemies.nextElement();
			if(enemy.name.equals(this.enemy.name)){
				this.enemy = enemy;
			}
			
			System.out.println(enemy);
			
		}
		System.out.println();
		
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
		
		EnemyBot enemy = (EnemyBot) event.getMessage();
		enemies.put(enemy.name, enemy);
		if(this.enemy.name.equals("")){
			this.enemy = enemy;
		}
	}
	
	
}
								