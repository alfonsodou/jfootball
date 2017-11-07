/**
 * 
 */
package org.javahispano.jfootball.views;

import org.javahispano.jfootball.Jfootball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

/**
 * @author alfonso
 *
 */
public class MainScreen implements Screen {
	private static final float FOV = 67f;
	private Jfootball parent;
	private Environment environment;
	private PerspectiveCamera camera;
	private CameraInputController cameraController;
	private ModelBatch modelBatch;
	private ModelInstance soccerInstance;
	private ModelInstance ballInstance;

	public MainScreen(Jfootball jfootball) {
		parent = jfootball;

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, 4f, 9f);
		camera.lookAt(0f, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();

		cameraController = new CameraInputController(camera);
		Gdx.input.setInputProcessor(cameraController);
		
		parent.getMyAssetManager().queueAddSoccer();
		parent.getMyAssetManager().queueAddBall();
		parent.getMyAssetManager().getManager().finishLoading();
		soccerInstance = loadObject(parent.getMyAssetManager().getSoccer());
		ballInstance = loadObject(parent.getMyAssetManager().getBall());
		ballInstance.transform.setToTranslation(0.5f, 0.13f, 0f);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		cameraController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(soccerInstance, environment);
		modelBatch.render(ballInstance, environment);
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
	}

	private void loadSoccer(String stringSoccer) {
		Model soccer = parent.getMyAssetManager().getManager().get(stringSoccer, Model.class);
		soccerInstance = new ModelInstance(soccer);
		for (Material mat : soccerInstance.materials) {
			mat.remove(IntAttribute.CullFace);
		}

		for (Material mat : soccerInstance.materials) {
			mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		}
	}

	private ModelInstance loadObject(String stringObject) {
		Model model = parent.getMyAssetManager().getManager().get(stringObject, Model.class);
		ModelInstance modelInstance = new ModelInstance(model);
		for (Material mat : modelInstance.materials) {
			mat.remove(IntAttribute.CullFace);
		}

		for (Material mat : modelInstance.materials) {
			mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		}
		
		return modelInstance;
	}

}
