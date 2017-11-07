package org.javahispano.jfootball.desktop;

import org.javahispano.jfootball.Jfootball;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static final float VIRTUAL_WIDTH = 1024;
	public static final float VIRTUAL_HEIGHT = 768;
	
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) VIRTUAL_WIDTH;
		config.height = (int) VIRTUAL_HEIGHT;
		new LwjglApplication(new Jfootball(), config);
	}
}
