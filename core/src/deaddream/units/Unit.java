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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dd.Constants;

/**
 * 
 * 
 * @author goran
 *
 */
public abstract class Unit {
	protected Sprite staticTexture;
	protected Body body;
	protected float velocity = 0.0f;
	protected Vector2 currentMoveGoal;
	protected float angleGoal = 0.0f;
	protected float goalPointFaultRange = 0.0f;
	protected float goalAngleFaultRange = 0.0f;
	protected boolean isMoving;
	protected Vector2 currentMoveVector;
	protected float angularVelocity = 0.0f;
	private boolean rotating = false;
	
	public Unit(World world, Texture staticTexture, float x, float y, float angle, BodyDef def, FixtureDef fixtureDef) {
		this.currentMoveGoal = new Vector2();
		this.currentMoveVector = new Vector2();
		this.isMoving = false;
		this.staticTexture = new Sprite(staticTexture);
		this.body = this.createUnit(world, x, y, angle, def, fixtureDef);
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
	private Body createUnit(World world, float x, float y, float angle, BodyDef def, FixtureDef fixtureDef) {
		
		def.position.set(x, y);
		def.angle = angle;
		
		Body body = world.createBody(def);
		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
		
		
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
		
		this.currentMoveVector.x = this.currentMoveGoal.x - this.body.getPosition().x;
		this.currentMoveVector.y = this.currentMoveGoal.y - this.body.getPosition().y;
		
		this.rotateTo(this.currentMoveVector.angle());
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
		
		this.updateLinearMove();
		this.updateRotatingMove();
	}
	
	protected void updateRotatingMove() {
		if (this.rotating && this.body.getAngle() * Math.abs(MathUtils.radiansToDegrees - this.angleGoal) <= this.goalAngleFaultRange) {
			this.body.setAngularVelocity(0.0f);
			System.out.println("Stop rotating. Current angle: " + String.valueOf(this.body.getAngle() * MathUtils.radiansToDegrees) + "; goal: " + String.valueOf(angleGoal));
			System.out.println("difference: " + String.valueOf(this.body.getAngle() * MathUtils.radiansToDegrees - this.angleGoal));
			this.rotating = false;
			return;
		}
		if (this.rotating){
			System.out.println("Do rotation. Current angle: " + String.valueOf(Math.abs(this.body.getAngle() * MathUtils.radiansToDegrees)) + "; goal: " + String.valueOf(angleGoal));
			this.rotateTo(this.angleGoal);
		} 
		return;
	}
	
	
	protected void rotateTo(float angle) {
		this.angleGoal = angle;
		System.out.println("rotate velocity: " + String.valueOf(MathUtils.degreesToRadians * this.angularVelocity));
		body.setAngularVelocity(MathUtils.degreesToRadians * this.angularVelocity);
		this.rotating = true;
	}
	
    protected void updateLinearMove() {
		
		if(this.isMoving && this.currentMoveVector.len2() <= this.goalPointFaultRange) {
			this.stopMove();
			return;
		}
		
		if (this.isMoving) {
			this.moveTo(this.currentMoveGoal.x, this.currentMoveGoal.y);
		}
		return;
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
