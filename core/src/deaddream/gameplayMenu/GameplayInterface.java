package deaddream.gameplayMenu;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import aurelienribon.bodyeditor.BodyEditorLoader.PolygonModel;
import aurelienribon.bodyeditor.BodyEditorLoader.RigidBodyModel;

public class GameplayInterface extends Actor {
	
	protected Sprite staticTexture;
	protected ClickListener clickListener;
	protected boolean isChecked;
	protected float positionX; 
	protected float positionY; 
	protected float Width;
	protected float Height;
	protected float scale;
	protected RigidBodyModel model;
	protected Polygon[] polygons;
	protected Vector2 texturePosition;
	protected Vector2 polygonPosition;
	
	
 public GameplayInterface(
		 String align,
		 float positionX, 
		 float positionY, 
		 float scale,
		 Sprite staticTexture, 
		 RigidBodyModel model){
	 this.positionX = positionX;
	 this.positionY = positionY;
	 this.scale = scale;
	 this.staticTexture = staticTexture;
	 this.staticTexture.setScale(scale);
	 this.model = model;
	 Width = this.staticTexture.getRegionWidth();
	 Height = this.staticTexture.getRegionHeight();
	 switch (align) {
		case "left": 
			this.texturePosition = new Vector2(positionX-Width*(1-scale)*0.5f, positionY-Height*(1-scale)*0.5f); 
			this.polygonPosition = new Vector2(positionX, positionY); 
			break;
		case "right": 
			this.texturePosition = new Vector2(positionX-Width+Width*(1-scale)*0.5f, positionY-Height*(1-scale)*0.5f); 
			this.polygonPosition = new Vector2(positionX-Width*scale, positionY);
			break;
		case "center": 
			this.texturePosition = new Vector2(positionX-Width/2, positionY-Height*(1-scale)*0.5f); 
			this.polygonPosition = new Vector2(positionX-Width/2*scale, positionY);
			break;
		default: break;
		}
	 this.staticTexture.setPosition(this.texturePosition.x,this.texturePosition.y);
	 setPolygons(model);
	 setTouchable(Touchable.enabled);
	 addListener(clickListener = new ClickListener() {
		 public void clicked (InputEvent event, float x, float y) {
			 setChecked(!isChecked, true);
		 }
	 });
 }
 
 void setChecked (boolean isChecked, boolean fireEvent) {
		if (this.isChecked == isChecked) return;
		this.isChecked = isChecked;
	}
 
 void setPolygons(RigidBodyModel model) {
	 if (model != null) {
		 polygons = new Polygon[model.polygons.size()];
		// for(PolygonModel polygon: model.polygons) {
		 for(int polygonsCount = 0; polygonsCount < model.polygons.size(); polygonsCount++) {
			 PolygonModel polygon = model.polygons.get(polygonsCount);
			 if(!polygon.vertices.isEmpty()) {
				 int polygonSize = 0;
				 for (@SuppressWarnings("unused") Vector2 vertices:polygon.vertices) {
					 polygonSize+=2;
				 }
				 float[] points =  new float[polygonSize];
				 int currentVertex = 0;
				 for (Vector2 vertices:polygon.vertices) {
					 points[currentVertex] = vertices.x;
					 currentVertex++;
					 points[currentVertex] = vertices.y;
					 currentVertex++;
				 }
				 polygons[polygonsCount] = new Polygon(points);
				 polygons[polygonsCount].setScale(Width*scale, Height*scale*(Width/Height));
				 polygons[polygonsCount].setPosition(polygonPosition.x, polygonPosition.y);
			 } 
		 }
	 }
 }
 @Override
 public void draw(Batch batch, float parentAlpha) {
	 this.staticTexture.draw(batch);
 }
 @Override
 public void drawDebug (ShapeRenderer shapes) {
	 if (model != null) {
		 int length = polygons.length;
		 for( int polygonCount = 0;polygonCount < length;polygonCount++)
			 shapes.polygon(polygons[polygonCount].getTransformedVertices());
	 }
 }
}

