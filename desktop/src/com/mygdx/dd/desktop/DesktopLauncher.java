package com.mygdx.dd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.dd.DeadDream;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.title = "Dead Dream";

		config.width = 1920;
		config.height = 1080;
		//config.width = 1280;
		//config.height = 720;
		config.fullscreen = false;
		new LwjglApplication(new DeadDream(), config);
	}
}
