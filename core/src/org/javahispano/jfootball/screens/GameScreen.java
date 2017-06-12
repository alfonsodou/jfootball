package org.javahispano.jfootball.screens;

import org.javahispano.jfootball.Core;
import org.javahispano.jfootball.GameWorld;
import org.javahispano.jfootball.Settings;
import org.javahispano.jfootball.UI.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
	Core game;
	GameWorld gameWorld;
	GameUI gameUI;

	public GameScreen(Core game) {
		this.game = game;
		gameUI = new GameUI(game);
		gameWorld = new GameWorld(gameUI);
		Settings.Paused = false;
		Gdx.input.setInputProcessor(gameUI.stage);
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render(float delta) {
		/** Updates */
		gameUI.update(delta);
		gameWorld.render(delta);
		gameUI.render();
	}

	@Override
	public void resize(int width, int height) {
		gameUI.resize(width, height);
		gameWorld.resize(width, height);
	}

	@Override
	public void dispose() {
		gameUI.dispose();
		gameWorld.dispose();
	}

	@Override
	public void show() {
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
}