package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class DefaultConnection<N extends Node<N>> implements Connection<N> {
	
	protected N fromNode;
	protected N toNode;
	protected BaseIndexedGraph<N> worldMap;
	PathCollitionDetector<N> pathCollisionDetector;
	
	static final float NON_DIAGONAL_COST = (float)Math.sqrt(2);
	static final float DIAGONAL_COST = 0.0f;
	static final float UNREACHEBLE_COST = 5000f;
	
	public DefaultConnection (BaseIndexedGraph<N> worldMap, N fromNode, N toNode) {
		this.worldMap = worldMap;
		this.fromNode = fromNode;
		this.toNode = toNode;
		pathCollisionDetector = new PathCollitionDetector<N>(worldMap);
	}
	
	protected float getDiogonalCost(boolean diagonal) {
		if (diagonal) {
			return DIAGONAL_COST;
		} else {
			return NON_DIAGONAL_COST;
		}
	}

	@Override
	public float getCost() {
		int bodyWeight = worldMap.getBodyWeight();
		if (bodyWeight > toNode.getWeight()) {
			return (float)UNREACHEBLE_COST * (bodyWeight - toNode.getWeight());
		}
		
		if (worldMap.diagonal) return 1f;
		//worldMap.diagonal = !worldMap.diagonal;
		return getToNode().x != worldMap.startNode.x
				&& getToNode().y != worldMap.startNode.y
				? NON_DIAGONAL_COST : 1f;

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
