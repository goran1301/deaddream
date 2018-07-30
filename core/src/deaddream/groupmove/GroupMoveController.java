package deaddream.groupmove;

import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Cohesion;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.Constants;

import deaddream.players.Player;
import deaddream.units.Unit;

public class GroupMoveController {
	
	Player currentPlayer;
	Array<SteeringEntity> steeringEntities = new Array<SteeringEntity>();
	
	public GroupMoveController(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public void addUnit(Unit unit) {
		SteeringEntity entity = new SteeringEntity(unit);
		entity.setMaxLinearSpeed(unit.getSpeed());
		entity.setMaxLinearAcceleration(unit.getSpeed());
		entity.setMaxAngularSpeed(5);
		entity.setMaxAngularAcceleration(0);
		steeringEntities.add(entity);
		RadiusProximity<Vector2> proximity = new RadiusProximity<Vector2>(entity, steeringEntities,
		(unit.getFlockRadius()) / Constants.PPM);
		entity.setProximity(proximity);
		
		
        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(entity, 0.0001f);
        CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(entity, proximity);
        

        Separation<Vector2> separationSB = new Separation<Vector2>(entity, proximity);
        Cohesion<Vector2> cohesionSB = new Cohesion<Vector2>(entity, proximity);
        Alignment<Vector2> alignmentSB = new Alignment<Vector2>(entity, proximity);
        
        prioritySteeringSB.add(separationSB);
        //prioritySteeringSB.add(collisionAvoidanceSB);
        //prioritySteeringSB.add(cohesionSB);
        prioritySteeringSB.add(alignmentSB);
        
        BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(entity) //
				.add(alignmentSB, .2f) //
				.add(cohesionSB, .06f) //
				.add(separationSB, 1.7f);
        
        //prioritySteeringSB.add(blendedSteering);
        
       //prioritySteeringSB.add(collisionAvoidanceSB);
     //	prioritySteeringSB.add(wanderSB);
        //entity.setSteeringBehavior(separationSB);
        entity.setSteeringBehavior(prioritySteeringSB);
        
        unit.setSteeringEntity(entity);
	}
	
	public void update() {
		for(int i = 0; i < currentPlayer.getUnits().size; i++) {
			Unit unit = currentPlayer.getUnit(i);
			//unit.getBody().getPosition();
			if (!unit.isMoving()) {
				continue;
			}
			for (int j = 0; j < currentPlayer.getUnits().size; j++) {
				if (i == j) {
					continue;
				}
				Unit current = currentPlayer.getUnit(j);
				if (!current.isMoving()) {
					if (unit.isDestinationEqual(current.getDestinationPoint())) {
						float distance = (float)Math.sqrt(
								Math.pow((current.getBody().getPosition().x - unit.getBody().getPosition().x) * Constants.PPM, 2) +
								Math.pow((current.getBody().getPosition().y - unit.getBody().getPosition().y) * Constants.PPM, 2)
							);
						if (distance <= unit.getFlockRadius() + current.getFlockRadius()) {
							unit.stopMove();
						}
					}
				}
			}
		}
	}
	
	public void render(ShapeRenderer shapeRenderer) {
		for(Unit unit : currentPlayer.getUnits()) {
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.circle(
					unit.getBody().getPosition().x * Constants.PPM,
					unit.getBody().getPosition().y * Constants.PPM,
					unit.getFlockRadius()
				);
		}
		for(SteeringEntity entity : steeringEntities) {
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.line(entity.getPosition().x * Constants.PPM, entity.getPosition().y * Constants.PPM,
					(entity.getPosition().x + entity.getVelocity().x) * Constants.PPM, (entity.getPosition().y + entity.getVelocity().y) * Constants.PPM);
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.line(entity.getPosition().x * Constants.PPM, entity.getPosition().y * Constants.PPM,
					(entity.getPosition().x + entity.getSteering().x) * Constants.PPM, (entity.getPosition().y + entity.getSteering().y) * Constants.PPM);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(
					entity.getPosition().x * Constants.PPM,
					entity.getPosition().y * Constants.PPM,
					entity.getProximity().getRadius() * Constants.PPM
				);
			
		}
	}
}
