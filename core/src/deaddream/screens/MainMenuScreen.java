package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	private TextButton buttonPlay, buttonExit, buttonHostGame, buttonJoinGame;
	private Image background;
	private Image Astra;
	private Image proection;
	private Color fontColor;
	
	public MainMenuScreen(final DeadDream game) {
		this.game = game;
		this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera));
		this.shapeRenderer = new ShapeRenderer();
		this.skin = new Skin();
		fontColor = Color.NAVY;
	}
	
	private void initButtons() {
		int offset = 400;
		buttonPlay = this.generateButton("Play demo", 10 + 200, game.V_HEIGHT - 80 - offset, 150*2, 80, fontColor);
		buttonHostGame = generateButton("Host game", 10 + 200, game.V_HEIGHT - 80 * 2 - 40 - offset, 150*2, 80, fontColor);
		buttonJoinGame = generateButton("Join game", 10 + 200, game.V_HEIGHT - 80 * 3 - 40 - offset, 150*2, 80, fontColor);
		buttonExit = generateButton("Exit", 10 + 200, game.V_HEIGHT - 80 * 4 - 40 - offset, 150*2, 80, fontColor);
		buttonPlay.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f)));
		buttonExit.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f)));
		buttonHostGame.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f)));
		buttonJoinGame.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f)));
		buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		buttonPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.gameScreen);
			}
		});
		buttonHostGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.hostGameScreen);
			}
		});
		buttonJoinGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.clientGameScreen);
			}
		});
		stage.addActor(buttonPlay);
		stage.addActor(buttonExit);
		stage.addActor(buttonHostGame);
		stage.addActor(buttonJoinGame);
	}
	
	private TextButton generateButton(String caption, int x, int y, int width, int height, Color color) {
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = game.font;
		buttonStyle.fontColor = color;
		TextButton button = new TextButton(caption, buttonStyle);
		button.setPosition(x, y);
		button.setSize(width, height);
		button.getLabel().setFontScale(4f);
		return button;
	}
	
	@Override
	public void show() {
		Texture bgTexture = game.assets.get("main_menu/background.png", Texture.class);
		Texture AstraTexture = game.assets.get("main_menu/Astra.png", Texture.class);
		Texture proectionTexture = game.assets.get("main_menu/proection.png", Texture.class);
		
		bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		AstraTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		proectionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Image(bgTexture);
		Astra = new Image(AstraTexture);
		proection = new Image(proectionTexture);
		
		stage.addActor(background);
		stage.addActor(proection);
		stage.addActor(Astra);
		
		background.toBack();
		float widthHeightDevition = game.camera.viewportWidth / game.camera.viewportHeight;
		background.setSize(game.camera.viewportWidth, game.camera.viewportWidth / widthHeightDevition);
		background.setPosition(0, 0);
		
		
		Astra.setSize(widthHeightDevition * game.camera.viewportHeight * 0.9f, game.camera.viewportHeight * 0.9f);
		Astra.setPosition(
				game.camera.viewportWidth - Astra.getWidth() - game.camera.viewportWidth * 0.05f,
				game.camera.viewportHeight * 0.08f
		);
		
		
		proection.setSize(widthHeightDevition * game.camera.viewportHeight * 0.9f, game.camera.viewportHeight * 0.9f);
		proection.setPosition(
				game.camera.viewportWidth - proection.getWidth() - Astra.getImageWidth() - game.camera.viewportWidth * 0.05f,
				game.camera.viewportHeight * 0.08f
		);
		proection.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(3.5f)));
		
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
		//this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera));
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
