package robots;

import java.awt.geom.Point2D;

import robots.TeamBot;
import robots.util.RobotUtils;
import robocode.util.Utils;
public class HeadOnTargetBot extends TeamBot {
	public void fire(){
			if(getTarget() == null) return;
//			double angle = RobotUtils.absbearing(location(),getEnemy().location()) - getHeading();
			// talvez levar em conta a energia a ser usada no tiro
			double bulletPower = Math.min(1.5,getEnergy());
			
			double absoluteBearing = getHeadingRadians() + getTarget().getBearing();
			double enemyX = getX() + getTarget().getDistance() * Math.sin(absoluteBearing);
			double enemyY = getY() + getTarget().getDistance() * Math.cos(absoluteBearing);
			
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

}
