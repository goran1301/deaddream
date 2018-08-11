package deaddream.units.utilities.move;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import deaddream.units.utilities.DefaultAngleMoveController;
import deaddream.units.utilities.DefaultLinearMoveController;
import deaddream.units.utilities.LogicMovementControllerInterface;

public class UCMothershipMoveController  implements LogicMovementControllerInterface <Array<Vector2>, Vector2> {

	protected Body body;
	
	protected Vector2 currentMoveVector;
	
	DefaultLinearMoveController linearMoveController;
	
	DefaultAngleMoveController angleMoveController;
	
	public UCMothershipMoveController(Body body, float linearVelocity, float goalPointFaultRange, float angularVelocity, float goalAngleFaultRange, float length) {
		this.body = body;
		currentMoveVector = new Vector2();
		linearMoveController = new UCMothershipLinearMoveController(body, linearVelocity, goalPointFaultRange, length);
		angleMoveController = new DefaultAngleMoveController(body, angularVelocity, goalAngleFaultRange);
	}
	
	@Override
	public void moveTo(Array<Vector2> path) {
		
		linearMoveController.moveTo(path);
		linearMoveController.pauseMove();
		//rotateTo(linearMoveController.getCurrentMoveVector().angle());
	}

	@Override
	public void stopMove() {
		linearMoveController.stopMove();
	}

	public void rotateTo(float angle) {
		angleMoveController.moveTo(angle);
	}
	
	public void stopRotation() {
		angleMoveController.stopMove();
	}

	public void update(float delta) {
		
		angleMoveController.update(delta);
		if (linearMoveController.isMoving()) {
			rotateTo(linearMoveController.getCurrentMoveVector().angle());
			if (!angleMoveController.stopRotateCondition()) {
				linearMoveController.pauseMove();
			}else{
				linearMoveController.resumeMove();
			}
		}
		
		linearMoveController.update(delta);
		
	}
	
	public boolean isMoving() {
		return linearMoveController.isMoving();
	}

	@Override
	public float getOrientation() {
		return angleMoveController.getGoal();
	}

	@Override
	public void addIntermediatePosition(Vector2 point) {
		Array<Vector2> elements = new Array<Vector2>();
		elements.add(point);
		for (int i = linearMoveController.getCurrentPathIndex(); i < linearMoveController.getPath().size; i++) {
			elements.add(linearMoveController.getPath().get(i));
		}
		moveTo(elements);
	}

	@Override
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}
}