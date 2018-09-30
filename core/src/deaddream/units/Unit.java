package deaddream.units;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.dd.Constants;

import deaddream.groupmove.SteeringEntity;
import deaddream.players.Player;
import deaddream.units.utilities.LogicMovementControllerInterface;

/**
 * 
 * 
 * @author goran
 *
 */
public abstract class Unit extends Actor implements Disableable {
	
	protected Sprite staticTexture;
	protected Sprite staticNormalTexture;
	protected Body body;
	private ClickListener clickListener;
	boolean isChecked, isDisabled;
	private boolean programmaticChangeEvents = true;
	private LogicMovementControllerInterface<Array<Vector2>, Vector2> movementController;
	private ShaderProgram shaderProgram;
	private int index;
	private final UUID uuid = UUID.randomUUID();
	private Player player;
	private Vector2 destinationPoint;
	protected SteeringEntity steering;
	protected float speed = 0;
	
	public Unit(World world, Sprite staticTexture, Sprite staticNormalTexture, float x, float y, float angle) {
		this.staticTexture = staticTexture;
		this.staticNormalTexture = staticNormalTexture;
		this.createUnit(world, x, y, angle);
		movementController = movementControllerFactory();
	}
	
	public float getFlockRadius() {
		return (getLargestSize() + 20f) / 2;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public float getLargestSize(){
		if (getWidth() > getHeight()) {
			return getWidth();
		}
		return getHeight();
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	protected abstract LogicMovementControllerInterface<Array<Vector2>, Vector2> movementControllerFactory();
	
	public void setProgrammaticChangeEvents (boolean programmaticChangeEvents) {
		this.programmaticChangeEvents = programmaticChangeEvents;
	}
	

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		
	}


	public boolean isDisabled() {
		return isDisabled;
	}
		
	/**
	 * Box2D body creation
	 * 
	 * @param world
	 * @param x meters
	 * @param y meters
	 * @param angle
	 * @return
	 */
	private void createUnit(World world, float x, float y, float angle) {
		BodyDef def = bodyDefFactory();
		
		
		def.position.set(x, y);
		def.angle = angle;
		 
		body = world.createBody(def);
		FixtureDef fixtureDef = fixtureDefFactory();
		if (fixtureDef != null) {
			body.createFixture(fixtureDef);
			fixtureDef.shape.dispose();
		}
		setTouchable(Touchable.enabled);
		addListener(clickListener = new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if (isDisabled()) return;
				setChecked(!isChecked, true);
				//System.out.println("check");
			}
		});
		isDisabled = false;
		//setDebug(true);
	}
	
	public void setChecked (boolean isChecked) {
		setChecked(isChecked, programmaticChangeEvents);
	}

	void setChecked (boolean isChecked, boolean fireEvent) {
		if (this.isChecked == isChecked) return;
		this.isChecked = isChecked;

		if (fireEvent) {
			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
			if (fire(changeEvent)) this.isChecked = !isChecked;
			Pools.free(changeEvent);
		}
	}
	
	
	public boolean isChecked () {
		return isChecked;
	}

	public boolean isPressed () {
		return clickListener.isVisualPressed();
	}

	public boolean isOver () {
		return clickListener.isOver();
	}

	public ClickListener getClickListener () {
		return clickListener;
	}

	
	/**
	 * Construct BodyDef for each unit 
	 * @return
	 */
	protected abstract BodyDef bodyDefFactory();
	
	/**
	 * Construct Fixture for each unit
	 * @return
	 */
	protected abstract FixtureDef fixtureDefFactory();
	
	
	/**
	 * render unit on screen
	 * 
	 * @param batch
	 */
	/*@Override
	public void draw(Batch batch, float parentAlpha) {
		
		this.staticTexture.setPosition(this.body.getPosition().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getPosition().y * Constants.PPM - (this.staticTexture.getHeight() /2));
		this.staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		this.staticNormalTexture.setRotation(this.staticTexture.getRotation());
		this.staticNormalTexture.setPosition(this.body.getPosition().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getPosition().y * Constants.PPM - (this.staticTexture.getHeight() /2));
		this.staticNormalTexture.getTexture().bind(1);
		this.staticTexture.getTexture().bind(0);
		System.out.println(batch+":2");
		this.staticTexture.draw(batch);
	}*/
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setShader(shaderProgram);
		this.staticTexture.setPosition(
				this.body.getWorldCenter().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getWorldCenter().y * Constants.PPM - (this.staticTexture.getHeight() /2)
			);
		this.staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		
		//System.out.println("Body's coordinate: " + String.valueOf(body.getPosition().x * Constants.PPM) + " : " + String.valueOf(body.getPosition().y * Constants.PPM));
		//System.out.println("Body's worldCenter coordinate: " + String.valueOf(body.getWorldCenter().x * Constants.PPM) + " : " + String.valueOf(body.getWorldCenter().y * Constants.PPM));
			
		/*staticTexture.setPosition(
				body.getPosition().x * Constants.PPM,
				body.getPosition().y * Constants.PPM
			);*/
		
		/*this.staticTexture.setPosition(this.body.getPosition().x * Constants.PPM,
				this.body.getPosition().y * Constants.PPM);*/
		/*this.staticTexture.setPosition(this.body.getPosition().x * Constants.PPM,
				this.body.getPosition().y * Constants.PPM);*/
		if (shaderProgram != null && staticNormalTexture != null) {
			this.staticNormalTexture.setRotation(this.staticTexture.getRotation());
			this.staticNormalTexture.setPosition(
					this.body.getWorldCenter().x * Constants.PPM - (this.staticTexture.getWidth() /2),
					this.body.getWorldCenter().y * Constants.PPM - (this.staticTexture.getHeight() /2)
				);
			this.staticNormalTexture.getTexture().bind(1);
			this.staticTexture.getTexture().bind(0);
		}
		this.staticTexture.draw(batch);
		batch.setShader(null);
	}

	/**
	 * Set new point to move
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(Array<Vector2> path) {
		movementController.moveTo(path);
		destinationPoint = path.get(path.size - 1);
	}
	
	/**
	 * Check is point a destination point
	 * 
	 * @param point
	 * @return
	 */
	public boolean isDestinationEqual(Vector2 point) {
		if (destinationPoint == null || point == null) {
			return false;
		}
		if (destinationPoint.x == point.x && destinationPoint.y == point.y) {
			return true;
		}
		return false;
	}
	
	public Vector2 getDestinationPoint() {
		return destinationPoint;
	}
	
	/**
	 * check is unit moving
	 * 
	 * @return
	 */
	public boolean isMoving() {
		return movementController.isMoving();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		update(delta);
	}
	
	
	/**
	 * logic for current delta
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		if (movementController != null) {
			if (isMoving()) {
				movementController.update(delta);
			}
			if (movementController.isMoving() || steering != null) {
				if (isMoving()) {
					steering.updateVelocity(movementController.getVelocity());
					steering.setAngle(movementController.getAngle());
				} else {
					steering.updateVelocity(new Vector2(0,0));
					steering.setAngle(body.getAngle());
				}
				steering.update(delta);
			}
		}
		setPosition (
				body.getPosition().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				body.getPosition().y * Constants.PPM - (this.staticTexture.getHeight() /2)
			);
		setOrigin(
				(this.staticTexture.getWidth() /2),
				(this.staticTexture.getHeight() /2)
			);
	}
	
	
	/*protected void rotateTo(float angle) {
		this.movementController.rotateTo(angle);
	}*/
	
    public void stopMove() {
		movementController.stopMove();
	}
    
    public float getOrientation() {
    	if (movementController != null) {
    		return movementController.getOrientation();
    	}
    	return 0f;
    }
    
    public void correctWay(Vector2 point) {
    	if (movementController != null) {
    		if (movementController.isMoving())
    		movementController.addIntermediatePosition(point);
    	}
    }
    
    public float getSpeed() {
    	return speed;
    }
	
	public Body getBody() {
		return this.body;
	}
	
	public void setSteeringEntity(SteeringEntity entity) {
		this.steering = entity;
	}
	
}
