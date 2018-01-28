package deaddream.units.utilities;

/**
 * Movement interface
 * 
 * @author goran
 *
 */
public interface MovementControllerInterface {
	
	/**
	 * Move to point
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(float x, float y);
	
	/**
	 * stop movement to point
	 */
	public void stopMove();
	
	
	/**
	 * Rotate to angle
	 * 
	 * @param angle degrees
	 */
	public void rotateTo(float angle);
	
	/**
	 * Stop rotation
	 */
	public void stopRotation();
	
}
