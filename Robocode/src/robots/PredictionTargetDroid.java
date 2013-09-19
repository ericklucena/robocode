package robots;

import java.io.IOException;

import robocode.*;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */

public class PredictionTargetDroid extends PredictionTargetBot implements Droid {

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
	// Quando um robô é destruido, verifica se é o atual alvo.
	public void onRobotDeath(RobotDeathEvent e) {
		if (!isTeammate(e.getName())) {
			enemies.remove(e.getName());
		} else {
			friends.remove(e.getName());
		}
	}
}
