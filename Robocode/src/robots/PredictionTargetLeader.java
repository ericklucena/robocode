package robots;

import java.io.IOException;

import robocode.*;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */

public class PredictionTargetLeader extends PredictionTargetBot {

	public void onMessageReceived(MessageEvent e) {
		Bot bot = (Bot) e.getMessage();
		bot.updateDistance(this.getX(), this.getY());
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
