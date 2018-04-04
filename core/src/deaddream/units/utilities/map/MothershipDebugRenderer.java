package deaddream.units.utilities.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.dd.Constants;

import deaddream.units.UCMothership;

public class MothershipDebugRenderer {
	
	private UCMothership mothership;
	
	private float length;
	
	private Vector2 frontPoint = new Vector2();
	
	private float bodyAngle;
	
	public MothershipDebugRenderer(UCMothership mothership) {
		this.mothership = mothership;
		length = mothership.getHeight() / Constants.PPM / 2;
	}
	
	private float standartAngle(float angle){
		angle = angle%360;
		if (angle<0){
			angle = angle+360;
		} 
		return angle;
	}
	
	private void updateBodyAngle() {
		bodyAngle = mothership.getBody().getAngle() * Math.abs(MathUtils.radiansToDegrees ) + 90; 
		//System.out.println(String.valueOf(bodyAngle));
	}
	
	public void calculateFrontPoint() {
		frontPoint.x = (float)Math.cos(mothership.getBody().getAngle() + MathUtils.degreesToRadians * 90) * length + mothership.getBody().getPosition().x;
		frontPoint.y = (float)Math.sin(mothership.getBody().getAngle() + MathUtils.degreesToRadians * 90) * length + mothership.getBody().getPosition().y;
	}
	
	private void update(float delta) {
		//System.out.println(String.valueOf(frontPoint.x) + " : " + String.valueOf(frontPoint.y));
		updateBodyAngle();
		calculateFrontPoint();
	}
	
	public void render(float delta, ShapeRenderer renderer) {
		update(delta);
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.BLUE);
		renderer.line(mothership.getBody().getPosition().x * Constants.PPM,
				mothership.getBody().getPosition().y * Constants.PPM,
				frontPoint.x * Constants.PPM,
				frontPoint.y * Constants.PPM
			);
		renderer.end();
	}
	
}
