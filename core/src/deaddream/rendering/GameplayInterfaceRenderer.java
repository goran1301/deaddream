package deaddream.rendering;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import aurelienribon.bodyeditor.BodyEditorLoader;
import aurelienribon.bodyeditor.BodyEditorLoader.RigidBodyModel;
import deaddream.gameplayMenu.GameplayInterface;

public class GameplayInterfaceRenderer {
	private TextureAtlas textureAtlas;
	private Pixmap AtlasPm;
	private Pixmap CoursorPm;
	private AtlasRegion region;
	private float scale = 0.5f;
	private ArrayList<GameplayInterface> panels;
	private Map<String, RigidBodyModel> panelsPolygons;
	
	public GameplayInterfaceRenderer(int width, int height) {
		textureAtlas = new TextureAtlas(Gdx.files.internal("GameplayInterfaceSpriteMap/SpriteMapConfiguration.atlas"));
		System.out.println("Interface Create");	
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("GameplayInterface/GUIPanelPolygons"));
		//System.out.println(loader.getInternalModel().rigidBodies.keySet().toArray()[0]);
		panelsPolygons = loader.getInternalModel().rigidBodies;
		//loader.getInternalModel().rigidBodies.keySet().toArray().length;
		//loader.getInternalModel().rigidBodies.get("map_frame.png");
		panels = new ArrayList<GameplayInterface>();
		panels.add(interfaceElementsFactory("map_frame",0.0f,0.0f, "left"));
		panels.add(interfaceElementsFactory("middle_frame",width*0.5f + width*0.05f*scale,0.0f,"center"));
		panels.add(interfaceElementsFactory("menu_frame",width, 0.0f, "right"));
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
	
	private GameplayInterface interfaceElementsFactory(
			String regionName, 
			float positionX, 
			float positionY,
			String align) {
		RigidBodyModel model = new RigidBodyModel();
		model = panelsPolygons.get(regionName+".png");
		region = textureAtlas.findRegion(regionName);
		Sprite sprite = new Sprite(region);
		return new GameplayInterface(align, positionX, positionY, scale, sprite, model);
	}
	
	public void render(Batch batch){
		if (!panels.isEmpty()) {
			this.panels.forEach(value->value.draw(batch,1));
		}
	}
	
	public void drawDebug (ShapeRenderer shapes) {
		shapes.setColor(Color.CYAN);
		if (!panels.isEmpty()) {
			this.panels.forEach(value->value.drawDebug(shapes));
		}
	}

	public void dispose() {
		AtlasPm.dispose();
		CoursorPm.dispose();
		textureAtlas.dispose();
		region.getTexture().getTextureData().disposePixmap();
	}
}
