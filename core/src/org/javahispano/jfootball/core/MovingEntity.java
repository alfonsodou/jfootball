/**
 * 
 */
package org.javahispano.jfootball.core;

import com.badlogic.gdx.math.Vector3;

/**
 * @author alfonso
 *
 */
public class MovingEntity {
	public Vector3 position;
	
	public MovingEntity() {
		this.position = new Vector3(0, 0,0);
	}
	
	public MovingEntity(Vector3 pos) {
		this.position = pos;
	}
	
	public MovingEntity(float x, float y, float z) {
		this.position = new Vector3(x, y, z);
	}
}
