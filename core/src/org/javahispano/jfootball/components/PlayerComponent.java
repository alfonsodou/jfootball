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
	
	public PlayerComponent() {
		score = 0;
		energy = 100;
		health = 100;
	}
}
