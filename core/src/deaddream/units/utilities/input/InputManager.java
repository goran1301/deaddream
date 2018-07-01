package deaddream.units.utilities.input;

import com.badlogic.gdx.math.Vector3;

public class InputManager {
	
	private Vector3 cursorPosition;
	
	public void updatePosition(Vector3 position) {
		cursorPosition = position;
	}
	
	public Vector3 getCursorPosition() {
		return cursorPosition;
	}
}
