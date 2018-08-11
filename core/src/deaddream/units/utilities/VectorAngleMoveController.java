package deaddream.units.utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class VectorAngleMoveController implements MovementControllerInterface <Float> {
protected Body body;
	
	protected float angleGoal;

	protected float angularVelocity;

	protected boolean isRotating = false;

	protected float goalAngleFaultRange;
	
	protected float bodyAngle;
	
	protected int angularDirection;
	
	protected int currentPathIndex = 0;
	
	protected float calculatedAngle = 0;
	
	public VectorAngleMoveController(Body body, float angularVelocity, float goalAngleFaultRange) {
		this.body = body;
		this.angularVelocity = angularVelocity;
		this.goalAngleFaultRange = goalAngleFaultRange;
	}

	
	
	public void keepMove(float angle) {
		this.moveTo(angle);
	}
	
	@Override
	public void moveTo(Float path) {
		angleGoal = path;
		updateBodyAngle();
		if((this.angleGoal - bodyAngle < 0)){
			if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = -1; else angularDirection = 1;
		}else{
			if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = 1; else angularDirection = -1;
		}
		
		//body.setAngularVelocity(angularDirection * MathUtils.degreesToRadians * this.angularVelocity);
		isRotating = true;
	}

	@Override
	public void stopMove() {
		//body.setAngularVelocity(0.0f);
		//calculatedAngle = 0;
		isRotating = false;
	}
	
	private float standartAngle(float angle){
		angle = angle%360;
		if (angle<0){
			angle = angle+360;
		} 
		return angle;
	}
	
	private void updateBodyAngle() {
		bodyAngle = standartAngle(body.getAngle() * Math.abs(MathUtils.radiansToDegrees )+90); 
		//bodyAngle = body.getAngle();
	}

	public void update(float delta) {
		if (isRotating){
			updateBodyAngle();
			if (stopRotateCondition()) {
				stopMove();
				return;
			}else{
				//body.setAngularVelocity(angularDirection * MathUtils.degreesToRadians * this.angularVelocity);
				if((this.angleGoal - bodyAngle < 0)){
					if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = -1; else angularDirection = 1;
				}else{
					if((Math.abs(this.angleGoal - bodyAngle)<180)) angularDirection = 1; else angularDirection = -1;
				}
				calculatedAngle = body.getAngle() + angularDirection * MathUtils.degreesToRadians * angularVelocity * delta;
			    //System.out.println(calculatedAngle);
			}
		} 
		return;
	}
	
	public float getAngle() {
		return calculatedAngle;
	}

	public boolean stopRotateCondition() {
		return isRotating && Math.abs(bodyAngle - angleGoal) <= goalAngleFaultRange;
	}
	
	public float getGoal() {
		return angleGoal;
	}
}
