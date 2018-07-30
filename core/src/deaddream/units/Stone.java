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
import deaddream.groupmove.SteeringEntity;
import deaddream.units.utilities.LogicMovementControllerInterface;

/**
 * Static stone object
 * 
 * @author goran
 *
 */
public final class Stone extends Unit {
	
	protected static final int width = 80;
	protected static final int height = 120;

	public Stone(World world, Sprite staticTexture, Sprite staticNormalTexture, float x, float y, float angle) {
		super(world, staticTexture, staticNormalTexture, x, y, angle);
		setSize(width, height);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
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
		//shape.setAsBox(Stone.width / 2 / Constants.PPM, Stone.height / 2 / Constants.PPM);
		//def.shape = shape;
		def.friction = 10f;
		def.density = 10f;
		loader.attachFixture(body, "Name", def, width / Constants.PPM);
		return null;
	}
	
	


	@Override
	protected LogicMovementControllerInterface<Array<Vector2>, Vector2> movementControllerFactory() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
