package deaddream.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class InterfaceRenderer {
	
	private TextureAtlas textureAtlas;
	private Pixmap AtlasPm;
	private Pixmap CoursorPm;
	private AtlasRegion region;
	
	public InterfaceRenderer() {
		textureAtlas = new TextureAtlas(Gdx.files.internal("GameplayInterfaceSpriteMap/SpriteMapConfiguration.atlas"));
		System.out.println("Interface Create");
	}
	
	public void show() {
		renderCursor();
	} 
	private void renderCursor() {
		region = textureAtlas.findRegion("cursor");
		if (!region.getTexture().getTextureData().isPrepared()) {
			region.getTexture().getTextureData().prepare();
		}
		AtlasPm = region.getTexture().getTextureData().consumePixmap();
		int width = region.getRegionWidth();
	    int height = region.getRegionHeight();
	    int regionx = region.getRegionX();
	    int regiony = region.getRegionY();
		CoursorPm = new Pixmap(128, 128, Format.RGBA8888);
		CoursorPm.drawPixmap(AtlasPm, regionx, regiony, width, height, 0, 0, width, height);
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(CoursorPm, 0, 0));
	}
	public void dispose() {
		AtlasPm.dispose();
		CoursorPm.dispose();
		textureAtlas.dispose();
		region.getTexture().getTextureData().disposePixmap();
	}
}
