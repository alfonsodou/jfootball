/**
 * 
 */
package org.javahispano.jfootball.components;

import java.awt.Component;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * @author alfonso
 *
 */
public class AnimationComponent extends Component {
	private AnimationController animationController;

	public AnimationComponent(ModelInstance instance) {
		animationController = new AnimationController(instance);
		animationController.allowSameAnimation = true;
	}

	public void animate(final String id, final int loops, final int speed) {
		animationController.animate(id, loops, speed, null, 0);
	}

	public void animate(String id, float offset, float duration, int loopCount, int speed) {
		animationController.animate(id, offset, duration, loopCount, speed, null, 0);
	}

	public void update(float delta) {
		animationController.update(delta);
	}
}