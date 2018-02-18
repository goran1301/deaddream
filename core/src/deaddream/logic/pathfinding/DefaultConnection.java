package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class DefaultConnection<N> implements Connection<N> {
	
	protected N fromNode;
	protected N toNode;
	protected float cost = 1;
	
	public DefaultConnection (N fromNode, N toNode) {
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	@Override
	public float getCost() {
		return cost;
	}

	@Override
	public N getFromNode() {
		return fromNode;
	}

	@Override
	public N getToNode() {
		return toNode;
	}

}
