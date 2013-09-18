package robots.util;

import java.io.Serializable;

public class BulletShotMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long shotTime;
	public double x;
	public double y;
	public double angle;
	public long id;
	Bot target;
	
	public BulletShotMessage(long id,long shotTime,double x, double y, double angle,Bot target){
		this.target = target;
		this.id = id;
		this.shotTime = shotTime;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
		
}
