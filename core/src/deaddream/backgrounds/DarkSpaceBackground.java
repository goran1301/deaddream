package deaddream.backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DarkSpaceBackground implements BackgroundInterface {
	
	Texture background;
	
	SpriteBatch batch;
	
	float x;
	
	float y;
	
	float width;
	
	float height;
	
	public DarkSpaceBackground(Texture background, SpriteBatch batch) {
		this.background = background;
		this.batch = batch;
	}

	@Override
	public void updateCameraPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void render() {
		batch.begin();
		batch.enableBlending();
		batch.draw(background, x - width / 2, y - height / 2, width, height);
		batch.end();
	}

	@Override
	public void setResolution(float width, float height) {
	    this.width = width;
	    this.height = height;
	}
}
