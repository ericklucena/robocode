package robots;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Hashtable;

import robocode.*;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */

public class TeamDroid extends TeamBot implements Droid {

	public boolean gambi = false;

	boolean direction = false;

	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		enemies = new Hashtable<String, Bot>();
		friends = new Hashtable<String, Bot>();

		do {

			// Turn the radar if we have no more turn, starts it if it stops and
			// at the start of round
			
			try {
				this.broadcastMessage(this.getBot());
			} catch (IOException e) {
				// System.out.println("AWAY");
				e.printStackTrace();
			}

			evaluate();
			antiGravMove();
			fire();
			execute();
		} while (true);

	}

public void onMessageReceived(MessageEvent e){
		
			
			Bot bot = (Bot) e.getMessage();
			
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
//					System.out.println(bot);
					enemies.remove(bot.name);
				}
			}
		
	}

	

	void goTo(double x, double y) {
		double dist = 20;
		double angle = Math.toDegrees(RobotUtils.absbearing(new Point2D.Double(
				getX(), getY()), new Point2D.Double(x, y)));
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
		} else if (ang < -90) {
			ang += 180;
			dir = -1;
		} else {
			dir = 1;
		}
		setTurnLeft(ang);
		return dir;
	}

	// Quando um robô é destruido, verifica se é o atual alvo.
	public void onRobotDeath(RobotDeathEvent e) {
		if (!isTeammate(e.getName())) {
			enemies.remove(e.getName());
		} else {
			friends.remove(e.getName());
		}
	}

	public void onDeath(DeathEvent de) {
		System.out.println("MORRI CARAI!!!");
		System.out.println("Recebi de inimigo " + danoInimigo);
		System.out.println("Recebi de amigo " + danoAmigo);
	}

	// Itera sobre o hastable e faz considerações sobre o que fazer
	public void evaluate() {
		
		// TODO levar em conta mais que a distancia.
		for (Bot enemy : enemies.values()) {
			
			System.out.println(enemy);
		}
		System.out.println("--");

		for (Bot friend : friends.values()) {
			System.out.println(friend);
		}

		System.out.println();
	}

	public void fire() {
		// sobrecarregado nos outros.
	}

	public Point2D location() {
		return new Point2D.Double(getX(), getY());
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

	}

	public Bot getBot() {
		return new Bot(this.getName(), 0, 0, this.getEnergy(),
				this.getHeading(), this.getVelocity(), this.getX(),
				this.getY(), this.getTime(), true, false);
	}

	public Bot getDeadBot() {
		return new Bot(this.getName(), 0, 0, this.getEnergy(),
				this.getHeading(), this.getVelocity(), this.getX(),
				this.getY(), this.getTime(), false, false);
	}
	
	float danoAmigo = 0;
	float danoInimigo = 0;
	
	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		if(isTeammate(event.getBullet().getName())){
			danoAmigo += event.getPower()> 1? event.getPower()*4 + 2*(event.getPower() - 1) : event.getPower() * 4;
		}else{
			danoInimigo += event.getPower()> 1? event.getPower()*4 + 2*(event.getPower() - 1) : event.getPower() * 4;
		}
		// TODO Auto-generated method stub
		super.onHitByBullet(event);
	}

}
