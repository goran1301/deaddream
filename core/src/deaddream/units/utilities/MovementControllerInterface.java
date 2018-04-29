package deaddream.units.utilities;

/**
 * Movement interface
 * 
 * @author goran
 *
 */
public interface MovementControllerInterface <P> {
	
	/**
	 * Move to point
	 * 
	 * @param x
	 * @param y
	 */
	//public void moveTo(float x, float y);
	
	/**
	 * Move to point
	 * 
	 * @param move to by path
	 */
	public void moveTo(P path);
	
	/**
	 * stop movement to point
	 */
	public void stopMove();
	
	
	/**
	 * Rotate to angle
	 * 
	 * @param angle degrees
	 */
	//public void rotateTo(float angle);
	
	/**
	 * Stop rotation
	 */
	//public void stopRotation();
	
	/**
	 * update movement logic
	 * 
	 * @param delta
	 */
	public void update(float delta);
	
}
