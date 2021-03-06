package org.javahispano.jfootball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Core extends ApplicationAdapter {
	private static final float FOV = 67f;
	public static final float VIRTUAL_WIDTH = 1024;
	public static final float VIRTUAL_HEIGHT = 768;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ModelInstance> playersTeamA = new Array<ModelInstance>();
	public Array<ModelInstance> playersTeamB = new Array<ModelInstance>();
	public ModelInstance ballInstance;
	public ModelInstance soccerInstance ;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Environment environment;
	public boolean loading;
	public float rotation;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 4f, 9f);
		cam.lookAt(0f, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();
		assets.load("soccer.g3dj", Model.class);
		assets.load("ybot_reducido_2.g3dj", Model.class);
		assets.load("Soccer Penalty Kick_reducido.g3dj", Model.class);
		assets.load("soccerball_1_reducido.g3dj", Model.class);
		loading = true;
	}

	private void doneLoading() {
		loading = true;

		loadSoccer("soccer.g3dj");
		loadBall("soccerball_1_reducido.g3dj", 0.5f, 0.13f, 0f);
		

		addObject("ybot_reducido_2.g3dj", 9f, 0.1f, 0f);
		addObject("ybot_reducido_2.g3dj", 6f, 0.1f, 2f);
		addObject("ybot_reducido_2.g3dj", 6f, 0.1f, -2f);
		addObject("ybot_reducido_2.g3dj", 5.5f, 0.1f, 0f);
		addObject("ybot_reducido_2.g3dj", 4.5f, 0.1f, 4f);
		addObject("ybot_reducido_2.g3dj", 4.5f, 0.1f, -4f);
		addObject("ybot_reducido_2.g3dj", 3.5f, 0.1f, 5f);
		addObject("ybot_reducido_2.g3dj", 3.5f, 0.1f, -5f);
		addObject("ybot_reducido_2.g3dj", 2f, 0.1f, 3.5f);
		addObject("ybot_reducido_2.g3dj", 2f, 0.1f, -3.5f);
		addObject("ybot_reducido_2.g3dj", 0.5f, 0.1f, 0f);

		addObject("Soccer Penalty Kick_reducido.g3dj", -9f, 0.1f, 0f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -6f, 0.1f, 2f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -6f, 0.1f, -2f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -5.5f, 0.1f, 0f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -4.5f, 0.1f, 4f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -4.5f, 0.1f, -4f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -3.5f, 0.1f, 5f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -3.5f, 0.1f, -5f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -2f, 0.1f, 3.5f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -2f, 0.1f, -3.5f);
		addObject("Soccer Penalty Kick_reducido.g3dj", -0.5f, 0.1f, 0f);

		loading = false;
	}

	private void loadSoccer(String stringSoccer) {
		Model soccer = assets.get(stringSoccer, Model.class);
		soccerInstance = new ModelInstance(soccer);
		for (Material mat : soccerInstance.materials) {
			mat.remove(IntAttribute.CullFace);
		}

		for (Material mat : soccerInstance.materials) {
			mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		}
		instances.add(soccerInstance);
	}

	private void loadBall(String stringBall, float x, float y, float z) {
		Model ball = assets.get(stringBall, Model.class);
		ballInstance = new ModelInstance(ball);
		for (Material mat : ballInstance.materials) {
			mat.remove(IntAttribute.CullFace);
		}

		for (Material mat : ballInstance.materials) {
			mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		}
		ballInstance.transform.setToTranslation(x, y, z);
		instances.add(ballInstance);
	}	
	
	private void addObject(String desc, float x, float y, float z) {
		Model player = assets.get(desc, Model.class);
		ModelInstance playerInstance = new ModelInstance(player);
		for (Material mat : playerInstance.materials) {
			mat.remove(IntAttribute.CullFace);
		}

		for (Material mat : playerInstance.materials) {
			mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		}
		playerInstance.transform.setToTranslation(x, y, z);
		instances.add(playerInstance);
	}

	@Override
	public void render() {
		if (loading && assets.update())
			doneLoading();
		
		rotate();
		
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	private void rotate() {
		rotation = (rotation + Gdx.graphics.getDeltaTime() * 100) % 360;
		Vector3 position = new Vector3();
		//for (ModelInstance instance : instances) {
			ballInstance.transform.getTranslation(position);
			ballInstance.transform.setFromEulerAngles(0, 0, rotation).trn(position.x, position.y, position.z);
		//}
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public AssetManager getAssetManager() {
		return assets;
	}
}
