package deaddream.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.dd.Constants;
import aurelienribon.bodyeditor.BodyEditorLoader;
import deaddream.units.utilities.DefaultMovementController;
import deaddream.units.utilities.MovementControllerInterface;

//import aurelienribon.bodyeditor.BodyEditorLoader;

/**
 * The class of Protector unit
 * 
 * @author goran
 *
 */
public final class Protector extends Unit {

	protected static final int width = 40;
	protected static final int height = 60;
	
	public Protector(World world, Sprite staticTexture, Sprite staticNormalTexture, float x, float y, float angle) {
		super(world, staticTexture, staticNormalTexture, x, y, angle);
		staticTexture.setSize(width, height);
		setSize(width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		//def.type = BodyType.KinematicBody;
		def.angularDamping = 1f;
		def.linearDamping = 7f;
		return def;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FixtureDef fixtureDefFactory() {

		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("skins/units/protectorBody"));

		FixtureDef def = new FixtureDef();
		
		//PolygonShape shape = new PolygonShape();
		//shape.setAsBox(Protector.width / 2 / Constants.PPM, Protector.height / 2 / Constants.PPM);
		//def.shape = shape;
		def.friction = 10f;
		def.density = 10f;
		//MassData massData = body.getMassData();
		//massData.center.set(0, 0);
		
		//body.setMassData(massData);
		loader.attachFixture(body, "protector", def, width / Constants.PPM);
		return null;
	}
	
	/*@Override
	public void render(SpriteBatch batch) {
		staticTexture.setPosition(
			body.getPosition().x * Constants.PPM - (staticTexture.getWidth() / 2),
			body.getPosition().y
		);
		staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		staticTexture.draw(batch);
	}*/
	
	

	@Override
	protected MovementControllerInterface<Array<Vector2>> movementControllerFactory() {
		return new DefaultMovementController(body, 5.0f, 0.5f, 360.0f, 5.0f);
	}

	
}
