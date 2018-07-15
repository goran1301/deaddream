package deaddream.backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldBackground implements BackgroundInterface{
	
    Texture background;
    
    Texture middleLayer;
	
	//SpriteBatch batch;
	
	float x;
	
	float y;
	
	float width;
	
	float height;
	
	public WorldBackground(Texture background, Texture middleLayer) {
		this.background = background;
		this.middleLayer = middleLayer;
		//this.batch = batch;
	}
	
	@Override
	public void updateCameraPosition(float x, float y) {
		this.x = x;
		this.y = y;
		
	}

	@Override
	public void setResolution(float width, float height) {
		this.width = width;
	    this.height = height;
	}

	@Override
	public void render(SpriteBatch batch) {
		//batch.begin();
		//batch.enableBlending();
		batch.draw(background, x - width / 2, y - height / 2);
		batch.draw(middleLayer, (x - width / 2) * 0.85f, (y - height / 2) * 0.85f);
		//batch.end();
	}

}
