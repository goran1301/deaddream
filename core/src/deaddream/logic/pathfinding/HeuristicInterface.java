package deaddream.logic.pathfinding;

public interface HeuristicInterface<N> {

	/** Calculates an estimated cost to reach the goal node from the given node.
	 * @param node the start node
	 * @param endNode the end node
	 * @return the estimated cost */
	public float estimate (N node, N endNode);
}
