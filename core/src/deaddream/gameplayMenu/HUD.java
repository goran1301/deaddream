package deaddream.gameplayMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import aurelienribon.bodyeditor.BodyEditorLoader.PolygonModel;
import aurelienribon.bodyeditor.BodyEditorLoader.RigidBodyModel;

public class HUD extends Actor {
	
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
	protected Vector2 polygonsPosition;
	protected Vector2 center;
	
	
 public HUD(
		 Stage stage,
		 String align,
		 float positionX, 
		 float positionY, 
		 float scale,
		 Sprite staticTexture, 
		 RigidBodyModel model){
	 this.setStage(stage);
	 this.positionX = positionX;
	 this.positionY = positionY;
	 this.scale = scale;
	 this.staticTexture = staticTexture;
	 this.staticTexture.setScale(scale);
	 this.model = model;
	 
	 Width = this.staticTexture.getRegionWidth();
	 Height = this.staticTexture.getRegionHeight();
	 switch (align) {
		case "left_bottom": 
			this.texturePosition = new Vector2(positionX-Width*(1-scale)*0.5f, positionY-Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX, positionY); 
			break;
		case "right_bottom": 
			this.texturePosition = new Vector2(positionX-Width+Width*(1-scale)*0.5f, positionY-Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX-Width*scale, positionY);
			break;
		case "center_bottom": 
			this.texturePosition = new Vector2(positionX-Width/2, positionY-Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX-Width/2*scale, positionY);
			break;
		case "left_middle": 
			this.texturePosition = new Vector2(positionX-Width*(1-scale)*0.5f, positionY-Height/2); 
			this.polygonsPosition = new Vector2(positionX, positionY-Height/2*scale); 
			break;
		case "right_middle": 
			this.texturePosition = new Vector2(positionX-Width+Width*(1-scale)*0.5f, positionY-Height/2); 
			this.polygonsPosition = new Vector2(positionX-Width*scale, positionY-Height/2*scale);
			break;
		case "center_middle": 
			this.texturePosition = new Vector2(positionX-Width/2, positionY-Height/2); 
			this.polygonsPosition = new Vector2(positionX-Width/2*scale, positionY-Height/2*scale);
			break;
		case "left_top": 
			this.texturePosition = new Vector2(positionX-Width*(1-scale)*0.5f, positionY-Height+Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX, positionY-Height*scale); 
			break;
		case "right_top": 
			this.texturePosition = new Vector2(positionX-Width+Width*(1-scale)*0.5f, positionY-Height+Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX-Width*scale, positionY-Height*scale);
			break;
		case "center_top": 
			this.texturePosition = new Vector2(positionX-Width/2, positionY-Height+Height*(1-scale)*0.5f); 
			this.polygonsPosition = new Vector2(positionX-Width/2*scale, positionY-Height*scale);
			break;
		default: break;
		}
	 this.setRotation(0);
	 this.staticTexture.setPosition(this.texturePosition.x,this.texturePosition.y);
	 this.center = new Vector2(this.texturePosition.x + this.Width/2,this.texturePosition.y + this.Height/2);
	 setPolygons(model);
	 setTouchable(Touchable.enabled);
	 clickListener = new ClickListener() {
		 @Override
		 public void clicked (InputEvent event, float x, float y) {
			 //Vector3 position = updatePosition();
			 //System.out.println("X: "+position.x+" Y: "+position.y);
		 } 
		 
	 };
	 addListener(clickListener);
 }
public Vector2 getCenter() {
	return center;
}
public Vector2 getLeftBottom() {
	return new Vector2(this.texturePosition.x,this.texturePosition.y);
}
public Vector2 getRightTop() {
	return new Vector2(this.texturePosition.x+ this.Width,this.texturePosition.y+ this.Width);
}
private void setPolygons(RigidBodyModel model) {
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
				 polygons[polygonsCount].setPosition(polygonsPosition.x, polygonsPosition.y);
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
	 shapes.setColor(Color.CYAN);
	 if (model != null) {
		 int length = polygons.length;
		 for( int polygonCount = 0;polygonCount < length;polygonCount++)
			 shapes.polygon(polygons[polygonCount].getTransformedVertices());
	 }
 }
 @Override
 public void drawDebugBounds(ShapeRenderer shapes) {
	 if (!getDebug()) return;
		shapes.rect(this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
 }
 @Override
 public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		Vector3 position = updatePosition();
		boolean isOver = false;
		for(Polygon polygon: polygons){
			 if (polygon.contains(position.x, position.y)) isOver = true;
		}
		return isOver ? this : null;
	}
 private Vector3 updatePosition() { 
	 return this.getStage().getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)); 
	 
 }
}

