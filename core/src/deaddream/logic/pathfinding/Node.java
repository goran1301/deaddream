package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node {
	/** A constant representing an empty tile */
	public static final int TILE_EMPTY = 0;

	/** A constant representing a walkable tile */
	public static final int TILE_FLOOR = 1;

	/** A constant representing a wall */
	public static final int TILE_WALL = 2;

	/** The x coordinate of this tile */
	public final int x;

	/** The y coordinate of this tile */
	public final int y;

	/** The type of this tile, see {@link #TILE_EMPTY}, {@link #TILE_FLOOR} and {@link #TILE_WALL} */
	public final int type;

	protected Array<Connection<Node>> connections;

	public Node (int x, int y, int type, Array<Connection<Node>> connections) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.connections = connections;
	}

	public int getIndex () {
		//return x * IndexedGraph.sizeY + y;
		return 0;
	}

	public Array<Connection<Node>> getConnections () {
		return this.connections;
	}
}
