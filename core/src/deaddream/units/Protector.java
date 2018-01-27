package deaddream.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.dd.Constants;

public final class Protector extends Unit {

	protected static final int width = 40;
	protected static final int height = 60;
	
	public Protector(World world, Texture staticTexture, float x, float y, float angle) {
		
		super(world, staticTexture, x, y, angle, createBodyDef(), createFixtureDef());
		this.goalPointFaultRange = 0.5f;
		this.goalAngleFaultRange = 5.0f;
		this.angularVelocity = 360.0f;
		this.velocity = 5f;
		// TODO Auto-generated constructor stub
	}

	private static FixtureDef createFixtureDef() {
		FixtureDef def = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Protector.width / 2 / Constants.PPM, Protector.height / 2 / Constants.PPM);
		def.shape = shape;
		def.friction = 10f;
		def.density = 10f;
		return def;
	}
	
	private static BodyDef createBodyDef() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.angularDamping = 1f;
		def.linearDamping = 7f;
		return def;
	}
	
}
