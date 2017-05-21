/**
 * 
 */
package org.javahispano.jfootball;

import org.javahispano.jfootball.managers.EntityFactory;
import org.javahispano.jfootball.systems.PlayerSystem;
import org.javahispano.jfootball.systems.RenderSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.sun.scenario.Settings;

/**
 * @author adou
 *
 */
public class GameWorld {
    private static final boolean debug = false;
    private Engine engine;
    private Entity character, gun, dome;
    public PlayerSystem playerSystem;
    private RenderSystem renderSystem;

    public GameWorld() {
        addSystems();
        addEntities();
    }

    private void addSystems() {
        engine = new Engine();
        engine.addSystem(renderSystem = new RenderSystem());
        EntityFactory.renderSystem = renderSystem;
        engine.addSystem(playerSystem = new PlayerSystem(this, renderSystem.perspectiveCamera));
    }

    private void addEntities() {
        loadLevel();
        createPlayer(0, 6, 0);
    }

    private void loadLevel() {
        engine.addEntity(EntityFactory.loadScene(0, 0, 0));
        engine.addEntity(dome = EntityFactory.loadDome(0, 0, 0));
        playerSystem.dome = dome;
    }

    private void createPlayer(float x, float y, float z) {
        character = EntityFactory.createPlayer(bulletSystem, x, y, z);
        engine.addEntity(character);
        engine.addEntity(gun = EntityFactory.loadGun(2.5f, -1.9f, -4));
        playerSystem.gun = gun;
        renderSystem.gun = gun;
    }

    public void render(float delta) {
        renderWorld(delta);
        checkPause();
    }

    private void checkPause() {
        if (Settings.Paused) {
            engine.getSystem(PlayerSystem.class).setProcessing(false);
            engine.getSystem(EnemySystem.class).setProcessing(false);
            engine.getSystem(StatusSystem.class).setProcessing(false);
            engine.getSystem(BulletSystem.class).setProcessing(false);
        } else {
            engine.getSystem(PlayerSystem.class).setProcessing(true);
            engine.getSystem(EnemySystem.class).setProcessing(true);
            engine.getSystem(StatusSystem.class).setProcessing(true);
            engine.getSystem(BulletSystem.class).setProcessing(true);
        }
    }

    protected void renderWorld(float delta) {
        engine.update(delta);
        if (debug) {
            debugDrawer.begin(renderSystem.perspectiveCamera);
            bulletSystem.collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }
    }

    public void resize(int width, int height) {
        renderSystem.resize(width, height);
    }

    public void dispose() {
        bulletSystem.collisionWorld.removeAction(character.getComponent(CharacterComponent.class).characterController);
        bulletSystem.collisionWorld.removeCollisionObject(character.getComponent(CharacterComponent.class).ghostObject);
        bulletSystem.dispose();

        bulletSystem = null;
        renderSystem.dispose();

        character.getComponent(CharacterComponent.class).characterController.dispose();
        character.getComponent(CharacterComponent.class).ghostObject.dispose();
        character.getComponent(CharacterComponent.class).ghostShape.dispose();
//        EntityFactory.dispose();
    }

    public void remove(Entity entity) {
        engine.removeEntity(entity);
        bulletSystem.removeBody(entity);
    }
}