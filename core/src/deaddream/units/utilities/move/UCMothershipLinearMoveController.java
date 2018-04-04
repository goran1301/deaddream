package deaddream.units.utilities.move;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.dd.Constants;

import deaddream.units.utilities.DefaultLinearMoveController;

public final class UCMothershipLinearMoveController extends DefaultLinearMoveController {

	private float length;
	
	private Vector2 frontPoint = new Vector2();
	
	
	public UCMothershipLinearMoveController(Body body, float linearVelocity, float goalPointFaultRange, float length) {
		super(body, linearVelocity, goalPointFaultRange);
		this.length = length / 2;
	}
		
	public void calculateFrontPoint() {
		if (isMoving()) {
			frontPoint.x = (float)Math.cos(body.getAngle() + MathUtils.degreesToRadians * 90) * length + body.getPosition().x;
			frontPoint.y = (float)Math.sin(body.getAngle() + MathUtils.degreesToRadians * 90) * length + body.getPosition().y;
		}
	}
	
	private void courseCorrection() {
		if (path.size < 2 || currentPathIndex == 0) {
			return;
		}
		for (int index = currentPathIndex - 1; index < path.size - 1; index++) {
			System.out.println(String.valueOf(Math.abs(path.get(path.size-1).x) - Math.abs(path.get(path.size-2).x)));
		    if (path.get(index).x != path.get(path.size-2).x || Math.abs(Math.abs(path.get(path.size-1).x) - Math.abs(path.get(path.size-2).x)) >= 80f / Constants.PPM) {
		    	break;
		    }
		    System.out.println(String.valueOf(currentPathIndex) +" "+ String.valueOf(index) + " of " + String.valueOf(path.size-1));
		    if (index == path.size-2) {
		    	System.out.println("final");
				currentPathIndex = path.size-1;
				currentMoveGoal = path.get(currentPathIndex);
				if (this.isMoving) {
					keepMove(currentMoveGoal.x, currentMoveGoal.y);
				}
				return;
			}
		}
		
		for (int index = currentPathIndex - 1; index < path.size - 1; index++) {
		    if (path.get(index).y != path.get(path.size-2).y || Math.abs(Math.abs(path.get(path.size-1).y) - Math.abs(path.get(path.size-2).y)) >= 80f / Constants.PPM) {
		    	break;
		    }
		    System.out.println(String.valueOf(index) + " of " + String.valueOf(path.size-1));
		    if (index == path.size-2) {
		    	System.out.println("final");
				currentPathIndex = path.size-1;
				currentMoveGoal = path.get(currentPathIndex);
				if (this.isMoving) {
					keepMove(currentMoveGoal.x, currentMoveGoal.y);
				}
				return;
			}
		}
		
		return;
	}
	
	public void update(float delta) {
		if (isMoving()) {
			courseCorrection();
		}
		calculateFrontPoint();
		super.update(delta);
	}
	
	@Override
	public boolean stopMoveCondition() {
		if (!lastPointGoalAchieveCondition()) {
			return super.stopMoveCondition();
		} else {
			return true;
		}
	}
	
	private boolean lastPointGoalAchieveCondition() {
		
		if (path == null || path.size == 0) {
			return true;
		}
		
		float biggestX;
		float smallestX;
		float biggestY;
		float smallestY;
		
		if (body.getPosition().x > frontPoint.x) {
			biggestX = body.getPosition().x;
			smallestX = frontPoint.x;
		} else {
			biggestX = frontPoint.x;
			smallestX = body.getPosition().x;
		}
		
		if (body.getPosition().y > frontPoint.y) {
			biggestY = body.getPosition().y;
			smallestY = frontPoint.y;
		} else {
			biggestY = frontPoint.y;
			smallestY = body.getPosition().y;
		}
		
		if (path.get(path.size - 1).x < smallestX || path.get(path.size - 1).x > biggestX || path.get(path.size - 1).y < smallestY || path.get(path.size - 1).y > biggestY) {
			return false;
		}
		
		//(x-x1)/(x2-x1)=(y-y1)/(y2-y1) http://www.sql.ru/forum/314428/kak-opredelit-lezhit-li-tochka-na-otrezke
		return Math.abs((path.get(path.size - 1).x - body.getPosition().x ) / (frontPoint.x - body.getPosition().x)
				- (path.get(path.size - 1).y - body.getPosition().y)/(frontPoint.y - body.getPosition().y))
				<= goalPointFaultRange;
	}
	
}
