package deaddream.gameplayMenu;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	protected float originX;
	protected float originY;
	protected float scale;
	protected RigidBodyModel model;
	
	
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
	 originX = this.staticTexture.getRegionWidth();
	 originY = this.staticTexture.getRegionHeight();
	 switch (align) {
		case "left": this.staticTexture.setPosition(positionX-originX*(1-scale)*0.5f, positionY-originY*(1-scale)*0.5f); break;
		case "right": this.staticTexture.setPosition(positionX-originX+originX*(1-scale)*0.5f, positionY-originY*(1-scale)*0.5f); break;
		case "center": this.staticTexture.setPosition(positionX-originX/2, positionY-originY*(1-scale)*0.5f); break;
		default: break;
		}
	 
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
 @Override
 public void draw(Batch batch, float parentAlpha) {
	 this.staticTexture.draw(batch);
 }
 @Override
 public void drawDebug (ShapeRenderer shapes) {
	
	 //model.polygons.forEach(value->value.vertices.forEach(value->));
	 //shapes.polygon();
 }
}
