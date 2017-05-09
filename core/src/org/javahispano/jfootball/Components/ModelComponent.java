/**
 * 
 */
package org.javahispano.jfootball.Components;

import java.awt.Component;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;

/**
 * @author adou
 *
 */
public class ModelComponent extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8973282301269419317L;
	
	public Model model;
	public ModelInstance instance;

	public ModelComponent(Model model, float x, float y, float z) {
		this.model = model;
		this.instance = new ModelInstance(model, new Matrix4().setToTranslation(x, y, z));
	}
}
