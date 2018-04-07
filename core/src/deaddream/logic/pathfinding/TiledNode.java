package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class TiledNode extends Node<TiledNode>{

	public TiledNode(int x, int y, int sizeY, int type, int connections) {
		super(x, y, sizeY, type, new Array<Connection<TiledNode>>(connections));
		// TODO Auto-generated constructor stub
	}

}
