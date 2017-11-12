/**
 * 
 */
package org.javahispano.jfootball.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author alfonso
 *
 */
public class MyAssetManager {
	public final AssetManager manager = new AssetManager();

	// Textures
	public final String playerImage = "images/player.png";
	public final String enemyImage = "images/enemy.png";

	// Sounds
	public final String boingSound = "sounds/boing.wav";
	public final String pingSound = "sounds/ping.wav";

	// Music
	public final String playingSong = "music/Rolemusic_-_pl4y1ng.mp3";

	// Skin
	public final String skin = "skin/glassy-ui.json";

	// Ball
	public final String ball = "models/soccerball_1_reducido.g3dj";

	// Y-Bot
	public final String yBot = "models/ybot_reducido_2.g3dj";

	// Z-Bot
	public final String zBot = "models/Soccer Penalty Kick_reducido.g3dj";

	// Soccer
	public final String soccer = "models/soccer.g3dj";

	public void queueAddSkin() {
		SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
		manager.load(skin, Skin.class, params);

	}

	public void queueAddMusic() {
		manager.load(playingSong, Music.class);
	}

	public void queueAddSounds() {
		manager.load(boingSound, Sound.class);
		manager.load(pingSound, Sound.class);
	}

	public void queueAddImages() {
		manager.load(playerImage, Texture.class);
		manager.load(enemyImage, Texture.class);
	}

	public void queueAddSoccer() {
		manager.load(soccer, Model.class);
	}
	
	public void queueAddBall() {
		manager.load(ball, Model.class);
	}
	
	public void queueAddYBot() {
		manager.load(yBot, Model.class);
	}
	
	public void queueAddZBot() {
		manager.load(zBot, Model.class);
	}

	// a small set of images used by the loading screen
	public void queueAddLoadingImages() {

	}

	/**
	 * @return the manager
	 */
	public AssetManager getManager() {
		return manager;
	}

	/**
	 * @return the playerImage
	 */
	public String getPlayerImage() {
		return playerImage;
	}

	/**
	 * @return the enemyImage
	 */
	public String getEnemyImage() {
		return enemyImage;
	}

	/**
	 * @return the boingSound
	 */
	public String getBoingSound() {
		return boingSound;
	}

	/**
	 * @return the pingSound
	 */
	public String getPingSound() {
		return pingSound;
	}

	/**
	 * @return the playingSong
	 */
	public Music getPlayingSong() {
		return this.manager.get(playingSong);
	}

	/**
	 * @return the skin
	 */
	public String getSkin() {
		return skin;
	}
	
	public String getSoccer() {
		return soccer;
	}
	
	public String getBall() {
		return ball;
	}
	
	public String getZBot() {
		return zBot;
	}
}
