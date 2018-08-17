package deaddream.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import deaddream.gameplayMenu.GameplayInterface;

public class GameplayInterfaceRenderer {
	private TextureAtlas textureAtlas;
	private Pixmap AtlasPm;
	private Pixmap CoursorPm;
	private AtlasRegion region;
	private GameplayInterface MapFrame,MiddleFrame,MenuFrame;
	private float scale = 1f;
	
	public GameplayInterfaceRenderer(int width, int height) {
		textureAtlas = new TextureAtlas(Gdx.files.internal("GameplayInterfaceSpriteMap/SpriteMapConfiguration.atlas"));
		System.out.println("Interface Create");	
		MapFrame = createInterfaceElement("map_frame",0.0f,0.0f, "left");
		MiddleFrame = createInterfaceElement("middle_frame",width*0.5f + width*0.05f*scale,0.0f,"center");
		MenuFrame = createInterfaceElement("menu_frame",width, 0.0f, "right");
	}

	public void show() {
		showCursor();
	} 
	private void showCursor() {
		region = textureAtlas.findRegion("cursor");
		if (!region.getTexture().getTextureData().isPrepared()) {
			region.getTexture().getTextureData().prepare();
		}
		AtlasPm = region.getTexture().getTextureData().consumePixmap();
		int width = region.getRegionWidth();
	    int height = region.getRegionHeight();
	    int regionx = region.getRegionX();
	    int regiony = region.getRegionY();
		CoursorPm = new Pixmap(64, 64, Format.RGBA8888);
		CoursorPm.drawPixmap(AtlasPm, regionx, regiony, width, height, 0, 0, width, height);
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(CoursorPm, 0, 0));
	}
	private GameplayInterface createInterfaceElement(
			String regionName, 
			float positionX, 
			float positionY,
			String align) {
		region = textureAtlas.findRegion(regionName);
		Sprite sprite = new Sprite(region);
		return new GameplayInterface(align, positionX,positionY,scale,sprite);
	}
	public void render(Batch batch){
		MapFrame.draw(batch, 1);
		MiddleFrame.draw(batch, 1);
		MenuFrame.draw(batch, 1);
	}
	public void dispose() {
		AtlasPm.dispose();
		CoursorPm.dispose();
		textureAtlas.dispose();
		region.getTexture().getTextureData().disposePixmap();
	}
}
