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
public class TeamBot extends TeamRobot {
	private Bot target = null;

	public Bot getTarget() {
		return target;
	}

	public boolean gambi = false;

	public void setTarget(Bot enemy) {
		this.target = enemy;
	}

	Hashtable<String, Bot> enemies;
	Hashtable<String, Bot> friends;

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
			if (getRadarTurnRemaining() == 0.0)
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
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

	public void onScannedRobot(ScannedRobotEvent e) {
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		double absbearing_rad = (getHeadingRadians() + e.getBearingRadians())
				% (2 * Math.PI);
		double x = getX() + Math.sin(absbearing_rad) * e.getDistance();
		double y = getY() + Math.cos(absbearing_rad) * e.getDistance();
		Bot scanned = new Bot();
		scanned.update(e, x, y);
		if(isTeammate(scanned.getName() )){
			friends.put(scanned.getName(), scanned);
		}else{
			enemies.put(scanned.getName(), scanned);
		}
		try {
			this.broadcastMessage(scanned);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void onMessageReceived(MessageEvent e) {
		Bot bot = (Bot) e.getMessage();
		if (this.isTeammate(bot.getName())) {
			Bot old = friends.get(bot.getName());
			if(old != null && old.getScanTime() > bot.scanTime){
				//DO NOTHING HAHAHAHA ZUEI
			}else{
				friends.put(bot.name, bot);
			}
			
		} else {
			Bot old = enemies.get(bot.getName());
			if(old != null && old.getScanTime() > bot.scanTime){
				//DO NOTHING HAHAHAHA ZUEI
			}else{
				enemies.put(bot.name, bot);
			}
		}

	}

	void antiGravMove() {
		double xforce = 0;
		double yforce = 0;
		double force;
		double ang;
		GravPoint p;

		for (Bot b : enemies.values()) {
			p = new GravPoint(b.location(), -500);
			force = p.power
					/ Math.pow(RobotUtils.getRange(new Point2D.Double(getX(),
							getY()), p.location), 2);
			// Find the bearing from the point to us
			ang = RobotUtils.normaliseBearing(Math.PI
					/ 2
					- Math.atan2(getY() - p.location.getY(), getX()
							- p.location.getX()));
			// Add the components of this force to the total force in their
			// respective directions
			xforce += Math.sin(ang) * force;
			yforce += Math.cos(ang) * force;
		}
		
		for (Bot b : friends.values()) {
			p = new GravPoint(b.location(), -300);
			force = p.power
					/ Math.pow(RobotUtils.getRange(new Point2D.Double(getX(),
							getY()), p.location), 2);
			// Find the bearing from the point to us
			ang = RobotUtils.normaliseBearing(Math.PI
					/ 2
					- Math.atan2(getY() - p.location.getY(), getX()
							- p.location.getX()));
			// Add the components of this force to the total force in their
			// respective directions
			xforce += Math.sin(ang) * force;
			yforce += Math.cos(ang) * force;
		}
		
		//TODO marcando para olhar depois e tweakar.
//		if(target != null){
//			p = new GravPoint(target.location(), +1000);
//			force = p.power
//					/ Math.pow(RobotUtils.getRange(new Point2D.Double(getX(),
//							getY()), p.location), 1.5);
//			// Find the bearing from the point to us
//			ang = RobotUtils.normaliseBearing(Math.PI
//					/ 2
//					- Math.atan2(getY() - p.location.getY(), getX()
//							- p.location.getX()));
//			// Add the components of this force to the total force in their
//			// respective directions
//			xforce += Math.sin(ang) * force;
//			yforce += Math.cos(ang) * force;
//		}
		
		/**
		 * The following four lines add wall avoidance. They will only affect us
		 * if the bot is close to the walls due to the force from the walls
		 * decreasing at a power 3.
		 **/
		Point2D here = new Point2D.Double(getX(), getY());
		xforce += 5000 / Math.pow(RobotUtils.getRange(here, new Point2D.Double(
				getBattleFieldWidth(), getY())), 3);
		xforce -= 5000 / Math.pow(
				RobotUtils.getRange(here, new Point2D.Double(0, getY())), 3);
		yforce += 5000 / Math.pow(RobotUtils.getRange(here, new Point2D.Double(
				getX(), getBattleFieldHeight())), 3);
		yforce -= 5000 / Math.pow(
				RobotUtils.getRange(here, new Point2D.Double(getX(), 0)), 3);

		// Move in the direction of our resolved force.
		goTo(getX() - xforce, getY() - yforce);
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
	}

	// Itera sobre o hastable e faz considerações sobre o que fazer
	public void evaluate() {
		target = null;
		//TODO levar em conta mais que a distancia.
		for (Bot enemy : enemies.values()) {
			if(target == null || target.distance > enemy.distance){
				target = enemy;
			}
			System.out.println(enemy);
		}
		System.out.println("--");

		for (Bot friend : friends.values()) {
			System.out.println(friend);
		}

		System.out.println();
	}

	public void fire() {
		//sobrecarregado nos outros.
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

}
