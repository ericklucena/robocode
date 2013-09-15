package robots;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import robocode.*;
import robocode.util.Utils;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */
public class TeamBot extends TeamRobot
{
	private Bot enemy = new Bot();
	public Bot getEnemy() {
		return enemy;
	}

	public void setEnemy(Bot enemy) {
		this.enemy = enemy;
	}

	Hashtable <String, Bot> enemies;
	Hashtable <String, Bot> friends;

	boolean direction = false;

	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		enemies = new Hashtable<String, Bot>();
		friends = new Hashtable<String, Bot>();

		do {

			// Turn the radar if we have no more turn, starts it if it stops and at the start of round
			if ( getRadarTurnRemaining() == 0.0 )
				setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
			try {
				this.broadcastMessage(this.getBot());
			} catch (IOException e) {
				System.out.println("AWAY");
				e.printStackTrace();
			}
			evaluate();
			fire();
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
		Bot enemy = (Bot) enemies.get(e.getName());
		
		if((enemy == null) && (this.enemy.name.equals(""))){
			System.out.println("A");
			this.enemy = new Bot();
			this.enemy.name = e.getName();
		}
		
		if(this.enemy.name.equals(e.getName())){
			double angleToEnemy = getHeadingRadians() + e.getBearingRadians();	
			double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );
			//double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
			double bodyTurn = (Math.PI/2) + getGunHeadingRadians();
			bodyTurn = Utils.normalRelativeAngle(bodyTurn -getHeadingRadians());
			double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

			radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
			setTurnRadarRightRadians(radarTurn);
			//setTurnGunRightRadians(gunTurn);
			setTurnRightRadians(bodyTurn);
			evaluateScan(e);

			double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*Math.PI);
			double x = getX()+Math.sin(absbearing_rad)*e.getDistance();
			double y = getY()+Math.cos(absbearing_rad)*e.getDistance();
			this.enemy.x = x;
			this.enemy.y = y;


			this.enemy.update(e);
			enemies.put(this.enemy.getName(), this.enemy);
			//fire(1);

			try {
				this.broadcastMessage(this.enemy);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void onMessageReceived(MessageEvent e){
		
		Bot bot = (Bot) e.getMessage();
		
		if(bot.getName().equals("sample.Interactive")){
			System.out.println(bot);
		}
		
		if(this.isTeammate(bot.getName())){
			if(bot.alive){
				friends.put(bot.name, bot);
			}else{
				friends.remove(bot.name);
			}
			
		}else{
			if(bot.alive){
				enemies.put(bot.name, bot);
			}else{
				System.out.println(bot);
				enemies.remove(bot.name);
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
			enemies.remove(e.getName());
			enemy.reset();
		}
		
		if(!isTeammate(e.getName())){
			enemies.remove(e.getName());
		}else{
			friends.remove(e.getName());
		}
		
	}
	
	public void onDeath(){
		try {
			//this.broadcastMessage(this.getDeadBot());
			enemy.alive = false;
			this.broadcastMessage(enemy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Itera sobre o hastable e faz considerações sobre o que fazer
	public void evaluate(){
		
		Enumeration<Bot> enemies = this.enemies.elements();
		
		while(enemies.hasMoreElements()){
			Bot enemy = enemies.nextElement();
			
			System.out.println(enemy);
			
		}
		
		System.out.println("----");
		
		Enumeration<Bot> friends = this.friends.elements();
		
		while(friends.hasMoreElements()){
			Bot enemy = friends.nextElement();
			
			System.out.println(enemy);
			
		}
		
		System.out.println();
		
	}
	public void fire(){
		
		
	}
	

	public Point2D location(){
		return new Point2D.Double(getX(), getY());
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

	}
	
	public Bot getBot(){
		return new Bot(this.getName(), 0, 0, this.getEnergy(), this.getHeading(), this.getVelocity(), this.getX(), this.getY(), this.getTime(), true, false);
	}

	public Bot getDeadBot(){
		return new Bot(this.getName(), 0, 0, this.getEnergy(), this.getHeading(), this.getVelocity(), this.getX(), this.getY(), this.getTime(), false, false);
	}
	

}
