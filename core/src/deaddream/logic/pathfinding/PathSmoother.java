package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.PathFinderQueue;
import com.badlogic.gdx.ai.pfa.PathSmootherRequest;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.TimeUtils;

public class PathSmoother<N, V extends Vector<V>> {

	RaycastCollisionDetector<V> raycastCollisionDetector;
	Ray<V> ray;

	/** Creates a {@code PathSmoother} using the given {@link RaycastCollisionDetector}
	 * @param raycastCollisionDetector the raycast collision detector */
	public PathSmoother (RaycastCollisionDetector<V> raycastCollisionDetector) {
		this.raycastCollisionDetector = raycastCollisionDetector;
	}

	/** Smoothes the given path in place.
	 * @param path the path to smooth
	 * @return the number of nodes removed from the path. */
	public int smoothPath (SmoothableGraphPath<N, V> path) {
		int inputPathLength = path.getCount();

		// If the path is two nodes long or less, then we can't smooth it
		if (inputPathLength <= 2) return 0;

		// Make sure the ray is instantiated
		if (this.ray == null) {
			V vec = path.getNodePosition(0);
			this.ray = new Ray<V>(vec.cpy(), vec.cpy());
		}

		// Keep track of where we are in the smoothed path.
		// We start at 1, because we must always include the start node in the smoothed path.
		int outputIndex = 1;

		// Keep track of where we are in the input path
		// We start at 2, because we assume two adjacent
		// nodes will pass the ray cast
		int inputIndex = 2;

		// Loop until we find the last item in the input
		while (inputIndex < inputPathLength) {
			// Set the ray
			ray.start.set(path.getNodePosition(outputIndex - 1));
			ray.end.set(path.getNodePosition(inputIndex));

			// Do the ray cast
			boolean collides = raycastCollisionDetector.collides(ray);

			if (collides) {
				// The ray test failed, swap nodes and consider the next output node
				path.swapNodes(outputIndex, inputIndex - 1);
				outputIndex++;
			}

			// Consider the next input node
			inputIndex++;
		}

		// Reached the last input node, always add it to the smoothed path.
		path.swapNodes(outputIndex, inputIndex - 1);
		path.truncatePath(outputIndex + 1);

		// Return the number of removed nodes
		return inputIndex - outputIndex - 1;
	}

	/** Smoothes in place the path specified by the given request, possibly over multiple consecutive frames.
	 * @param request the path smoothing request
	 * @param timeToRun the time in nanoseconds that this call can use on the current frame
	 * @return {@code true} if this operation has completed; {@code false} if more time is needed to complete. */
	public boolean smoothPath (PathSmootherRequest<N, V> request, long timeToRun) {

		long lastTime = TimeUtils.nanoTime();

		SmoothableGraphPath<N, V> path = request.path;
		int inputPathLength = path.getCount();

		// If the path is two nodes long or less, then we can't smooth it
		if (inputPathLength <= 2) return true;

		if (request.isNew) {
			request.isNew = false;

			// Make sure the ray is instantiated
			if (this.ray == null) {
				V vec = request.path.getNodePosition(0);
				this.ray = new Ray<V>(vec.cpy(), vec.cpy());
			}

			// Keep track of where we are in the smoothed path.
			// We start at 1, because we must always include the start node in the smoothed path.
			request.outputIndex = 1;

			// Keep track of where we are in the input path
			// We start at 2, because we assume two adjacent
			// nodes will pass the ray cast
			request.inputIndex = 2;

		}

		// Loop until we find the last item in the input
		while (request.inputIndex < inputPathLength) {

			// Check the available time
			long currentTime = TimeUtils.nanoTime();
			timeToRun -= currentTime - lastTime;
			if (timeToRun <= PathFinderQueue.TIME_TOLERANCE) return false;

			// Set the ray
			ray.start.set(path.getNodePosition(request.outputIndex - 1));
			ray.end.set(path.getNodePosition(request.inputIndex));

			// Do the ray cast
			boolean collided = raycastCollisionDetector.collides(ray);

			if (collided) {
				// The ray test failed, swap nodes and consider the next output node
				path.swapNodes(request.outputIndex, request.inputIndex - 1);
				request.outputIndex++;
			}

			// Consider the next input node
			request.inputIndex++;

			// Store the current time
			lastTime = currentTime;
		}

		// Reached the last input node, always add it to the smoothed path
		path.swapNodes(request.outputIndex, request.inputIndex - 1);
		path.truncatePath(request.outputIndex + 1);

		// Smooth completed
		return true;
	}
}