package deaddream.units.utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Movement controller abstract class
 * 
 * @author goran
 *
 */
public class DefaultMovementController implements MovementControllerInterface {

	protected Body body;
	
	protected Vector2 currentMoveGoal;
	
	protected Vector2 currentMoveVector;

	protected boolean isMoving;
	
	protected float goalPointFaultRange;
	
	protected float linearVelocity;

	protected float angleGoal;

	protected float angularVelocity;

	protected boolean isRotating;

	protected float goalAngleFaultRange;
	
	protected float bodyAngle;
	
	protected int angularDirection;

	
	@Override
	public void moveTo(float x, float y) {
		this.currentMoveGoal.x = x;
		this.currentMoveGoal.y = y;
		float rangeX = x - body.getPosition().x;
		float rangeY = y - body.getPosition().y;
		float maxXY= Math.max(Math.abs( rangeX ), Math.abs( rangeY ));
		body.setLinearVelocity(rangeX / maxXY * linearVelocity, rangeY / maxXY * linearVelocity);
		
		this.currentMoveVector.x = this.currentMoveGoal.x - this.body.getPosition().x;
		this.currentMoveVector.y = this.currentMoveGoal.y - this.body.getPosition().y;
		
		this.rotateTo(this.currentMoveVector.angle());
		this.isMoving = true;
	}

	@Override
	public void stopMove() {
		body.setLinearVelocity(0f, 0f);
		this.isMoving = false;
	}

	@Override
	public void rotateTo(float angle) {
		angleGoal = angle;
		updateBodyAngle();
		if((this.angleGoal - bodyAngle < 0)){
			if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = -1; else angularDirection = 1;
		}else{
			if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = 1; else angularDirection = -1;
		}
		body.setAngularVelocity(angularDirection * MathUtils.degreesToRadians * this.angularVelocity);
		isRotating = true;
	}
	
	protected void updateRotatingMove() {
		if (stopRotateCondition()) {
			stopRotation();
			return;
		}
		if (isRotating){
			updateBodyAngle();
			body.setAngularVelocity(angularDirection * MathUtils.degreesToRadians * this.angularVelocity);
		} 
		return;
	}
	
	
	
	private float standartAngle(float angle){
		angle = angle%360;
		if (angle<0){
			angle = angle+360;
		} 
		return angle;
	}
	
	private void updateBodyAngle() {
		bodyAngle = standartAngle(body.getAngle()* Math.abs(MathUtils.radiansToDegrees )+90); 
	}

	@Override
	public void stopRotation() {
		body.setAngularVelocity(0.0f);
		isRotating = false;
	}


	public void update(float delta) {
		this.currentMoveVector.x = this.currentMoveGoal.x - body.getPosition().x;
		this.currentMoveVector.y = this.currentMoveGoal.y - body.getPosition().y;
		
		this.updateLinearMove();
		this.updateRotatingMove();
		
	}
	
    protected void updateLinearMove() {
    	
		if(stopMoveCondition()) {
			stopMove();
			return;
		}
		
		if (this.isMoving) {
			moveTo(this.currentMoveGoal.x, this.currentMoveGoal.y);
		}
		return;
	}
    
	
	
	public boolean stopMoveCondition() {
		return this.isMoving && this.currentMoveVector.len2() <= this.goalPointFaultRange;
	}

	public boolean stopRotateCondition() {
		return isRotating && Math.abs(bodyAngle - angleGoal) <= goalAngleFaultRange;
	}
	
	
	
	public boolean isMoving() {
		return isMoving;
	}
	
}
