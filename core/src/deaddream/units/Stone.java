package deaddream.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.dd.Constants;

/**
 * Static stone object
 * 
 * @author goran
 *
 */
public final class Stone extends Unit {
	
	protected static final int width = 80;
	protected static final int height = 120;

	public Stone(World world, Texture staticTexture, float x, float y, float angle) {
		super(world, staticTexture, x, y, angle, createBodyDef(), createShape());
		// TODO Auto-generated constructor stub
	}
	
	
	private static PolygonShape createShape() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Stone.width / 2 / Constants.PPM, Stone.height / 2 / Constants.PPM);
		return shape;
	}
	
	private static BodyDef createBodyDef() {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		return def;
	}
	
}
