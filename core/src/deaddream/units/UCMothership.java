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

public class UCMothership extends Unit {
	
	protected static final int width = 600;
	protected static final int height = 680;
	
	public UCMothership(World world, Sprite staticTexture, float x, float y, float angle) {
		super(world, staticTexture, x, y, angle);
		setSize(width, height);
	}

	@Override
	protected BodyDef bodyDefFactory() {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
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
	
	/**
	 * render unit on screen
	 * 
	 * @param batch
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
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

	@Override
	protected MovementControllerInterface<Array<Vector2>> movementControllerFactory() {
		return new DefaultMovementController(body, 0.5f, 0.5f, 10.f, 5.0f);
	}

	

}
