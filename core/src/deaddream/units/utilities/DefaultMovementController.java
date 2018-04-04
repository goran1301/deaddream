package deaddream.units.utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;


/**
 * Movement controller abstract class
 * 
 * @author goran
 *
 */
public class DefaultMovementController implements MovementControllerInterface <Array<Vector2>> {

	protected Body body;
	
	protected Vector2 currentMoveGoal;
	
	protected Vector2 currentMoveVector;

	protected boolean isMoving = false;
	
	protected float goalPointFaultRange;
	
	protected float linearVelocity;

	protected float angleGoal;

	protected float angularVelocity;

	protected boolean isRotating = false;

	protected float goalAngleFaultRange;
	
	protected float bodyAngle;
	
	protected int angularDirection;
	
	protected Array<Vector2> path;
	
	protected int currentPathIndex = 0;
	
	public DefaultMovementController(Body body, float linearVelocity, float goalPointFaultRange, float angularVelocity, float goalAngleFaultRange) {
		this.body = body;
		this.linearVelocity = linearVelocity;
		this.goalPointFaultRange = goalPointFaultRange;
		this.angularVelocity = angularVelocity;
		this.goalAngleFaultRange = goalAngleFaultRange;
		this.currentMoveGoal = new Vector2();
		this.currentMoveVector = new Vector2();
	}

	
	//@Override
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
		isMoving = true;
	}
	
	public void keepMove(float x, float y) {
		this.currentMoveGoal.x = x;
		this.currentMoveGoal.y = y;
		float rangeX = x - body.getPosition().x;
		float rangeY = y - body.getPosition().y;
		float maxXY= Math.max(Math.abs( rangeX ), Math.abs( rangeY ));
		body.setLinearVelocity(rangeX / maxXY * linearVelocity, rangeY / maxXY * linearVelocity);
		
		this.currentMoveVector.x = this.currentMoveGoal.x - this.body.getPosition().x;
		this.currentMoveVector.y = this.currentMoveGoal.y - this.body.getPosition().y;
		
		this.rotateTo(this.currentMoveVector.angle());
		isMoving = true;
	}
	
	
	@Override
	public void moveTo(Array<Vector2> path) {
		if (path != null) {
			if (path.size > 0) {
				this.path = path;
				currentPathIndex = 0;
				currentMoveGoal = path.get(0);
				keepMove(currentMoveGoal.x, currentMoveGoal.y);
			}
		}
		
	}

	@Override
	public void stopMove() {
		body.setLinearVelocity(0f, 0f);
		currentPathIndex = 0;
		path = null;
		isMoving = false;
	}

	//@Override
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
		if (isRotating){
			updateBodyAngle();
			if (stopRotateCondition()) {
				stopRotation();
				return;
			}else{
				body.setAngularVelocity(angularDirection * MathUtils.degreesToRadians * this.angularVelocity);
			}
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

	//@Override
	public void stopRotation() {
		body.setAngularVelocity(0.0f);
		isRotating = false;
	}


	public void update(float delta) {
		if (isMoving){
			this.currentMoveVector.x = this.currentMoveGoal.x - body.getPosition().x;
			this.currentMoveVector.y = this.currentMoveGoal.y - body.getPosition().y;
			this.updateLinearMove();
			
		}
		this.updateRotatingMove();
	}
	
    protected void updateLinearMove() {
		if (isMoving) {
			updateCurrentPathGoal();
			if(stopMoveCondition()) {
				stopMove();
				return;
			}
			keepMove(currentMoveGoal.x, currentMoveGoal.y);
		}
		return;
	}
    
    public void updateCurrentPathGoal() {
    	if (stopMoveCondition() && path != null) {
    		if (path.size > currentPathIndex + 1) {
    			currentMoveGoal = path.get(currentPathIndex + 1);
    			currentPathIndex++;
    			keepMove(currentMoveGoal.x, currentMoveGoal.y);
    		}
    	}
    }
	
	
	public boolean stopMoveCondition() {
		return currentMoveVector.len2() <= goalPointFaultRange;
	}

	public boolean stopRotateCondition() {
		return isRotating && Math.abs(bodyAngle - angleGoal) <= goalAngleFaultRange;
	}
	
	
	
	public boolean isMoving() {
		return isMoving;
	}
	
}
