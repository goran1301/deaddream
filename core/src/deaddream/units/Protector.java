package deaddream.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.dd.Constants;
import aurelienribon.bodyeditor.BodyEditorLoader;

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
	
	public Protector(World world, Texture staticTexture, float x, float y, float angle) {
		
		super(world, staticTexture, x, y, angle);
		this.goalPointFaultRange = 0.5f;
		this.goalAngleFaultRange = 5.0f;
		this.angularVelocity = 360.0f;
		this.velocity = 5f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
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
	
	/**
	 * render unit on screen
	 * 
	 * @param batch
	 */
	@Override
	public void render(SpriteBatch batch) {
		this.staticTexture.setPosition(
				this.body.getWorldCenter().x * Constants.PPM - (this.staticTexture.getWidth() /2),
				this.body.getWorldCenter().y * Constants.PPM - (this.staticTexture.getHeight() /2)
			);
		
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
		this.staticTexture.setRotation(MathUtils.radiansToDegrees * this.body.getAngle());
		this.staticTexture.draw(batch);
	}
}
