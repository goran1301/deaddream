package deaddream.units;

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
	
	public Unit(World world, Texture staticTexture, float x, float y, float angle) {
		this.currentMoveGoal = new Vector2();
		this.currentMoveVector = new Vector2();
		this.isMoving = false;
		this.staticTexture = new Sprite(staticTexture);
		this.createUnit(world, x, y, angle);
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
	private Body createUnit(World world, float x, float y, float angle) {
		BodyDef def = bodyDefFactory();
		body = world.createBody(def);
		FixtureDef fixtureDef = fixtureDefFactory();
		
		def.position.set(x, y);
		def.angle = angle;
		
		if (fixtureDef != null) {
			body.createFixture(fixtureDef);
			fixtureDef.shape.dispose();
		}
		
		
		return body;
	}
	
	/**
	 * Construct BodyDef for each unit 
	 * @return
	 */
	protected abstract BodyDef bodyDefFactory();
	
	/**
	 * Construct Fixture for each unit
	 * @return
	 */
	protected abstract FixtureDef fixtureDefFactory();
	
	
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
	private float standartAngle(float angle){
		angle = angle%360;
		if (angle<0){
			angle = angle+360;
		} 
		return angle;
	}
	protected void updateRotatingMove() {
		float bodyAngle = this.standartAngle(this.body.getAngle()* Math.abs(MathUtils.radiansToDegrees )+90);
		if (this.rotating && Math.abs(bodyAngle - this.angleGoal)<= this.goalAngleFaultRange) {
			this.body.setAngularVelocity(0.0f);
			//System.out.println("Stop rotating. Current angle: " + String.valueOf(bodyAngle) + "; goal: " + String.valueOf(angleGoal));
			//System.out.println("difference: " + String.valueOf(bodyAngle - this.angleGoal));
			this.rotating = false;
			return;
		}
		if (this.rotating){
			//System.out.println("Do rotation. Current angle: " + String.valueOf(bodyAngle) + "; goal: " + String.valueOf(angleGoal));
			this.rotateTo(this.angleGoal);
		} 
		return;
	}
	
	
	protected void rotateTo(float angle) {
		
		this.angleGoal = angle;
		System.out.println("rotate velocity: " + String.valueOf(MathUtils.degreesToRadians * this.angularVelocity));
		float bodyAngle = this.standartAngle(this.body.getAngle()* Math.abs(MathUtils.radiansToDegrees )+90);
		if((this.angleGoal - bodyAngle<0)){
			if((Math.abs(this.angleGoal - bodyAngle)<180))body.setAngularVelocity(-MathUtils.degreesToRadians * this.angularVelocity); else body.setAngularVelocity(MathUtils.degreesToRadians * this.angularVelocity);
		}else{
			if((Math.abs(this.angleGoal - bodyAngle)<180))body.setAngularVelocity(MathUtils.degreesToRadians * this.angularVelocity); else body.setAngularVelocity(-MathUtils.degreesToRadians * this.angularVelocity);
			
		}
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
