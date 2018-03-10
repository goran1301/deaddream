package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class TiledManhattanDistance<N extends Node<N>> implements Heuristic<N> {
	public TiledManhattanDistance () {
	}

	@Override
	public float estimate (N node, N endNode) {
		//return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
		//return (float)Math.sqrt((endNode.x - node.x) * (endNode.x - node.x) + (endNode.y - node.y) * (endNode.y - node.y));
		return (float)(endNode.x - node.x) * (endNode.x - node.x) + (endNode.y - node.y) * (endNode.y - node.y);
	}
}
