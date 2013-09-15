package robots.util;

import java.io.Serializable;

public class LockMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String enemyName;
	
	public LockMessage(String name, String enemyName){
		this.name = name;
		this.enemyName = enemyName;
	}
		
}
