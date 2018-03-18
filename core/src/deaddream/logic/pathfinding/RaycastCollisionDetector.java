package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

public interface RaycastCollisionDetector<T extends Vector<T>> {
	/** Casts the given ray to test if it collides with any objects in the game world.
	 * @param ray the ray to cast.
	 * @return {@code true} in case of collision; {@code false} otherwise. */
	public boolean collides (Ray<T> ray);

	/** Find the closest collision between the given input ray and the objects in the game world. In case of collision,
	 * {@code outputCollision} will contain the collision point and the normal vector of the obstacle at the point of collision.
	 * @param outputCollision the output collision.
	 * @param inputRay the ray to cast.
	 * @return {@code true} in case of collision; {@code false} otherwise. */
	public boolean findCollision (Collision<T> outputCollision, Ray<T> inputRay);
}
