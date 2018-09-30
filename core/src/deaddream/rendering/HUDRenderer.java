package deaddream.rendering;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import aurelienribon.bodyeditor.BodyEditorLoader;
import aurelienribon.bodyeditor.BodyEditorLoader.RigidBodyModel;
import deaddream.gameplayMenu.HUD;

public class HUDRenderer {
	private TextureAtlas textureAtlas;
	private Pixmap AtlasPm;
	private Pixmap CoursorPm;
	private AtlasRegion region;
	private float scale = 1f;
	private ArrayList<HUD> panels;
	private Map<String, RigidBodyModel> panelsPolygons;
	
	public HUDRenderer(Stage stage) {
		textureAtlas = new TextureAtlas(Gdx.files.internal("GameplayInterfaceSpriteMap/SpriteMapConfiguration.atlas"));
		System.out.println("Interface Create");	
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("GameplayInterface/GUIPanelPolygons"));
		//System.out.println(loader.getInternalModel().rigidBodies.keySet().toArray()[0]);
		panelsPolygons = loader.getInternalModel().rigidBodies;
		//loader.getInternalModel().rigidBodies.keySet().toArray().length;
		//loader.getInternalModel().rigidBodies.get("map_frame.png");
		panels = new ArrayList<HUD>();
		panels.add(interfaceElementsFactory(stage, "map_frame",0.0f,0.0f, "left_bottom"));
		panels.add(interfaceElementsFactory(stage, "middle_frame",stage.getWidth()*0.5f + stage.getWidth()*0.05f*scale,0.0f,"center_bottom"));
		panels.add(interfaceElementsFactory(stage, "menu_frame",stage.getWidth(), 0.0f, "right_bottom"));
		panels.add(interfaceElementsFactory(stage, "atack_button",stage.getWidth() - stage.getWidth()*(1-0.919791667f)*scale, stage.getHeight() - stage.getHeight()*(1-0.12387037f*scale), "center_middle"));
		panels.add(interfaceElementsFactory(stage, "order_button",stage.getWidth() - stage.getWidth()*(1-0.919791667f)*scale, stage.getHeight() - stage.getHeight()*(1-0.151f*scale), "center_bottom"));
		panels.add(interfaceElementsFactory(stage, "cansell_button",stage.getWidth() - stage.getWidth()*(1-0.919791667f)*scale, stage.getHeight() - stage.getHeight()*(1-0.095f*scale), "center_top"));
		panels.add(interfaceElementsFactory(stage, "patrol_button",stage.getWidth() - stage.getWidth()*(1-0.886583334f)*scale, stage.getHeight() - stage.getHeight()*(1-0.1585f*scale), "center_middle"));
		panels.add(interfaceElementsFactory(stage, "button",stage.getWidth() - stage.getWidth()*(1-0.886583334f)*scale, stage.getHeight() - stage.getHeight()*(1-0.09f*scale), "center_middle"));
		panels.add(interfaceElementsFactory(stage, "repair_button",stage.getWidth() - stage.getWidth()*(1-0.953f)*scale, stage.getHeight() - stage.getHeight()*(1-0.1585f*scale), "center_middle"));
		panels.add(interfaceElementsFactory(stage, "button",stage.getWidth() - stage.getWidth()*(1-0.953f)*scale, stage.getHeight() - stage.getHeight()*(1-0.09f*scale), "center_middle"));
	}

	public void show() {
		showCursor(false);
	} 
	
	public void showCursor(boolean clicked) {
		region = clicked ? region = textureAtlas.findRegion("clicked_cursor") : textureAtlas.findRegion("cursor");
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
		
	private HUD interfaceElementsFactory(
			Stage stage,
			String regionName, 
			float positionX, 
			float positionY,
			String align) {
		RigidBodyModel model = new RigidBodyModel();
		model = panelsPolygons.get(regionName+".png");
		region = textureAtlas.findRegion(regionName);
		Sprite sprite = new Sprite(region);
		return new HUD(stage, align, positionX, positionY, scale, sprite, model);
	}
	
	public void render(Batch batch){
		if (!panels.isEmpty()) {
			this.panels.forEach(value->value.draw(batch,1));
		}
	}
	
	public void drawDebug (ShapeRenderer shapes) {
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

	public void addToStage(Stage stage) {
		if (!panels.isEmpty()) {
			this.panels.forEach(value->stage.addActor(value));
		}
	}
}
