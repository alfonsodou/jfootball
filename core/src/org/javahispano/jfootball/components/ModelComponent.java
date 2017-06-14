/**
 * 
 */
package org.javahispano.jfootball.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.math.Matrix4;

/**
 * @author adou
 *
 */
public class ModelComponent implements Component {

	public Model model;
	public ModelInstance instance;
	public Matrix4 matrix4;
	public BlendingAttribute blendingAttribute;

	public ModelComponent(Model model, float x, float y, float z) {
		this.matrix4 = new Matrix4();
		this.model = model;
		this.instance = new ModelInstance(model, matrix4.setToTranslation(x, y, z));
	}

	public void update(float delta) {
		if (blendingAttribute != null)
			blendingAttribute.opacity = blendingAttribute.opacity - delta / 3;
	}
}
