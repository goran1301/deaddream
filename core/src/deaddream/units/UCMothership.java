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
//import deaddream.units.utilities.DefaultMovementController;
import deaddream.units.utilities.MovementControllerInterface;
import deaddream.units.utilities.move.UCMothershipMoveController;

public class UCMothership extends Unit {
	
	protected static final int width = 600;
	protected static final int height = 680;
	
	public UCMothership(World world, Sprite staticTexture, Sprite staticNormalTexture, float x, float y, float angle) {
		super(world, staticTexture, staticNormalTexture, x, y, angle);
		setSize(width, height);
	}

	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		def.angularDamping = 1f;
		def.linearDamping = 7f;
		return def;
	}

	@Override
	protected FixtureDef fixtureDefFactory() {
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("skins/units/protectorBody"));
		FixtureDef def = new FixtureDef();
		
		def.friction = 10f;
		def.density = 10f;
		loader.attachFixture(body, "ucmothership", def, width / Constants.PPM);
		return null;
	}
	
	

	@Override
	protected MovementControllerInterface<Array<Vector2>> movementControllerFactory() {
		return new UCMothershipMoveController(body, 0.5f, 0.5f, 10.f, 0.2f, height / Constants.PPM);
	}

	

}
