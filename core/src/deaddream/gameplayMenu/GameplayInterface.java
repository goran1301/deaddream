package deaddream.gameplayMenu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameplayInterface extends Actor {
	
	protected Sprite staticTexture;
	private ClickListener clickListener;
	boolean isChecked;
	float positionX; 
	float positionY; 
	float originX;
	float originY;
	float scale;
	
 public GameplayInterface(
		 String align,
		 float positionX, 
		 float positionY, 
		 float scale,
		 Sprite staticTexture){
	 this.positionX = positionX;
	 this.positionY = positionY;
	 this.scale = scale;
	this.staticTexture = staticTexture;
	this.staticTexture.setScale(scale);
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
}
