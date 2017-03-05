/**
 * 
 */
package org.javahispano.jfootball.client.application.animations;

import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.user.client.ui.Panel;

/**
 * @author alfonso
 *
 */
public interface Animation extends AnimationCallback, Comparable<Animation> {
	public void start(Panel parent);

	public void stop();

	public String getName();
}
