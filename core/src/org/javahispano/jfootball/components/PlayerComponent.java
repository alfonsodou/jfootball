/**
 * 
 */
package org.javahispano.jfootball.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * @author alfonso
 *
 */
public class PlayerComponent implements Component {
	public float energy;
	public float health;
	public static int score;
	public Vector3 characterDirection;
	public Vector3 walkDirection;
	
	public PlayerComponent() {
		score = 0;
		energy = 100;
		health = 100;
		characterDirection = new Vector3();
		walkDirection = new Vector3();
	}
}
