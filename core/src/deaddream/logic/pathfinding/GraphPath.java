package deaddream.logic.pathfinding;

public interface GraphPath<N> extends Iterable<N> {

	/** Returns the number of items of this path. */
	public int getCount ();

	/** Returns the item of this path at the given index. */
	public N get (int index);

	/** Adds an item at the end of this path. */
	public void add (N node);

	/** Clears this path. */
	public void clear ();

	/** Reverses this path. */
	public void reverse ();

}
