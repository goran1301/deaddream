package com.mygdx.dd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import deaddream.screens.GameScreen;
import deaddream.screens.LoadingScreen;
import deaddream.screens.MainMenuScreen;
import deaddream.screens.StartScreen;
//import deaddream.screens.StartScreen;

//import com.mygdx.game.screens.GameScreen;
//import com.mygdx.helpers.AssetLoader;

public class DeadDream extends Game {

	final public int V_WIDTH = 720;
	final public int V_HEIGHT = 480;
	public OrthographicCamera camera;
	public AssetManager assets;
	public BitmapFont font;
	public SpriteBatch batch;
	
	public LoadingScreen loadingScreen;
	public StartScreen startScreen;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	
	@Override
	public void create() {
		this.assets = new AssetManager();
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		//AssetLoader.load();
		Gdx.app.log("TheGame", "created");
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.V_WIDTH, this.V_HEIGHT);
		this.setScreen(new LoadingScreen(this));
		
		this.loadingScreen = new LoadingScreen(this);
		this.startScreen = new StartScreen(this);
		this.mainMenuScreen = new MainMenuScreen(this);
		this.gameScreen = new GameScreen(this);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		assets.dispose();
		loadingScreen.dispose();
		startScreen.dispose();
		mainMenuScreen.dispose();
		gameScreen.dispose();
		//this.getScreen().dispose();
	}
	
	
}
