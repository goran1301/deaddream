package deaddream.backgrounds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface BackgroundInterface {
	
	public void updateCameraPosition(float x, float y);
	public void setResolution(float width, float height);
	public void render(SpriteBatch batch);
	
}
