/**
 * 
 */
package org.javahispano.jfootball.systems;

import java.util.Random;

import org.javahispano.jfootball.GameWorld;
import org.javahispano.jfootball.components.EnemyComponent;
import org.javahispano.jfootball.components.ModelComponent;
import org.javahispano.jfootball.components.PlayerComponent;
import org.javahispano.jfootball.managers.EntityFactory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * @author alfonso
 *
 */
public class EnemySystem extends EntitySystem implements EntityListener {
	private ImmutableArray<Entity> entities;
	private Entity player;
	private Quaternion quat = new Quaternion();
	private Engine engine;
	private GameWorld gameWorld;
	private Vector3 playerPosition = new Vector3();
	private Vector3 enemyPosition = new Vector3();
	private Matrix4 ghost = new Matrix4();
	private Vector3 translation = new Vector3();
	private Random random = new Random();

	private float[] xSpawns = { 12, -12, 112, -112 };
	private float[] zSpawns = { -112, 112, -12, 12 };

	ComponentMapper<PlayerComponent> cm = ComponentMapper.getFor(PlayerComponent.class);

	public EnemySystem(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	@Override
	public void addedToEngine(Engine e) {
		entities = e.getEntitiesFor(Family.one(EnemyComponent.class).get());
		e.addEntityListener(Family.one(PlayerComponent.class).get(), this);
		this.engine = e;
	}

	public void update(float delta) {
		if (entities.size() < 1)
			spawnEnemy(getRandomSpawnIndex());

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			ModelComponent mod = e.getComponent(ModelComponent.class);
			ModelComponent playerModel = player.getComponent(ModelComponent.class);

			playerModel.instance.transform.getTranslation(playerPosition);
			mod.instance.transform.getTranslation(enemyPosition);

			float dX = playerPosition.x - enemyPosition.x;
			float dZ = playerPosition.z - enemyPosition.z;

			float theta = (float) (Math.atan2(dX, dZ));

			// Calculate the transforms
			Quaternion rot = quat.setFromAxis(0, 1, 0, (float) Math.toDegrees(theta) + 90);

			mod.instance.transform.set(translation.x, translation.y, translation.z, rot.x, rot.y, rot.z, rot.w);
		}
	}

	private void spawnEnemy(int randomSpawnIndex) {
		engine.addEntity(EntityFactory.createEnemy(xSpawns[randomSpawnIndex], 33, zSpawns[randomSpawnIndex]));
	}

	@Override
	public void entityAdded(Entity entity) {
		player = entity;
	}

	@Override
	public void entityRemoved(Entity entity) {
	}

	public int getRandomSpawnIndex() {
		return random.nextInt(xSpawns.length);
	}
}
