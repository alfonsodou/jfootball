/**
 * 
 */
package org.javahispano.jfootball;

import org.javahispano.jfootball.loader.MyAssetManager;
import org.javahispano.jfootball.views.EndScreen;
import org.javahispano.jfootball.views.LoadingScreen;
import org.javahispano.jfootball.views.MainScreen;
import org.javahispano.jfootball.views.MenuScreen;
import org.javahispano.jfootball.views.PreferencesScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;

/**
 * @author alfonso
 *
 */
public class Jfootball extends Game {
	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private EndScreen endScreen;
	private AppPreferences preferences;
	public MyAssetManager myAssetManager = new MyAssetManager();
	private Music playingSong;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;

	@Override
	public void create() {
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		setScreen(loadingScreen);

		// tells our asset manger that we want to load the images set in
		// loadImages method
		myAssetManager.queueAddMusic();
		// tells the asset manager to load the images and wait until finished
		// loading.
		myAssetManager.getManager().finishLoading();
		// loads the 2 sounds we use
		playingSong = myAssetManager.getPlayingSong();

		playingSong.play();

	}

	public void changeScreen(int screen) {
		switch (screen) {
		case MENU:
			if (menuScreen == null)
				menuScreen = new MenuScreen(this);
			this.setScreen(menuScreen);
			break;
		case PREFERENCES:
			if (preferencesScreen == null)
				preferencesScreen = new PreferencesScreen(this);
			this.setScreen(preferencesScreen);
			break;
		case APPLICATION:
			if (mainScreen == null)
				mainScreen = new MainScreen(this);
			this.setScreen(mainScreen);
			break;
		case ENDGAME:
			if (endScreen == null)
				endScreen = new EndScreen(this);
			this.setScreen(endScreen);
			break;
		}
	}

	public AppPreferences getPreferences() {
		return this.preferences;
	}

	@Override
	public void dispose() {
		playingSong.dispose();
		myAssetManager.getManager().dispose();
	}
	
	public MyAssetManager getMyAssetManager() {
		return myAssetManager;
	}
}
