/**
 * 
 */
package org.javahispano.jfootball.systems;

import org.javahispano.jfootball.GameWorld;
import org.javahispano.jfootball.components.ModelComponent;
import org.javahispano.jfootball.components.PlayerComponent;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * @author alfonso
 *
 */
public class PlayerSystem extends EntitySystem implements EntityListener, InputProcessor {
	private Entity player;
	private PlayerComponent playerComponent;
	private ModelComponent modelComponent;
	private final Vector3 tmp = new Vector3();
	private final Camera camera;
	private GameWorld gameWorld;
	private float deltaX;
	private float deltaY;
	private Vector3 translation = new Vector3();
	private Matrix4 ghost = new Matrix4();
	public Entity dome;
	
	public PlayerSystem(GameWorld gameWorld, Camera camera) {
		this.camera = camera;
		this.gameWorld = gameWorld;
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(PlayerComponent.class).get(), this);
	}

	@Override
	public void update(float delta) {
		if (player == null)
			return;
		updateMovement(delta);
		// updateStatus();
		// checkGameOver();
	}

	private void updateMovement(float delta) {
		deltaX = -Gdx.input.getDeltaX() * 0.5f;
		deltaY = -Gdx.input.getDeltaY() * 0.5f;
		tmp.set(0, 0, 0);
		camera.rotate(camera.up, deltaX);
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, deltaY);
		tmp.set(0, 0, 0);
		playerComponent.characterDirection.set(-1, 0, 0).rot(modelComponent.instance.transform).nor();
		playerComponent.walkDirection.set(0, 0, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			playerComponent.walkDirection.add(camera.direction);
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			playerComponent.walkDirection.sub(camera.direction);
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			tmp.set(camera.direction).crs(camera.up).scl(-1);
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			tmp.set(camera.direction).crs(camera.up);
		playerComponent.walkDirection.add(tmp);
		playerComponent.walkDirection.scl(10f * delta);
		ghost.set(0, 0, 0, 0);
		translation.set(0, 0, 0);
		translation = new Vector3();
		ghost.getTranslation(translation);
		modelComponent.instance.transform.set(translation.x, translation.y, translation.z, camera.direction.x,
				camera.direction.y, camera.direction.z, 0);
		camera.position.set(translation.x, translation.y, translation.z);
		camera.update(true);
	}

	@Override
	public void entityAdded(Entity entity) {
		player = entity;
		playerComponent = entity.getComponent(PlayerComponent.class);
		modelComponent = entity.getComponent(ModelComponent.class);
		//
	}

	@Override
	public void entityRemoved(Entity entity) {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
