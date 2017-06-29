/**
 * 
 */
package org.javahispano.jfootball.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * @author adou
 *
 */
public class CharacterComponent implements Component {
	public Vector3 characterDirection = new Vector3();
	public Vector3 walkDirection = new Vector3();
}