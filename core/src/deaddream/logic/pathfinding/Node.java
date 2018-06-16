package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node <N extends Node<N>> {
	/** A constant representing an empty tile */
	public static final int TILE_EMPTY = 0;

	/** A constant representing a walkable tile */
	public static final int TILE_FLOOR = 1;

	/** A constant representing a wall */
	public static final int TILE_WALL = 2;
	
	/** A weight which define the coast of tile */
	private int weight;

	/** The x coordinate of this tile */
	public final int x;

	/** The y coordinate of this tile */
	public final int y;

	/** The type of this tile, see {@link #TILE_EMPTY}, {@link #TILE_FLOOR} and {@link #TILE_WALL} */
	public final int type;
	
	public final int sizeY;

	protected Array<Connection<N>> connections;

	public Node (int x, int y, int sizeY, int type, Array<Connection<N>> connections) {
		this.x = x;
		this.y = y;
		this.sizeY = sizeY;
		this.type = type;
		this.connections = connections;
	}
	
	public void setWeight(int weight)
	{
		this.weight = weight;
	}
	
	public int getWeight()
	{
		return weight;
	}

	public int getIndex () {
		return x * sizeY + y;
	}

	public Array<Connection<N>> getConnections () {
		return this.connections;
	}
	
	public void setConnections (Array<Connection<N>> connections) {
		this.connections = connections;
	}
}
