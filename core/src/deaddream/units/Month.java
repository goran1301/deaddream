package deaddream.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.Constants;

import aurelienribon.bodyeditor.BodyEditorLoader;
import deaddream.units.utilities.DefaultMoveController;
import deaddream.units.utilities.LogicMovementControllerInterface;

public class Month extends Unit {

	protected static final int width = 120;
	protected static final int height = 120;
	
	public Month(World world, Sprite staticTexture, Sprite staticNormalTexture, float x, float y, float angle) {
		super(world, staticTexture, staticNormalTexture, x, y, angle);
		staticTexture.setSize(width, height);
		setSize(width, height);
		weight = 3;
	}

	@Override
	protected LogicMovementControllerInterface<Array<Vector2>, Vector2> movementControllerFactory() {
		speed = 5f;
		return new DefaultMoveController(body, 5.0f, 0.5f, 360.0f, 5.0f);
	}

	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		return def;
	}
	
	@Override
	public float getFlockRadius() {
		return (getLargestSize() + 50f) / 2;
	}

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

}
