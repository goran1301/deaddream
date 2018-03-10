package com.mygdx.dd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import deaddream.screens.GameScreen;
import deaddream.screens.LoadingScreen;
import deaddream.screens.MainMenuScreen;
import deaddream.screens.StartScreen;
//import deaddream.screens.StartScreen;

//import com.mygdx.game.screens.GameScreen;
//import com.mygdx.helpers.AssetLoader;

public class DeadDream extends Game {

	final public int V_WIDTH = 1366 * 2;
	final public int V_HEIGHT = 768 * 2;
	public OrthographicCamera camera;
	public AssetManager assets;
	public BitmapFont font;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public LoadingScreen loadingScreen;
	public StartScreen startScreen;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	
	@Override
	public void create() {
		this.assets = new AssetManager();
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.font.getData().setScale(5);
		
		this.shapeRenderer = new ShapeRenderer();
		//AssetLoader.load();
		Gdx.app.log("TheGame", "created");
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.V_WIDTH, this.V_HEIGHT);
		
		
		this.loadingScreen = new LoadingScreen(this);
		this.startScreen = new StartScreen(this);
		this.mainMenuScreen = new MainMenuScreen(this);
		this.gameScreen = new GameScreen(this);
		
		this.setScreen(loadingScreen);
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
