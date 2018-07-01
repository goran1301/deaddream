package deaddream.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraManager {
    
	private Vector3 cursorPosition = new Vector3(0f, 0f, 0f);
	
	private Vector3 cameraPosition = new Vector3(0f, 0f, 0f);

    private float screenWidth;
    
    private float screenHeight;

    public CameraManager(float screenWidth, float screenHeigth) {
    	this.screenWidth = screenWidth;
    	this.screenHeight = screenHeigth;
    }

    public void update(OrthographicCamera camera) {
    	updatePosition(camera);
    	updateCameraPosition(camera);
    	camera.position.set(cameraPosition);
    }

	private void updatePosition(OrthographicCamera camera) {
		cursorPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}
	
	private void updateCameraPosition(OrthographicCamera camera) {
		float cameraX = camera.position.x;
		float cameraY = camera.position.y;
		if (cursorPosition.x >= cameraX + screenWidth / 2 - 5){
			cameraX += 10f;
		}
		if (cursorPosition.x <= cameraX - screenWidth / 2 + 5){
			cameraX -= 10f;
		}
		if (cursorPosition.y >= cameraY + screenHeight / 2 - 5){
			cameraY += 10f;
		}
		if (cursorPosition.y <= cameraY - screenHeight / 2 + 5){
			cameraY -= 10f;
		}
		
		cameraPosition.x = cameraX;
		cameraPosition.y = cameraY;
	}
	
	public Vector3 getCameraPosition() {
		return cameraPosition;
	}
	
	public Vector3 getCursorPosition() {
		return cursorPosition;
	}
}
