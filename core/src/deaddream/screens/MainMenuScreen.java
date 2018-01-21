package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
//import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.dd.DeadDream;

public class MainMenuScreen implements Screen{

	private DeadDream game;
	
	private Stage stage;
	private ShapeRenderer shapeRenderer;
	private Skin skin;
	private TextButton buttonPlay, buttonExit;
	
	public MainMenuScreen(final DeadDream game) {
		this.game = game;
		this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera));
		this.shapeRenderer = new ShapeRenderer();
		this.skin = new Skin();
	}
	
	private void initButtons() {
		this.buttonPlay = this.generateButton("Play", 10, 50, 150, 20, Color.BLACK);
		this.buttonExit = this.generateButton("Exit", 10, 20, 150, 20, Color.BLACK);
		this.buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		this.buttonPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.gameScreen);
			}
		});
		this.stage.addActor(this.buttonPlay);
		this.stage.addActor(this.buttonExit);
	}
	
	private TextButton generateButton(String caption, int x, int y, int width, int height, Color color) {
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = this.game.font;
		buttonStyle.fontColor = color;
		TextButton button = new TextButton(caption, buttonStyle);
		button.setPosition(x, y);
		button.setSize(width, height);
		return button;
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this.stage);
		System.out.println("Main menu");
		this.initButtons();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.update(delta);
		this.stage.draw();
		
	}
	
	public void update(float delta) {
		this.stage.act(delta);
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
		this.stage.dispose();
		this.skin.dispose();
		this.shapeRenderer.dispose();
	}
	
}
