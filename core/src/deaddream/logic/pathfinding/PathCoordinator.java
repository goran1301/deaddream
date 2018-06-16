package deaddream.logic.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.Constants;

import deaddream.logic.pathfinding.TiledSmoothableGraphPath;
import deaddream.logic.pathfinding.TiledNode;

public class PathCoordinator {
	public static Array<Vector2> getCoordinatesPath(TiledSmoothableGraphPath<TiledNode> path, float finalX, float finalY, int tileSizeX, int tileSizeY, boolean isEven) {
		Array<Vector2> coordinatesPath = null;
		if (path.nodes.size > 0){
			coordinatesPath = new Array<Vector2>(path.nodes.size);
			for (int i = 0; i < path.nodes.size - 1; i++) {
				if (i == 0) {
					continue;
				}
				TiledNode node = path.nodes.get(i);
				float x;
				float y;
				if (!isEven) {
					x = (node.x * tileSizeX + tileSizeX / 2) / Constants.PPM;
					y = (node.y * tileSizeY + tileSizeY / 2) / Constants.PPM;
				} else {
					x = (node.x * tileSizeX + tileSizeX) / Constants.PPM;
					y = (node.y * tileSizeY + tileSizeY) / Constants.PPM;
				}
				coordinatesPath.add(new Vector2(x, y));
			}
			coordinatesPath.add(new Vector2(finalX / Constants.PPM, finalY / Constants.PPM));
		}
		return coordinatesPath;
	}
}
