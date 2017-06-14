/**
 * 
 */
package org.javahispano.jfootball.components;

import com.badlogic.ashley.core.Component;

/**
 * @author alfonso
 *
 */
public class EnemyComponent implements Component {

	public enum STATE {
		IDLE, FLEEING, HUNTING
	}

	public STATE state = STATE.IDLE;

	public EnemyComponent(STATE state) {
		this.state = state;
	}
}
