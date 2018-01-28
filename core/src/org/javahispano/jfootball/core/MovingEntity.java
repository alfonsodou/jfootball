/**
 * 
 */
package org.javahispano.jfootball.core;

import org.javahispano.jfootball.core.engine.util.Position;

/**
 * @author alfonso
 *
 */
public class MovingEntity {
	public Position position;

	public MovingEntity() {
		this.position = new Position(0, 0, 0);
	}

	public MovingEntity(Position pos) {
		this.position = pos;
	}

	public MovingEntity(double x, double y, double z) {
		this.position = new Position(x, y, z);
	}
}
