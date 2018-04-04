package deaddream.units.utilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class DefaultMoveController implements MovementControllerInterface <Array<Vector2>> {

	protected Body body;
	
	DefaultLinearMoveController linearMoveController;
	
	DefaultAngleMoveController angleMoveController;
	
	public DefaultMoveController(Body body, float linearVelocity, float goalPointFaultRange, float angularVelocity, float goalAngleFaultRange) {
		this.body = body;
		linearMoveController = new DefaultLinearMoveController(body, linearVelocity, goalPointFaultRange);
		angleMoveController = new DefaultAngleMoveController(body, angularVelocity, goalAngleFaultRange);
	}
	
	@Override
	public void moveTo(Array<Vector2> path) {
		linearMoveController.moveTo(path);
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
		linearMoveController.update(delta);
		if (linearMoveController.isMoving()){
			this.rotateTo(linearMoveController.getCurrentMoveVector().angle());
		}
		angleMoveController.update(delta);
	}
	
	public boolean isMoving() {
		return linearMoveController.isMoving();
	}
}