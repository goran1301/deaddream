package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.dd.DeadDream;

public class LoadingScreen implements Screen {

	public ShapeRenderer shapeRenderer;
	public DeadDream game;
	//private AssetManager assets;
	private float progress;
	//private Class<T> nextScreenClass;
	
	public LoadingScreen(final DeadDream game) {
		shapeRenderer = new ShapeRenderer();
		//this.assets = game.assets;
		this.game = game;
		//this.nextScreenClass = nextScreenClass;
	}
	
	private void queueAssets() {
		game.assets.load("12april.png", Texture.class);
		game.assets.load("backgrounds/bg1.jpg", Texture.class);
		game.assets.load("skins/units/protector.png", Texture.class);
		game.assets.load("skins/units/stone.png", Texture.class);
	}
	
	@Override
	public void show() {
		System.out.println("Loading");
		this.progress = 0f;
		queueAssets();
		//this.progress = game.assets.getProgress();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		this.progress = this.game.assets.getProgress();
		
		Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		this.shapeRenderer.setColor(Color.BLACK);
		this.shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, game.camera.viewportWidth - 154, 16);
		
		this.shapeRenderer.setColor(Color.BLUE);
		this.shapeRenderer.rect(32, game.camera.viewportHeight / 2 - 8, progress * (game.camera.viewportWidth - 154), 16);
		//System.out.println(String.valueOf("Progress:" + progress));
		this.shapeRenderer.end();
		// TODO Auto-generated method stub
		update(delta);
	}
	
	private void update(float delta) {
		//if assets loaded, change Screen
		if (game.assets.update()) {
			//game.setScreen(game.startScreen);
			game.setScreen(game.gameScreen);
		}
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
		shapeRenderer.dispose();
		// TODO Auto-generated method stub
		
	}

}
