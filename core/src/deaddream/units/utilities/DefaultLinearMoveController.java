package deaddream.units.utilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class DefaultLinearMoveController implements MovementControllerInterface <Array<Vector2>> {

		protected Body body;
		
		protected Vector2 currentMoveGoal;
		
		protected Vector2 currentMoveVector;

		protected boolean isMoving = false;
		
		protected float goalPointFaultRange;
		
		protected float linearVelocity;
		
		protected Array<Vector2> path;
		
		protected int currentPathIndex = 0;
		
		public DefaultLinearMoveController(Body body, float linearVelocity, float goalPointFaultRange) {
			this.body = body;
			this.linearVelocity = linearVelocity;
			this.goalPointFaultRange = goalPointFaultRange;
			this.currentMoveGoal = new Vector2();
			this.currentMoveVector = new Vector2();
		}
		
		public void keepMove(float x, float y) {
			this.currentMoveGoal.x = x;
			this.currentMoveGoal.y = y;
			float rangeX = x - body.getPosition().x;
			float rangeY = y - body.getPosition().y;
			float maxXY= Math.max(Math.abs( rangeX ), Math.abs( rangeY ));
			
			
			this.currentMoveVector.x = this.currentMoveGoal.x - this.body.getPosition().x;
			this.currentMoveVector.y = this.currentMoveGoal.y - this.body.getPosition().y;
			
			body.setLinearVelocity(rangeX / maxXY * linearVelocity, rangeY / maxXY * linearVelocity);
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
		
		public void pauseMove() {
			body.setLinearVelocity(0f, 0f);
			isMoving = false;
		}
		
		public void resumeMove() {
			if (path != null) {
				keepMove(currentMoveGoal.x, currentMoveGoal.y);
			}
		}
		
		

		public void update(float delta) {
			if (isMoving()) {
				this.currentMoveVector.x = this.currentMoveGoal.x - body.getPosition().x;
				this.currentMoveVector.y = this.currentMoveGoal.y - body.getPosition().y;
				updateCurrentPathGoal();
				if(stopMoveCondition()) {
					stopMove();
					return;
				}
			}
			if (isMoving){
				keepMove(currentMoveGoal.x, currentMoveGoal.y);		
			}
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

				
		public boolean isMoving() {
			return isMoving || path != null;
		}
		
		
		public Vector2 getCurrentMoveVector() {
			return currentMoveVector;
		}
		
		public Vector2 getCurrentMoveGoal() {
			return currentMoveGoal;
		}
		
		public int getCurrentPathIndex() {
			return currentPathIndex;
		}
		
		public Array<Vector2> getPath(){
			return path;
		}
}
