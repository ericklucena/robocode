package robots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

import robots.TeamBot;
import robots.util.RobotUtils;
import robocode.util.Utils;
public class CircularTargetBot extends TeamBot {
	
	
	Queue<Point2D> toDraw = new LinkedList<Point2D>();
	
	public void fire(){
		
//			double angle = RobotUtils.absbearing(location(),getEnemy().location()) - getHeading();
			// talvez levar em conta a energia a ser usada no tiro
			double bulletPower = Math.min(1.5,getEnergy());
			
			double absoluteBearing = getHeadingRadians() + getEnemy().getBearing();
			double enemyX = getX() + getEnemy().getDistance() * Math.sin(absoluteBearing);
			double enemyY = getY() + getEnemy().getDistance() * Math.cos(absoluteBearing);
			double enemyHeading = getEnemy().getHeading();
			double enemyVelocity = getEnemy().getVelocity();
			double enemyTurningAngle = getEnemy().turning;
			 
			 
			double deltaTime = 0;
			double battleFieldHeight = getBattleFieldHeight(), 
			       battleFieldWidth = getBattleFieldWidth();
			double predictedX = enemyX, predictedY = enemyY;
			while((++deltaTime) * (CIRCLE_RADIUS - 3.0 * bulletPower) < 
			      RobotUtils.getRange(location(),new Point2D.Double(predictedX,predictedY))){
				toDraw.add(new Point2D.Double(predictedX, predictedY));
				predictedX += Math.sin(enemyHeading) * enemyVelocity;	
				predictedY += Math.cos(enemyHeading) * enemyVelocity;
				enemyHeading += enemyTurningAngle;
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
	private static final int CIRCLE_RADIUS = 30;
	public void onPaint(Graphics2D g){
		if(getEnemy() != null){
			g.setStroke(new BasicStroke(2.0f));
			g.setColor(new Color(0x00, 0xBB, 0xFF, 0x80));
			g.drawOval( (int) (getEnemy().x - CIRCLE_RADIUS), (int) (getEnemy().y - CIRCLE_RADIUS), CIRCLE_RADIUS*2, CIRCLE_RADIUS*2);
			g.setColor(new Color(0x00, 0xFF, 0x00, 0x80));
			double angle = getGunHeadingRadians();
			g.drawOval( (int) (getX() - CIRCLE_RADIUS), (int) (getY() - CIRCLE_RADIUS), CIRCLE_RADIUS*2, CIRCLE_RADIUS*2);
			g.drawLine((int) (getX() + Math.sin(angle)*CIRCLE_RADIUS), (int)(getY() + Math.cos(angle)*CIRCLE_RADIUS), (int)(getEnemy().x + Math.sin(angle)*1000), (int)(getEnemy().y + Math.cos(angle)*1000));
			g.setColor(new Color(0xFF, 0x00, 0x00, 0x80));
			
		}
		
		if(!toDraw.isEmpty()){
			BasicStroke stroke = new BasicStroke(4.0f);
			g.setStroke(stroke);
			Point2D p = toDraw.element();
			
			while(!toDraw.isEmpty()){
				g.setColor(new Color(0x00, 0xBB, 0x55, 0x80));
				Point2D p2 = toDraw.element();
				g.drawLine((int)p.getX(), (int)p.getY(), (int)p2.getX(), (int)p2.getY());
				p = p2;
				toDraw.remove();
			}
		}
	}

}
