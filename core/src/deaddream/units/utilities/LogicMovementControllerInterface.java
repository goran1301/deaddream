package deaddream.units.utilities;

public interface LogicMovementControllerInterface <P, V> extends MovementControllerInterface <P> {
	public boolean isMoving();
	
	public float getOrientation();
	
	public void addIntermediatePosition(V point);
	
	public V getVelocity();
}
