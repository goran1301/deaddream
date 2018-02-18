package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.utils.Array;

public class IndexedGraph<N extends Node> implements Graph<N> {

	public final int sizeX;
	public final int sizeY;
	
	public IndexedGraph(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	@Override
	public Array<Connection<N>> getConnections(N fromNode) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
