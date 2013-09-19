package robots;


import robots.TeamBot;
import robots.util.Bot;
import robocode.util.Utils;
public class HeadOnTargetBot extends TeamBot {
	public void fire(){
			if(getTarget() == null) return;
//			double angle = RobotUtils.absbearing(location(),getEnemy().location()) - getHeading();
			// talvez levar em conta a energia a ser usada no tiro
			double bulletPower = Math.min(1.5,getEnergy());
			double enemyX = getTarget().x;
			double enemyY = getTarget().y;
			double theta = Utils.normalAbsoluteAngle(Math.atan2(
			    enemyX - getX(), enemyY - getY()));
			 
//			setTurnRadarRightRadians(
//			    Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
			setTurnGunRightRadians(Utils.normalRelativeAngle(theta - getGunHeadingRadians()));
		if(this.getGunHeat() <= 0){
			setFire(bulletPower);
		}
		else{
		}
	}
	public void evaluate(){
		setTarget(null);
		//TODO levar em conta mais que a distancia.
		for (Bot enemy : enemies.values()) {
			if(getTarget() == null || getTarget().distance > enemy.distance){
				setTarget(enemy);
			}
			System.out.println(enemy);
		}
		System.out.println("--");

		for (Bot friend : friends.values()) {
			System.out.println(friend);
		}

		System.out.println();
	}

}
