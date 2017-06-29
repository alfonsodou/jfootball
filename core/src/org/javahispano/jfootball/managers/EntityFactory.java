/**
 * 
 */
package org.javahispano.jfootball.managers;

import org.javahispano.jfootball.components.AnimationComponent;
import org.javahispano.jfootball.components.CharacterComponent;
import org.javahispano.jfootball.components.EnemyComponent;
import org.javahispano.jfootball.components.ModelComponent;
import org.javahispano.jfootball.components.PlayerComponent;
import org.javahispano.jfootball.systems.RenderSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;

/**
 * @author alfonso
 *
 */
public class EntityFactory {
	private static Model playerModel, enemyModel;
	private static Texture playerTexture;
	private static ModelBuilder modelBuilder;
	public static RenderSystem renderSystem;
	private static ModelData enemyModelData;
	private static ModelComponent enemyModelComponent;

	static {
		modelBuilder = new ModelBuilder();
		playerTexture = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		Material material = new Material(TextureAttribute.createDiffuse(playerTexture),
				ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
		playerModel = modelBuilder.createCapsule(2f, 6f, 16, material, VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
	}

	private static Entity createCharacter(float x, float y, float z) {
		Entity entity = new Entity();
		ModelComponent modelComponent = new ModelComponent(playerModel, x, y, z);
		entity.add(modelComponent);
		CharacterComponent characterComponent = new CharacterComponent();
		entity.add(characterComponent);
		return entity;
	}

	public static Entity createPlayer(float x, float y, float z) {
		Entity entity = createCharacter(x, y, z);
		entity.add(new PlayerComponent());
		return entity;
	}

	public static Entity createEnemy(float x, float y, float z) {
		Entity entity = new Entity();
		ModelLoader<?> modelLoader = new G3dModelLoader(new JsonReader());
		if (enemyModel == null) {
			enemyModelData = modelLoader.loadModelData(Gdx.files.internal("data/monster.g3dj"));
			enemyModel = new Model(enemyModelData, new TextureProvider.FileTextureProvider());
			for (Node node : enemyModel.nodes)
				node.scale.scl(0.0025f);
			enemyModel.calculateTransforms();
			enemyModelComponent = new ModelComponent(enemyModel, x, y, z);

			Material material = enemyModelComponent.instance.materials.get(0);
			BlendingAttribute blendingAttribute;
			material.set(blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
			enemyModelComponent.blendingAttribute = blendingAttribute;
		}
		entity.add(enemyModelComponent);
		CharacterComponent characterComponent = new CharacterComponent();
		entity.add(characterComponent);
		entity.add(new EnemyComponent(EnemyComponent.STATE.HUNTING));
		AnimationComponent animationComponent = new AnimationComponent(enemyModelComponent.instance);
		animationComponent.animate(EnemyAnimations.id, EnemyAnimations.offsetRun1, EnemyAnimations.durationRun1, -1, 1); // TODO
																															// variable
																															// animationspeed
		entity.add(animationComponent);

		return entity;
	}

	public static Entity loadDome(int x, int y, int z) {
		UBJsonReader jsonReader = new UBJsonReader();
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		Model model = modelLoader.loadModel(Gdx.files.getFileHandle("data/spacedome.g3db", Files.FileType.Internal));
		ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		Entity entity = new Entity();
		entity.add(modelComponent);
		return entity;
	}

	public static Entity loadScene(int x, int y, int z) {
		Entity entity = new Entity();
		ModelLoader<?> modelLoader = new G3dModelLoader(new JsonReader());
		ModelData modelData = modelLoader.loadModelData(Gdx.files.internal("data/arena.g3dj"));
		Model model = new Model(modelData, new TextureProvider.FileTextureProvider());
		ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		entity.add(modelComponent);
		return entity;
	}

	// public static void dispose() {
	// playerModel.dispose();
	// playerTexture.dispose();
	// boxModel.dispose();
	// }
}