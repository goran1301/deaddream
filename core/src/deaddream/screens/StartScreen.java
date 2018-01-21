package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import deaddream.renderers.MenuRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dd.DeadDream;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class StartScreen implements Screen {
	
	private MenuRenderer renderer;
	private Stage stage;
	private DeadDream game;
	private Image logo;
	
	public StartScreen(final DeadDream game) {
		this.game = game;
		this.renderer = new MenuRenderer();
		//this.camera = game.camera;
		//this.font = game.font;
		//this.stage = new Stage(new FitViewport(width, height, camera));
		//this.stage = new Stage(new FillViewport(width, height, camera));
		this.stage = new Stage(new StretchViewport(game.V_WIDTH, game.V_HEIGHT, game.camera));
		
	}

	@Override
	public void show() {
		System.out.println("Start");
        Gdx.input.setInputProcessor(this.stage);
        
        Runnable transitionRunnable = new Runnable() {
        	@Override
        	public void run() {
        		game.setScreen(game.mainMenuScreen);
        	}
        };
		
		//Texture logoTxt = new Texture(Gdx.files.internal("12april.png"));
		Texture logoTxt = game.assets.get("12april.png", Texture.class);
		this.logo = new Image(logoTxt);
		
		stage.addActor(this.logo);
		
		this.logo.setPosition(stage.getWidth()/2 - 100, stage.getHeight()/2 - 100);
		this.logo.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f), Actions.run(transitionRunnable)/*, Actions.rotateBy(360, 3.5f)*/));
		//this.logo.addAction(Actions.fadeIn(3.5f));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		stage.draw();
		game.batch.begin();
		game.font.setColor(0, 0, 0, 1);
		game.font.draw(game.batch, "presents", 290, 100);
		game.batch.end();
		//this.renderer.render();
	}
	
	public void update(float delta) {
		stage.act(delta);
		//this.logo.
	}

	@Override
	public void resize(int width, int height) {
		if (width < 100) {
			width = 100;
		}
		
		if (height < 100) {
			height = 100;
		}
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, false);
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
		this.stage.dispose();
		// TODO Auto-generated method stub
		//game.assets.dispose();
		
	}
	
}
