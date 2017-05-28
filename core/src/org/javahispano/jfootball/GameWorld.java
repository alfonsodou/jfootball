/**
 * 
 */
package org.javahispano.jfootball;

import org.javahispano.jfootball.managers.EntityFactory;
import org.javahispano.jfootball.systems.PlayerSystem;
import org.javahispano.jfootball.systems.RenderSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

/**
 * @author adou
 *
 */
public class GameWorld {
    private Engine engine;
    private Entity character, dome;
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
        character = EntityFactory.createPlayer(x, y, z);
        engine.addEntity(character);
    }

    public void render(float delta) {
        renderWorld(delta);
        checkPause();
    }

    private void checkPause() {
        if (Settings.Paused) {
            engine.getSystem(PlayerSystem.class).setProcessing(false);
        } else {
            engine.getSystem(PlayerSystem.class).setProcessing(true);
        }
    }

    protected void renderWorld(float delta) {
        engine.update(delta);
    }

    public void resize(int width, int height) {
        renderSystem.resize(width, height);
    }

    public void dispose() {
        renderSystem.dispose();
    }

    public void remove(Entity entity) {
        engine.removeEntity(entity);
    }
}