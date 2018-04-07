package deaddream.logic.pathfinding;

import com.badlogic.gdx.math.Vector;

public interface SmoothableGraphPath<N, V extends Vector<V>> extends GraphPath<N> {

	/** Returns the position of the node at the given index.
	 * @param index the index of the node you want to know the position */
	public V getNodePosition (int index);

	/** Swaps the specified nodes of this path.
	 * @param index1 index of the first node to swap
	 * @param index2 index of the second node to swap */
	public void swapNodes (int index1, int index2);

	/** Reduces the size of this path to the specified length (number of nodes). If the path is already smaller than the specified
	 * length, no action is taken.
	 * @param newLength the new length */
	public void truncatePath (int newLength);

}
