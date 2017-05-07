package org.javahispano.jfootball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Jfootball extends ApplicationAdapter {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public AssetManager assets;
	public OrthographicCamera orthoCam;
	public SpriteBatch spriteBatch;
	public BitmapFont font;
	public StringBuilder stringBuilder = new StringBuilder();
	private Vector3 position = new Vector3();

	@Override
	public void create() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(5, 20, 20);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 100f;
		cam.update();

		assets = new AssetManager();
		assets.load("Ball.g3db", Model.class);
		assets.finishLoading();
		model = assets.get("Ball.g3db", Model.class);
		for (float x = -30; x <= 10f; x += 20) {
			for (float z = -30f; z <= 0f; z += 10f) {
				ModelInstance instance = new ModelInstance(model);
				instance.transform.setToTranslation(x, 0, z);
				instances.add(instance);
			}
		}

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		orthoCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		orthoCam.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void render() {
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		int count = 0;
		for (ModelInstance instance : instances) {
			if (isVisible(cam, instance)) {
				modelBatch.render(instance, environment);
				count++;
			}
		}
		modelBatch.end();

		orthoCam.update();
		spriteBatch.setProjectionMatrix(orthoCam.combined);
		spriteBatch.begin();
		stringBuilder.setLength(0);
		stringBuilder.append("FPS: " + Gdx.graphics.getFramesPerSecond()).append("\n");
		stringBuilder.append("Balls: " + count).append("\n");
		stringBuilder.append("Total: " + instances.size).append("\n");
		font.draw(spriteBatch, stringBuilder, 0, Gdx.graphics.getHeight());
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		assets.dispose();
	}

	private boolean isVisible(final Camera cam, final ModelInstance instance) {
		instance.transform.getTranslation(position);
		return cam.frustum.pointInFrustum(position);
	}

}
