package robots;

import java.awt.geom.Point2D;

import robots.TeamBot;
import robots.util.RobotUtils;
import robocode.util.Utils;
public class LinearTargetBot extends TeamBot {
	public void fire(){
			if(getTarget() == null) return;
//			double angle = RobotUtils.absbearing(location(),getEnemy().location()) - getHeading();
			// talvez levar em conta a energia a ser usada no tiro
			double bulletPower = Math.min(1.5,getEnergy());
			
			double absoluteBearing = getHeadingRadians() + getTarget().getBearing();
			double enemyX = getX() + getTarget().getDistance() * Math.sin(absoluteBearing);
			double enemyY = getY() + getTarget().getDistance() * Math.cos(absoluteBearing);
			double enemyHeading = getTarget().getHeading();
			double enemyVelocity = getTarget().getVelocity();
			 
			 
			double deltaTime = 0;
			double battleFieldHeight = getBattleFieldHeight(), 
			       battleFieldWidth = getBattleFieldWidth();
			double predictedX = enemyX, predictedY = enemyY;
			while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
			      RobotUtils.getRange(location(),new Point2D.Double(predictedX,predictedY))){		
				predictedX += Math.sin(enemyHeading) * enemyVelocity;	
				predictedY += Math.cos(enemyHeading) * enemyVelocity;
				if(	predictedX < 18.0 
					|| predictedY < 18.0
					|| predictedX > battleFieldWidth - 18.0
					|| predictedY > battleFieldHeight - 18.0){
					predictedX = Math.min(Math.max(18.0, predictedX), 
			                    battleFieldWidth - 18.0);	
					predictedY = Math.min(Math.max(18.0, predictedY), 
			                    battleFieldHeight - 18.0);
					break;
				}
			}
			double theta = Utils.normalAbsoluteAngle(Math.atan2(
			    predictedX - getX(), predictedY - getY()));
			 
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
