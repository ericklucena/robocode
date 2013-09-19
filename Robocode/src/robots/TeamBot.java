package robots;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

import robocode.*;
import robocode.util.Utils;
import robots.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AimBot
 */

public class TeamBot extends TeamRobot {
	
	private Bot scanned = null;
	private long lastScannedTime;
	Point2D safePoint; 

	public Bot getScanned() {
		return scanned;
	}

	private Bot target = null;

	public Bot getTarget() {
		return target;
	}

	public boolean gambi = false;

	public void setTarget(Bot enemy) {
		this.target = enemy;
	}

	public Hashtable<String, Bot> enemies;
	public Hashtable<String, Bot> friends;

	boolean direction = false;

	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		safePoint = new Point2D.Double(getBattleFieldWidth()/2, getBattleFieldHeight()/2); 
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

			if ((getTime() - lastScannedTime) > 5) {
				enemies.clear();
			}

			evaluate();
			antiGravMove();
			fire();
			execute();
		} while (true);

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
		
		
		//Descobre um ponto de paz e aplica uma gravidade positiva, para não deixar o robô parado

		if(getTime()%20 == 0){
			safePoint = getSafePoint(5);
		}
		
		p = new GravPoint(safePoint, 1000);
		System.out.println(safePoint);
		force = p.power	/ Math.pow(RobotUtils.getRange(new Point2D.Double(getX(), getY()), p.location), 2);
		ang = RobotUtils.normaliseBearing(Math.PI/2	- Math.atan2(getY() - p.location.getY(), getX()	- p.location.getX()));

		xforce += Math.sin(ang) * force;
		yforce += Math.cos(ang) * force;
		
		//Fim clear point
		

		// TODO marcando para olhar depois e tweakar.
		// if(target != null){
		// p = new GravPoint(target.location(), +1000);
		// force = p.power
		// / Math.pow(RobotUtils.getRange(new Point2D.Double(getX(),
		// getY()), p.location), 1.5);
		// // Find the bearing from the point to us
		// ang = RobotUtils.normaliseBearing(Math.PI
		// / 2
		// - Math.atan2(getY() - p.location.getY(), getX()
		// - p.location.getX()));
		// // Add the components of this force to the total force in their
		// // respective directions
		// xforce += Math.sin(ang) * force;
		// yforce += Math.cos(ang) * force;
		// }

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

public void onMessageReceived(MessageEvent e){
		
		if(e.getMessage() instanceof LockMessage){
			System.out.println("Lock");
			LockMessage l = (LockMessage) e.getMessage();
			
			if(this.scanned!=null){
				if(l.enemyName.equals(scanned.name) &&  l.name.compareTo(getName())<0){
					this.scanned = null;
				}
			}
			
		}else{
			
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
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		if (isTeammate(e.getName())) {
			return;
		}

		Bot enemy = (Bot) enemies.get(e.getName());

		if ((enemy == null) && (this.scanned == null)) {
			this.scanned = new Bot();
			this.scanned.name = e.getName();
			this.lastScannedTime = getTime();

			try {
				this.broadcastMessage(new LockMessage(this.getName(), scanned.name));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//Calculo de posição do robô sob o scan
		double absbearing_rad = (getHeadingRadians() + e.getBearingRadians()) % (2 * Math.PI);
		double x = getX() + Math.sin(absbearing_rad) * e.getDistance();
		double y = getY() + Math.cos(absbearing_rad) * e.getDistance();
		Bot scanned = new Bot();
		scanned.update(e, x, y);

		enemies.put(scanned.getName(), scanned);
		if(scanned!=null){
			if(this.scanned.name.equals(e.getName())){
				this.lastScannedTime = getTime();
				//Calculo de movimentação do radar para manter o alvo sob scan constante
				double angleToEnemy = getHeadingRadians() + e.getBearingRadians();	
				double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );
				//double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );
				double bodyTurn = (Math.PI/2) + getGunHeadingRadians();
				bodyTurn = Utils.normalRelativeAngle(bodyTurn -getHeadingRadians());
				double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );
		
				radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
				setTurnRadarRightRadians(radarTurn);
				
				try {
					this.broadcastMessage(scanned);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
		
		if(e.getName().equals(scanned.getName())){
			System.out.println("Yippee ki-yay, motherfucker!");
			enemies.remove(e.getName());
			scanned = null;
		}
		
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
		target = null;
		// TODO levar em conta mais que a distancia.
		for (Bot enemy : enemies.values()) {
			if (target == null || target.distance > enemy.distance) {
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
	
	public Point2D getSafePoint(int gridSize){
		
		int[][] grid = new int[gridSize][gridSize];
		
		Point2D[] bestPoints = new Point2D.Double[gridSize*gridSize];
		
		int height = (int) this.getBattleFieldHeight();
		int width = (int) this.getBattleFieldWidth();
		int hIncrement = height/gridSize;
		int wIncrement = width/gridSize;
		
				
		for(int w=wIncrement, i=0 ; w<=width; w+=wIncrement, i++){
			for(int h=hIncrement, j=0 ; h<=height; h+=hIncrement, j++){
				for (Bot b : enemies.values()) {
					if( (b.x < w) && (b.x > w-wIncrement) && (b.y < h) && (b.y > h-hIncrement)){
						grid[i][j]++;						
					}
				}
			}
		}
		
		int min=5000;
		int k=0;
		
		int x=0, y=0;
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j]<min){
					x=i;
					y=j;
					min = grid[i][j];
				}
			}			
		}
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j]==min){
					bestPoints[k++] = new Point2D.Double(i, j);
				}
			}			
		}
		
		Random r = new Random();
		
		Point2D p = bestPoints[r.nextInt(k)];
		
		x = (int) p.getX();
		y = (int) p.getY();
		
		x *= wIncrement;
		y *= hIncrement;
		
		x += wIncrement;
		y += hIncrement;
		
		return new Point2D.Double(x, y);

	}

}
