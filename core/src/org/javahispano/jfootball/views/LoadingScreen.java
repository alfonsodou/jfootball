/**
 * 
 */
package org.javahispano.jfootball.views;

import org.javahispano.jfootball.Jfootball;

import com.badlogic.gdx.Screen;

/**
 * @author alfonso
 *
 */
public class LoadingScreen implements Screen {
	private Jfootball parent;

	public LoadingScreen(Jfootball jfootball) {
		parent = jfootball;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		parent.changeScreen(Jfootball.MENU);
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
		// TODO Auto-generated method stub

	}

}
