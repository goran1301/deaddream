package deaddream.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dd.Constants;

public class TiledObjectUtil {
	public static void parseTiledObjectLayer(World world, MapObjects objeсts){
		for (MapObject object : objeсts) {
			Shape shape;
			if (object instanceof PolylineMapObject) {
				shape = createPolyline((PolylineMapObject)object);
			}else{
				continue;
			}
			
			Body body;
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.StaticBody;
			body = world.createBody(bodyDef);
			body.createFixture(shape, 1.0f);
			shape.dispose();
		}
	}
	
	private static ChainShape createPolyline(PolylineMapObject polyline) {
		float[] vertices = polyline.getPolyline().getTransformedVertices();
		for (int i = 0; i < vertices.length; i++) {
		    vertices[i] = vertices[i] / Constants.PPM;
	    }
		ChainShape cs = new ChainShape();
		cs.createChain(vertices);
		return cs;
	}
}
