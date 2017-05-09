/**
 * 
 */
package org.javahispano.jfootball;

import org.javahispano.jfootball.Components.ModelComponent;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * @author adou
 *
 */
public class GameWorld {
	private static final float FOV = 67F;
	private ModelBatch modelBatch;
	private Environment environment;
	private PerspectiveCamera perspectiveCamera;
	private Engine engine;

	public GameWorld() {
		engine = new Engine();

		ModelBuilder modelBuilder = new ModelBuilder();
		Material boxMaterial = new Material(ColorAttribute.createDiffuse(Color.WHITE),
				ColorAttribute.createSpecular(Color.RED), FloatAttribute.createShininess(16f));
		Model box = modelBuilder.createBox(5, 5, 5, boxMaterial,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		Entity entity = new Entity();
		entity.add(new ModelComponent(box, 10, 10, 10));
		engine.addEntity(entity);

		initPersCamera();
		initEnvironment();
		initModelBatch();
	}

	private void initPersCamera() {
		perspectiveCamera = new PerspectiveCamera(FOV, Core.VIRTUAL_WIDTH, Core.VIRTUAL_HEIGHT);
		perspectiveCamera.position.set(30f, 40f, 30f);
		perspectiveCamera.lookAt(0f, 0f, 0f);
		perspectiveCamera.near = 1f;
		perspectiveCamera.far = 300f;
		perspectiveCamera.update();
	}

	private void initEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f));
	}

	private void initModelBatch() {
		modelBatch = new ModelBatch();
	}

	/*
	 * The ModelBatch is one of the objects, which require disposing, hence we
	 * add it to the disposefunction.
	 */
	public void dispose() {
		modelBatch.dispose();
	}

	/* With the camera set we can now fill in the resize function as well */
	public void resize(int width, int height) {
		perspectiveCamera.viewportHeight = height;
		perspectiveCamera.viewportWidth = width;
	}

	// and set up the render function with the modelbatch
	public void render(float delta) {
		modelBatch.begin(perspectiveCamera);
		modelBatch.end();
	}
}
