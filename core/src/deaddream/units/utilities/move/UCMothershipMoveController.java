package deaddream.units.utilities.move;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import deaddream.units.utilities.DefaultAngleMoveController;
import deaddream.units.utilities.DefaultLinearMoveController;
import deaddream.units.utilities.MovementControllerInterface;

public class UCMothershipMoveController  implements MovementControllerInterface <Array<Vector2>> {

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
}