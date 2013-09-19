package robots.util;

import java.io.Serializable;

public class BulletEndMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long hitTime;
	public double x;
	public double y;
	public long id;
	public Bot hit;
	
	public BulletEndMessage(long id,long hitTime,double x, double y, Bot hit){
		this.hit = hit;
		this.id = id;
		this.hitTime = hitTime;
		this.x = x;
		this.y = y;
	}
		
}
