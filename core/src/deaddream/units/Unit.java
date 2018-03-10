package deaddream.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import deaddream.units.utilities.MovementControllerInterface;

/**
 * 
 * 
 * @author goran
 *
 */
public abstract class Unit extends Actor implements Disableable {
	
	protected Sprite staticTexture;
	protected Body body;
	private ClickListener clickListener;
	boolean isChecked, isDisabled;
	private boolean programmaticChangeEvents = true;
	private MovementControllerInterface<Array<Vector2>> movementController;
	
	
	public Unit(World world, Sprite staticTexture, float x, float y, float angle) {
		this.staticTexture = staticTexture;
		this.createUnit(world, x, y, angle);
		movementController = movementControllerFactory();
	}
	
	protected abstract MovementControllerInterface<Array<Vector2>> movementControllerFactory();
	
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
			public void clicked (InputEvent event, float x, float y) {
				if (isDisabled()) return;
				setChecked(!isChecked, true);
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
	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.staticTexture.setPosition(this.body.getPosition().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getPosition().y * Constants.PPM - (this.staticTexture.getHeight() /2));
		this.staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		this.staticTexture.draw(batch);
	}
	

	/**
	 * Set new point to move
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(Array<Vector2> path) {
		movementController.moveTo(path);
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
			movementController.update(delta);
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
	
	
	protected void rotateTo(float angle) {
		this.movementController.rotateTo(angle);
	}
	
    public void stopMove() {
		movementController.stopMove();
	}
	
	public Body getBody() {
		return this.body;
	}
	
	
}
