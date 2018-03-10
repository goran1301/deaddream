package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class BaseIndexedGraph<N extends Node<N>> implements IndexedGraph<N> {

	protected final int sizeX;
	protected final int sizeY;
	public boolean diagonal;
	public N startNode;
	protected int pixelNodeSizeX;
	protected int pixelNodeSizeY;
	protected Array<N> nodes; 
	
	public BaseIndexedGraph(int sizeX, int sizeY, Array<N> nodes, int pixelNodeSizeX, int pixelNodeSizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.nodes = nodes;
		this.pixelNodeSizeX = pixelNodeSizeX;
		this.pixelNodeSizeY = pixelNodeSizeY;
		this.diagonal = false;
		this.startNode = null;
		addConnections();
	}
	
	protected void addConnections() {
		for (int x = 0; x < sizeX; x++) {
			int idx = x * sizeY;
			for (int y = 0; y < sizeY; y++) {
				N n = nodes.get(idx + y);
				if (x > 0){
					addConnection(n, -1, 0);
				}
				if (y > 0){
					addConnection(n, 0, -1);
				}
				if (x < sizeX - 1){
					addConnection(n, 1, 0);
				}
				if (y < sizeY - 1){
					addConnection(n, 0, 1);
				}
				/*if (y > 0 && x > 0) {
					addConnection(n, -1, -1);
				}
				if (y < sizeY-1 && x < sizeX-1) {
					addConnection(n, 1, 1);
				}
				if (y > 0 && x < sizeX-1) {
					addConnection(n, 1, -1);
				}
				if (y < sizeY-1 && x > 0) {
					addConnection(n, -1, 1);
				}*/
			}
		}
	}
	
	@Override
	public Array<Connection<N>> getConnections(N fromNode) {
		return fromNode.getConnections();
	}

	@Override
	public int getIndex(N node) {
		return node.getIndex();
	}

	@Override
	public int getNodeCount() {
		return nodes.size;
	}
	
	public N getNode(int x, int y) {
		return nodes.get(x * sizeY + y);
	}
	
	public N getNode(int index) {
		return nodes.get(index);
	}
	
	private void addConnection (N n, int xOffset, int yOffset) {
		N target = getNode(n.x + xOffset, n.y + yOffset);
		if (target.type == N.TILE_FLOOR) {
			Array<Connection<N>> connections = n.getConnections();
		    connections.add(new DefaultConnection<N>(this, n, target));
		    n.setConnections(connections);
		}
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public N getNodeByCoordinates(float xCoordinate, float yCoordinate) {
		int x = (int) Math.floor(xCoordinate / pixelNodeSizeX);
		int y = (int) Math.floor(yCoordinate / pixelNodeSizeY);
		return this.getNode(x, y);
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public int getPixelNodeSizeX() {
		return pixelNodeSizeX;
	}
	
	public int getPixelNodeSizeY() {
		return pixelNodeSizeY;
	}
	
}
