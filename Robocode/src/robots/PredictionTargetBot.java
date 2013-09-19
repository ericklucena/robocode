package robots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

import robots.TeamBot;
import robots.util.Bot;
import robots.util.RobotUtils;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.Rules;
import robocode.util.Utils;
public class PredictionTargetBot extends TeamBot {
	
	
	Queue<Point2D> toDraw = new LinkedList<Point2D>();
	Point2D aim = null;
	Point2D shot = null;
	int shotCount = 0;
	
	public void fire(){
//			if(getTarget() == null) 
//				{
//				System.out.println("EPA");
//				return;
//				}
		if(enemies.size() == 0 ) return;
			double[] angles = new double[enemies.size()];
			double[] distances = new double[enemies.size()];
			
			double bulletPower = Math.min(1.5,getEnergy());
			int i = 0;
			for(Bot b : enemies.values()){
				toDraw.add(new Point2D.Double(0, 0));
				double enemyX = b.x;
				double enemyY = b.y;
				double enemyHeading = b.getHeading();
				double enemyVelocity = b.getVelocity();
				double enemyTurningAngle = b.turning;
				double enemyAcceleration = b.acceleration;
				double deltaTime = 0;
				double battleFieldHeight = getBattleFieldHeight(), 
				       battleFieldWidth = getBattleFieldWidth();
				double predictedX = enemyX, predictedY = enemyY;
				while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
				      RobotUtils.getRange(location(),new Point2D.Double(predictedX,predictedY))){
					bulletPower = Math.min(3 - ((RobotUtils.getRange(location(),new Point2D.Double(predictedX,predictedY))-100)/250  ),getEnergy());
					toDraw.add(new Point2D.Double(predictedX, predictedY));
					predictedX += Math.sin(enemyHeading) * enemyVelocity;	
					predictedY += Math.cos(enemyHeading) * enemyVelocity;
					
					enemyHeading += enemyTurningAngle;
					if(enemyAcceleration > 0)
						enemyVelocity = Math.min(8.0 , enemyVelocity + enemyAcceleration);
					else{
						//tenso
						if(enemyVelocity < 0){
							enemyAcceleration = Math.min(-1.0, enemyAcceleration);
							enemyVelocity = Math.max(-8.0 , enemyVelocity + enemyAcceleration);
						}else{
							enemyVelocity += enemyAcceleration;
						}
						
					}
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
				
				distances[i] =  RobotUtils.getRange(new Point2D.Double(predictedX,predictedY), location()); 
				angles[i++] = Utils.normalAbsoluteAngle(Math.atan2(
				    predictedX - getX(), predictedY - getY()));
				
			}
			
			
			
//			aim = new Point2D.Double(predictedX,predictedY);
//			setTurnRadarRightRadians(
//			    Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
			int escolhido = 0;
			for(int j = 0; j < enemies.size(); j++){
				if(distances[escolhido] > distances[j]){
					escolhido = j;
				}
			}
			setTurnGunRightRadians(Utils.normalRelativeAngle(angles[escolhido] - getGunHeadingRadians()));
		if(this.getGunHeat() <= 0 && Math.abs(angles[escolhido] - getGunHeadingRadians()) < Rules.GUN_TURN_RATE_RADIANS){
			setFire(bulletPower);
			double realGunTurn = (angles[escolhido] > 0? Math.min(angles[escolhido], Rules.GUN_TURN_RATE_RADIANS):Math.max(distances[escolhido], - Rules.GUN_TURN_RATE_RADIANS));
			double realAngle = getGunHeadingRadians() + realGunTurn;
			double realX = getX() + distances[escolhido] * Math.sin(realAngle);
			double realY = getY() + distances[escolhido] * Math.cos(realAngle);
			shot = new Point2D.Double(realX,realY);
			shotCount = 30;
		}
		else{
			shotCount--;
//			shot = null;
		}
	}
	private static final int CIRCLE_RADIUS = 30;
	public void onPaint(Graphics2D g){
		BasicStroke stroke = new BasicStroke(2.0f);
		g.setStroke(stroke);
//		if(getTarget() != null){
//			g.setColor(new Color(0x00, 0xBB, 0xFF, 0x80));
//			g.drawOval( (int) (getTarget().x - CIRCLE_RADIUS), (int) (getTarget().y - CIRCLE_RADIUS), CIRCLE_RADIUS*2, CIRCLE_RADIUS*2);
//			g.setColor(new Color(0x00, 0xFF, 0x00, 0x80));
//			double angle = getGunHeadingRadians();
//			g.drawOval( (int) (getX() - CIRCLE_RADIUS), (int) (getY() - CIRCLE_RADIUS), CIRCLE_RADIUS*2, CIRCLE_RADIUS*2);
//			g.drawLine((int) (getX() + Math.sin(angle)*CIRCLE_RADIUS), (int)(getY() + Math.cos(angle)*CIRCLE_RADIUS), (int)(getX() + Math.sin(angle)*1000), (int)(getY() + Math.cos(angle)*1000));
//			g.setColor(new Color(0xFF, 0x00, 0x00, 0x80));	
//		}
		
		if(!toDraw.isEmpty()){
			Point2D p = toDraw.element();
			boolean reset = true;
			while(!toDraw.isEmpty()){
				
				g.setColor(new Color(0x00, 0xBB, 0x55, 0x80));
				Point2D p2 = toDraw.element();
				if(p2.getX() < 0.1 || p.getX() < 0.1) reset = true;
				if(!reset)
				g.drawLine((int)p.getX(), (int)p.getY(), (int)p2.getX(), (int)p2.getY());
				p = p2;
				toDraw.remove();
				reset = false;
			}
		}
		if(aim != null){
			g.setStroke(new BasicStroke(2.0f));
			g.setColor(new Color(0xFF, 0x00, 0x00, 0xDD));
			g.drawOval( (int) (aim.getX() - CIRCLE_RADIUS+2), (int) (aim.getY() - CIRCLE_RADIUS+2), CIRCLE_RADIUS*2-4, CIRCLE_RADIUS*2-4);
			g.setColor(new Color(0x00, 0xFF, 0x00, 0x80));
		}

		if(shot != null){
			g.setStroke(new BasicStroke(2.0f));
			g.setColor(new Color(0xFF, 0xBB, 0x00, 0xDD*shotCount/30));
			g.drawOval( (int) (shot.getX() - CIRCLE_RADIUS+2), (int) (shot.getY() - CIRCLE_RADIUS+2), CIRCLE_RADIUS*2-4, CIRCLE_RADIUS*2-4);
			g.setColor(new Color(0x00, 0xFF, 0x00, 0x80));
		}
	}
	@Override
	public void onBulletHit(BulletHitEvent event) {
		
	}
	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		
	}
	

}
