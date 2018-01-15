package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dd.Constants;
import com.mygdx.dd.DeadDream;


public class GameScreen implements Screen {
	
	private DeadDream game;
	
	private World world;
	
	private Body testUnit, platform;
	
	private Box2DDebugRenderer b2ddr;
	
	private Matrix4 debugMatrix;
	
	private Texture background;
	
	private Texture testUnitSkin;
	
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		this.debugMatrix = this.game.camera.combined.cpy();//new Matrix4(this.game.camera.combined.cpy());
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		
		
		this.testUnit = this.createUnit(0, 0, 40, 60, false);
		this.platform = this.createUnit(200, 200, 40, 60, true);
	}
	
	private Body createUnit(int x, int y, int width, int height, boolean isStatic) {
		BodyDef def = new BodyDef();
		if (isStatic) {
			def.type = BodyType.StaticBody;
		} else {
			def.type = BodyType.DynamicBody;
		}
		
		def.position.set(x / Constants.PPM, y / Constants.PPM);
		//TODO we need rotating unit
		def.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM);
		
		Body unit = this.world.createBody(def);
		unit.createFixture(shape, 1.0f);
		shape.dispose();
		
		
		return unit;
	}
	

	@Override
	public void show() {
		System.out.println("Game");
		this.loadTextures();
		// TODO Auto-generated method stub
		
	}
	
	private void loadTextures() {
		this.background = game.assets.get("backgrounds/bg1.jpg", Texture.class);
		this.testUnitSkin = game.assets.get("skins/units/protector.png", Texture.class);
		//this.background = new Image(background);
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.game.batch.begin();
		this.game.batch.enableBlending();
		this.game.batch.draw(this.background, 0, 0,
                this.game.V_WIDTH, this.game.V_HEIGHT);
		this.game.batch.draw(this.testUnitSkin, this.testUnit.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2) , this.testUnit.getPosition().y * Constants.PPM - (this.testUnitSkin.getHeight() /2));
		this.game.batch.draw(this.testUnitSkin, this.platform.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2) , this.platform.getPosition().y * Constants.PPM - (this.testUnitSkin.getHeight() /2));
		game.font.setColor(Color.WHITE);
		game.font.draw(game.batch, "Skin X" + String.valueOf(this.testUnit.getPosition().x * Constants.PPM - (this.testUnitSkin.getWidth() /2)) + " Skin Y" + String.valueOf(testUnit.getPosition().y * Constants.PPM - (testUnitSkin.getHeight() /2) + " Body X" + String.valueOf(testUnit.getPosition().x)) + " Body Y" + String.valueOf(testUnit.getPosition().y), 0, 0);
		
		this.game.batch.end();
		
		
		// TODO Auto-generated method stub
		
		this.b2ddr.render(this.world, this.debugMatrix);
	}
	
	public void update(float delta) {
		world.step(1/60f, 6, 2);
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		this.setCameraToMeters(testUnit.getPosition().x, testUnit.getPosition().y);
		this.updateInput();
	}
	
	private void updateInput() {
		int horizontalForce = 0;
		int verticalForce = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 1;
		} 
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 1;
		}
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			verticalForce += 1;
		} 
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			verticalForce -= 1;
		}
        this.testUnit.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
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
		world.dispose();
		b2ddr.dispose();
		// TODO Auto-generated method stub
		
	}

}
