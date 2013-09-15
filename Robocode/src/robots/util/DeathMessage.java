package robots.util;

import java.io.Serializable;

public class DeathMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String enemyName;
	
	public DeathMessage(String name, String enemyName){
		this.name = name;
		this.enemyName = enemyName;
	}
		
}
