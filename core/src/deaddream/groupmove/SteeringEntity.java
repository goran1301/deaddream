package deaddream.groupmove;

import com.badlogic.gdx.ai.steer.Proximity.ProximityCallback;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.proximities.ProximityBase;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.Constants;

import deaddream.units.Unit;

public class SteeringEntity implements Steerable<Vector2> {
	
	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
	
	private Unit unit;
	
	private boolean tagged;
	
	float boundingRadius;
	
	float maxLinearSpeed, maxLinearAcceleration;
	
	float maxAngularSpeed, maxAngularAcceleration;
	
	float zeroLinearSpeedThreshold = 0.001f;
	
	float orientation = 0f;
	
	Vector2 velocity = new Vector2();
	
	Vector2 steeringLinear = new Vector2();
	
	Vector2 resultVector = new Vector2();
	
	SteeringBehavior<Vector2> steeringBehavior;
	
	ProximityBase proximity;
	
	float angle = 0;

	public SteeringEntity(Unit unit) {
		this.unit = unit;
	}
	
	public void setProximity(ProximityBase<Vector2> radius) {
		proximity = radius;
	}
	
	public ProximityBase<Vector2> getProximity() {
		return proximity;
	}
	
	public void updateVelocity(Vector2 velocity) {
		this.velocity.x = velocity.x;
		this.velocity.y = velocity.y;
	}
	
	@Override
	public Vector2 getPosition() {
		Vector2 position = new Vector2();
		position.x = unit.getBody().getPosition().x;
		position.y = unit.getBody().getPosition().y;
		return position;
	}

	@Override
	public float getOrientation() {
		//return velocity.angle();
		return orientation;
	}

	@Override
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return SteeringUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return SteeringUtils.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation() {
		return new SteeringLocation();
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroLinearSpeedThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zeroLinearSpeedThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		return this.maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
		
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return this.maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return velocity;
	}

	@Override
	public float getAngularVelocity() {
		return unit.getBody().getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return unit.getFlockRadius() / Constants.PPM;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}
	
	public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}
	
	public void update(float delta) {
		orientation = this.vectorToAngle(velocity);
		/*ProximityCallback<Vector2> callback = new ProximityCallback<Vector2>() {
			
			@Override
			public boolean reportNeighbor(Steerable<Vector2> neighbor) {
				System.out.println("SHIP SO CLOSE!!!");
				return false;
			}
		};z
		proximity.findNeighbors(callback);*/
		//System.out.println("PROXIMITY AGENTS COUNT: " + String.valueOf(proximity.getAgents().size));
		//System.out.println("PROXIMITY AGENTS COUNT: " + String.valueOf(proximity.findNeighbors(callback)));
		
		if (steeringBehavior != null) {
			steeringBehavior.calculateSteering(steeringOutput);
			
			//if (steeringOutput.linear.len() == 0) {
				/*steeringLinear.x = velocity.x;
				steeringLinear.y = velocity.y;*/
				
			//} else {
				steeringLinear.x = steeringOutput.linear.x;
				steeringLinear.y = steeringOutput.linear.y;
				//steeringLinear = normalize(steeringLinear);
				
				
				//ToDo 1 for protector, 2 for moth   
				steeringLinear.x *= getMaxLinearSpeed() * 2;
				steeringLinear.y *= getMaxLinearSpeed() * 2;
				steeringLinear.limit(getMaxLinearSpeed());
			//}
			//steeringLinear.limit(getMaxLinearSpeed());
			//velocity.x += steeringOutput.linear.x;
			//velocity.y += steeringOutput.linear.y;
			//velocity.limit(getMaxLinearSpeed());
			
			resultVector.x = velocity.x + steeringLinear.x;
			resultVector.y = velocity.y + steeringLinear.y;
			resultVector.limit(getMaxLinearSpeed());
			resultVector.x *= delta;
			resultVector.y *= delta;
			
			Vector2 position = unit.getBody().getPosition();
			position.x += resultVector.x;
			position.y += resultVector.y;
			//orientation = steeringOutput.angular;
			//float newOrientation = vectorToAngle(velocity);
			//float newAng = (newOrientation - orientation) * delta; // this is superfluous if independentFacing is always true
			
			unit.getBody().setTransform(position, angle + steeringOutput.angular * maxAngularSpeed * delta);			
		}
	}
	
	public void test(float delta) {
		Vector2 position = unit.getBody().getPosition();
		velocity.mulAdd(steeringOutput.linear, delta).limit(getMaxLinearSpeed());
		position.mulAdd(velocity, delta);
		
		unit.getBody().setTransform(position, unit.getBody().getAngle());	
	}
	
	public Vector2 normalize(Vector2 vector) {
		float invLen = 1 / vector.len();
		//Vector2 normalized = new Vector2();
		vector.x *= invLen;
		vector.y *= invLen;
		return vector;
	}
	
	public Vector2 getSteering() {
		return steeringLinear;
	}
	
	public void setAngle(float angle) {
		orientation = angle;
		this.angle = angle;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

}
