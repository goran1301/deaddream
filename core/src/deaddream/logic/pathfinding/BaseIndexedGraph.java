package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BaseIndexedGraph<N extends Node<N>> implements IndexedGraph<N> {

	protected final int sizeX;
	protected final int sizeY;
	public boolean diagonal;
	public N startNode;
	protected int pixelNodeSizeX;
	protected int pixelNodeSizeY;
	protected Array<N> nodes; 
	public Vector2 unitSize = new Vector2();
	protected int bodyWeight = 0;
	
	public void setBodyWeight(int weight) {
		this.bodyWeight = weight;
	}
	
	public int getBodyWeight() {
		return bodyWeight;
	}
	
	public BaseIndexedGraph(int sizeX, int sizeY, Array<N> nodes, int pixelNodeSizeX, int pixelNodeSizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.nodes = nodes;
		this.pixelNodeSizeX = pixelNodeSizeX;
		this.pixelNodeSizeY = pixelNodeSizeY;
		this.diagonal = false;
		this.startNode = null;
		addConnections();
		addWeights();
	}
	
	//todo to complete
	protected void addWeights()
	{
		for (int i = 0; i < nodes.size; i++) {
			N node = nodes.get(i);
			if (node.type != Node.TILE_FLOOR) {
				node.setWeight(0);
				continue;
			}
			int evenWeight = evenStrategy(node);
			int oddWeight = oddStrategy(node);
			if (evenWeight > oddWeight) {
				node.setWeight(evenWeight);
			}else{
				node.setWeight(oddWeight);
			}
		}
	}
	
	protected int evenStrategy(N node)
	{
		//System.out.println("node position: x " + String.valueOf(node.x) + " y " + String.valueOf(node.y));
		boolean complete = false;
		int index = 0;
		int currentIndex = index + 2;
		int xOffsetStart = 0;
		int xOffsetEnd = 0;
		int yOffsetStart = 0;
		int yOffsetEnd = 0;
		int half = 0;
		while (!complete) {
			//System.out.println("even " + String.valueOf(currentIndex));
			half = currentIndex / 2;
			xOffsetEnd = node.x + half;
			xOffsetStart = node.x - (half - 1);
			yOffsetEnd = node.y + half;
			yOffsetStart = node.y - (half - 1);
			//System.out.println("X offset start " + String.valueOf(xOffsetStart) + "X offset end " + String.valueOf(xOffsetEnd) + "Y offset start " + String.valueOf(yOffsetStart) + "Y offset end " + String.valueOf(yOffsetEnd));
			
			//System.out.println("map size x: " + sizeX + " y: " + sizeY);
			
			if (xOffsetStart < 0 || xOffsetEnd < 0 || yOffsetStart < 0 || yOffsetEnd < 0) {
				complete = true;
			}
			if (xOffsetStart >= this.sizeX || xOffsetEnd >= this.sizeX || yOffsetStart >= this.sizeY || yOffsetEnd >= this.sizeY) {
				complete = true;
			}
			for (int i = xOffsetStart; i <= xOffsetEnd; i++) {
				N topNode = this.getNode(i, yOffsetStart);
				N bottomNode = this.getNode(i, yOffsetEnd);
				if (topNode == null || bottomNode == null) {
					complete = true;
					//System.out.println("even complete " + String.valueOf(index));
					break;
				}
				if (topNode.type != Node.TILE_FLOOR || bottomNode.type != Node.TILE_FLOOR) {
					complete = true;
					//System.out.println("even complete " + String.valueOf(index));
					break;
				}
			}
			for (int i = yOffsetStart; i <= yOffsetEnd; i++) {
				N leftNode = this.getNode(xOffsetStart, i);
				N rightNode = this.getNode(xOffsetEnd, i);
				if (leftNode == null || rightNode == null) {
					complete = true;
					//System.out.println("even complete " + String.valueOf(index));
					break;
				}
				if (leftNode.type != Node.TILE_FLOOR || rightNode.type != Node.TILE_FLOOR) {
					complete = true;
					//System.out.println("even complete " + String.valueOf(index));
					break;
				}
			}
			if (complete) {
				//System.out.println("even complete FINISHED " + String.valueOf(index));
				break;
			}
			index = currentIndex;
			currentIndex += 2;
		}
		return index;
	}
	
	protected int oddStrategy(N node)
	{
		//System.out.println("node position: x " + String.valueOf(node.x) + " y " + String.valueOf(node.y));
		boolean complete = false;
		int index = 1;
		int currentIndex = 3;
		int xOffsetStart = 0;
		int xOffsetEnd = 0;
		int yOffsetStart = 0;
		int yOffsetEnd = 0;
		int half = 0;
		while (!complete) {
			//System.out.println("odd " + String.valueOf(currentIndex));
			half = (int) Math.ceil(currentIndex / 2);
			xOffsetStart = node.x - half;
			xOffsetEnd = node.x + half;
			yOffsetStart = node.y - half;
			yOffsetEnd = node.y + half;
			//System.out.println("X offset start " + String.valueOf(xOffsetStart) + "X offset end " + String.valueOf(xOffsetEnd) + "Y offset start " + String.valueOf(yOffsetStart) + "Y offset end " + String.valueOf(yOffsetEnd));
			if (xOffsetStart < 0 || xOffsetEnd < 0 || yOffsetStart < 0 || yOffsetEnd < 0) {
				complete = true;
			}
			if (xOffsetStart >= sizeX || xOffsetEnd >= sizeX || yOffsetStart >= sizeY || yOffsetEnd >= sizeY) {
				complete = true;
			}
			for (int i = xOffsetStart; i <= xOffsetEnd; i++) {
				N topNode = this.getNode(i, yOffsetStart);
				N bottomNode = this.getNode(i, yOffsetEnd);
				if (topNode == null || bottomNode == null) {
					complete = true;
					//System.out.println("odd complete " + String.valueOf(index));
					break;
				}
				if (topNode.type != Node.TILE_FLOOR || bottomNode.type != Node.TILE_FLOOR) {
					complete = true;
					//System.out.println("odd complete" + String.valueOf(index));
					break;
				}
			}
			for (int i = yOffsetStart; i <= yOffsetEnd; i++) {
				N leftNode = this.getNode(xOffsetStart, i);
				N rightNode = this.getNode(xOffsetEnd, i);
				if (leftNode == null || rightNode == null) {
					complete = true;
					//System.out.println("odd complete " + String.valueOf(index));
					break;
				}
				if (leftNode.type != Node.TILE_FLOOR || rightNode.type != Node.TILE_FLOOR) {
					complete = true;
					//System.out.println("odd complete " + String.valueOf(index));
					break;
				}
			}
			if (complete) {
				//System.out.println("odd complete, FINISHED " + String.valueOf(index));
				break;
			}
			index = currentIndex;
			currentIndex += 2;
		}
		return index;
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
		if (x * sizeY + y >= nodes.size || x * sizeY + y < 0) {
			return null;
		}
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
