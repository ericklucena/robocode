package robots;

import java.io.IOException;
import java.util.Hashtable;

import robocode.*;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */

public class TeamLeader extends TeamBot {

	private Bot target = null;

	public Bot getTarget() {
		return target;
	}

	public boolean gambi = false;

	public void setTarget(Bot enemy) {
		this.target = enemy;
	}

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

	public void onMessageReceived(MessageEvent e) {

		Bot bot = (Bot) e.getMessage();

		if (this.isTeammate(bot.getName())) {
			if (bot.alive) {
				friends.put(bot.name, bot);
			} else {
				friends.remove(bot.name);
			}

		} else {
			if (bot.alive) {
				enemies.put(bot.name, bot);
			} else {
				// System.out.println(bot);
				enemies.remove(bot.name);
			}
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		if (isTeammate(e.getName())) {
			return;
		}
		// Calculo de posição do robô sob o scan
		double absbearing_rad = (getHeadingRadians() + e.getBearingRadians())
				% (2 * Math.PI);
		double x = getX() + Math.sin(absbearing_rad) * e.getDistance();
		double y = getY() + Math.cos(absbearing_rad) * e.getDistance();
		Bot scanned = new Bot();
		scanned.update(e, x, y);

		enemies.put(scanned.getName(), scanned);
		try {
			this.broadcastMessage(scanned);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Quando um robô é destruido, verifica se é o atual alvo.
	public void onRobotDeath(RobotDeathEvent e) {
		if (!isTeammate(e.getName())) {
			enemies.remove(e.getName());
		} else {
			friends.remove(e.getName());
		}
	}

}
