package deaddream.units.utilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class VectorLinearMoveController implements MovementControllerInterface <Array<Vector2>>{

	protected Body body;
	
	protected Array<Vector2> path;
	
	protected int currentPathIndex = 0;
	
	protected Vector2 currentMoveGoal;
	
	protected boolean isMoving;
	
	protected Vector2 steering = new Vector2();
	
	protected Vector2 position = new Vector2();
	
	protected Vector2 currentVelocity = new Vector2();
	
	protected float maxVelocity;
	
	protected float goalPointFaultRange;
	
	protected Vector2 velocity = new Vector2();
	
	
	
	public VectorLinearMoveController(Body body, float linearVelocity, float goalPointFaultRange) {
		this.body = body;
		this.maxVelocity = linearVelocity;
		this.goalPointFaultRange = goalPointFaultRange;
		this.currentMoveGoal = new Vector2();
	}
	
	@Override
	public void moveTo(Array<Vector2> path) {
		if (path != null) {
			if (path.size > 0) {
				this.path = path;
				currentPathIndex = 0;
				currentMoveGoal = path.get(0);
				isMoving = true;
			}
		}	
	}

	@Override
	public void stopMove() {
		//body.setLinearVelocity(0f, 0f);
		currentPathIndex = 0;
		path = null;
		isMoving = false;
	}

	@Override
	public void update(float delta) {
		steering.x = 0;
		steering.y = 0;
		position.x = body.getPosition().x;
		position.y = body.getPosition().y;
		if (isMoving && checkGoalAchieved()) {
			if (currentPathIndex + 1 >= path.size) {
				stopMove();
			}else {
				nextGoal();
			}
		}
		//seek steering behavior for a path's point
		if (isMoving) {
			//desired_velocity = normalize(target - position) * max_velocity
			velocity.x = currentMoveGoal.x - position.x;
			velocity.y = currentMoveGoal.y - position.y;
			velocity = normalize(velocity);
			velocity.x *= maxVelocity;
			velocity.y *= maxVelocity;
			velocity.limit(maxVelocity);
			//steering = desired_velocity - velocity
			steering.x = velocity.x - body.getLinearVelocity().x;
			steering.y = velocity.y - body.getLinearVelocity().y;
			
			steering.limit(maxVelocity);
			//steering.x *= delta;
			//steering.y *= delta;
			//position = position + velocity
			//position.x += steering.x;
			//position.y += steering.y;
			//body.setTransform(position, body.getAngle());
		}
	}
	
	protected void nextGoal() {
		currentPathIndex++;
		currentMoveGoal = path.get(currentPathIndex);
	}
	
	protected boolean checkGoalAchieved() {
		float distanse = (float)Math.sqrt((position.x - currentMoveGoal.x) * (position.x - currentMoveGoal.x) + (position.y - currentMoveGoal.y) * (position.y - currentMoveGoal.y));
		if (distanse <= goalPointFaultRange) {
			return true;
		}
		return false;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public Vector2 normalize(Vector2 vector) {
		float invLen = 1 / vector.len();
		//Vector2 normalized = new Vector2();
		vector.x *= invLen;
		vector.y *= invLen;
		return vector;
	}
	
	public Vector2 getVelocity() {
		return steering;
	}
	
}
