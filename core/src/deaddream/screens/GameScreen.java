package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dd.Constants;
import com.mygdx.dd.DeadDream;

import deaddream.units.Protector;
import deaddream.units.Stone;
import deaddream.units.Unit;


public class GameScreen implements Screen {
	
	private DeadDream game;
	
	private World world;
	
	private Unit unit00, unit01, stone;
	
	private Box2DDebugRenderer b2ddr;
	
	private Matrix4 debugMatrix;
	
	private Texture background;
	
	
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		this.debugMatrix = this.game.camera.combined.cpy();//new Matrix4(this.game.camera.combined.cpy());
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
	}	

	@Override
	public void show() {
		System.out.println("Game");
		this.loadTextures();
		this.unit00 = new Protector(this.world, game.assets.get("skins/units/protector.png", Texture.class), 0.0f, 0.0f, 0.0f);
		this.unit01 = new Protector(this.world, game.assets.get("skins/units/protector.png", Texture.class), 10f, 10f, 1f);
		this.stone = new Stone(this.world, game.assets.get("skins/units/stone.png", Texture.class), 15f, 15f, 1f);
	
	}
	
	private void loadTextures() {
		this.background = game.assets.get("backgrounds/bg1.jpg", Texture.class);
		//this.testUnitSkin = game.assets.get("skins/units/protector.png", Texture.class);
		//this.background = new Image(background);
	}

	@Override
	public void render(float delta) {
		update(delta);
		beginBatch();
		renderBackground();
		renderUnits();
		//this.game.batch.draw(this.testUnitSkin, this.testUnit.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2) , this.testUnit.getPosition().y * Constants.PPM - (this.testUnitSkin.getHeight() /2));
		//this.game.batch.draw(this.testUnitSkin, this.platform.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2) , this.platform.getPosition().y * Constants.PPM - (this.testUnitSkin.getHeight() /2));
		//game.font.setColor(Color.WHITE);
		//game.font.draw(game.batch, "Skin X" + String.valueOf(this.testUnit.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2)) + " Skin Y" + String.valueOf(testUnit.getPosition().y * Constants.PPM - (testUnitSkin.getHeight() /2) + " Body X" + String.valueOf(testUnit.getPosition().x)) + " Body Y" + String.valueOf(testUnit.getPosition().y), 0, 0);
		
		this.game.batch.end();
		
		
		
		// TODO Auto-generated method stub
		
		//this.b2ddr.render(this.world, this.debugMatrix);
	}
	
	private void renderUnits() {
		this.unit00.render(this.game.batch);
		this.unit01.render(this.game.batch);
		this.stone.render(this.game.batch);
	}
	
	private void beginBatch() {
		this.game.batch.begin();
		this.game.batch.enableBlending();
		
	}
	
	private void renderBackground() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batch.draw(this.background, 
				this.game.camera.position.x - this.game.V_WIDTH / 2, 
				this.game.camera.position.y - this.game.V_HEIGHT / 2,
                this.game.V_WIDTH, this.game.V_HEIGHT
                );
	}
	
	public void update(float delta) {
		world.step(1/60f, 6, 2);
		unit00.update(delta);
		unit01.update(delta);
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		this.setCameraToMeters(unit00.getBody().getPosition().x, unit00.getBody().getPosition().y);
		this.updateInput();
	}
	
	private void updateInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
		    Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		    this.unit00.moveTo(tmp.x / Constants.PPM, tmp.y / Constants.PPM);
		}
		this.unit00.move();
	}
	
	public void setCameraToMeters(float x, float y) {
		cameraUpdate(x * Constants.PPM, y * Constants.PPM);
	}
	
	public void cameraUpdate(float x, float y) {
		Vector3 position = this.game.camera.position;
		position.x = x;
		position.y = y;
		game.camera.position.set(position);
		
		game.camera.update();
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
		this.background.dispose();
		this.unit00.dispose();
		this.unit01.dispose();
		world.dispose();
		b2ddr.dispose();
		// TODO Auto-generated method stub
		
	}

}
