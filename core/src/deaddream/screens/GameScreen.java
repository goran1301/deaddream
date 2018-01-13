package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
	
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		
		this.testUnit = this.createUnit(8, 10, 32, 32, false);
		this.platform = this.createUnit(0, 0, 32, 64, true);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// TODO Auto-generated method stub
		
		this.b2ddr.render(this.world, this.game.camera.combined.scl(Constants.PPM));
	}
	
	public void update(float delta) {
		world.step(1/60f, 6, 2);
		setCameraToMeters(testUnit.getPosition().x, testUnit.getPosition().y);
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
