package deaddream.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dd.Constants;

/**
 * 
 * 
 * @author goran
 *
 */
public class Unit {
	protected Sprite staticTexture;
	protected Body body;
	protected final int width = 40;
	protected final int height = 60;
	protected final float velocity = 5f;
	protected Vector2 currentMoveGoal;
	protected final float goalPointFaultRange = 0.5f;
	protected boolean isMoving;
	protected Vector2 currentMoveVector;
	
	public Unit(World world, Texture staticTexture, Vector2 position, float angle) {
		constructor(world, staticTexture, position.x, position.y, angle);
	}
	
	public Unit(World world, Texture staticTexture, float x, float y, float angle) {
		constructor(world, staticTexture, x, y, angle);
	}
	
	private void constructor(World world, Texture staticTexture, float x, float y, float angle) {
		this.currentMoveGoal = new Vector2();
		this.currentMoveVector = new Vector2();
		this.isMoving = false;
		this.staticTexture = new Sprite(staticTexture);
		this.body = this.createUnit(world, x, y, angle);
	}
	
	/**
	 * Box2D body creation
	 * 
	 * @param world
	 * @param x meters
	 * @param y meters
	 * @param angle
	 * @return
	 */
	protected Body createUnit(World world, float x, float y, float angle) {
		
		BodyDef def = new BodyDef();
		def.position.set(x, y);
		def.angle = angle;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(this.width / 2 / Constants.PPM, this.height / 2 / Constants.PPM);
		def.type = BodyType.DynamicBody;
		Body body = world.createBody(def);
		body.createFixture(shape, 1.0f);
		shape.dispose();
		
		
		return body;
	}
	
	/**
	 * render unit on screen
	 * 
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		this.staticTexture.setPosition(this.body.getPosition().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getPosition().y * Constants.PPM - (this.staticTexture.getHeight() /2));
		this.staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		this.staticTexture.draw(batch);
	}
	

	public void move() {
		if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
        	this.moveTo(5f, 5f);
        	return;
        }
		
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
        
        this.body.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
	}
	
	/**
	 * Set new point to move
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(float x, float y) {
		this.currentMoveGoal.x = x;
		this.currentMoveGoal.y = y;
		float rangeX = x - body.getPosition().x;
		float rangeY = y - body.getPosition().y;
		float maxXY= Math.max(Math.abs( rangeX ), Math.abs( rangeY ));
		body.setLinearVelocity(rangeX / maxXY * velocity, rangeY / maxXY * velocity);
		
		//float h = (float) Math.sqrt( rangeX * rangeX +  rangeY * rangeY );
		this.isMoving = true;
		//body.setLinearVelocity((x - body.getPosition().x) * velocity, (y - body.getPosition().y)* velocity);
	}
	
	/**
	 * logic for current delta
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		
		this.moveUpdate(delta);
	}
	
	/**
	 * Move logic
	 * @param delta
	 */
	protected void moveUpdate(float delta) {
		this.currentMoveVector.x = this.currentMoveGoal.x - this.body.getPosition().x;
		this.currentMoveVector.y = this.currentMoveGoal.y - this.body.getPosition().y;
		if(this.isMoving && this.currentMoveVector.len2() <= this.goalPointFaultRange) {
			System.out.println("Stopped body position: " + String.valueOf(body.getPosition().x) + " : " + String.valueOf(body.getPosition().y));
			System.out.println("Stopped goal position: " + String.valueOf(this.currentMoveGoal.x) + " : " + String.valueOf(this.currentMoveGoal.y));
			this.stopMove();
		}
	}
	
	public void stopMove() {
		body.setLinearVelocity(0f, 0f);
		this.isMoving = false;
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public void dispose() {
		
	}
	
}
