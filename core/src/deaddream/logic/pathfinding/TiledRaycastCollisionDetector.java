package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

public class TiledRaycastCollisionDetector<N extends Node<N>> implements RaycastCollisionDetector<Vector2> {

	BaseIndexedGraph<N> worldMap;
	PathCollitionDetector<N> pathCollisionDetector;
	
	public TiledRaycastCollisionDetector (BaseIndexedGraph<N> worldMap) {
		this.worldMap = worldMap;
		pathCollisionDetector = new PathCollitionDetector<N>(worldMap);
	}

	// See http://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	@Override
	public boolean collides (Ray<Vector2> ray) {
		int x0 = (int)ray.start.x;
		int y0 = (int)ray.start.y;
		int x1 = (int)ray.end.x;
		int y1 = (int)ray.end.y;

		int tmp;
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if (steep) {
			// Swap x0 and y0
			tmp = x0;
			x0 = y0;
			y0 = tmp;
			// Swap x1 and y1
			tmp = x1;
			x1 = y1;
			y1 = tmp;
		}
		if (x0 > x1) {
			// Swap x0 and x1
			tmp = x0;
			x0 = x1;
			x1 = tmp;
			// Swap y0 and y1
			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		int deltax = x1 - x0;
		int deltay = Math.abs(y1 - y0);
		int error = 0;
		int y = y0;
		int ystep = (y0 < y1 ? 1 : -1);
		for (int x = x0; x <= x1; x++) {
			N tile = steep ? worldMap.getNode(y, x) : worldMap.getNode(x, y);
			//if (tile.type != TiledNode.TILE_FLOOR) return true; // We've hit a wall
			//if (tile.type != TiledNode.TILE_FLOOR || collitionPossibility(tile)) {
			if (tile.type != TiledNode.TILE_FLOOR || pathCollisionDetector.detect(tile) || collitionPossibility(tile)) {
			    return true;
			}
			error += deltay;
			if (error + error >= deltax) {
				y += ystep;
				error -= deltax;
			}
		}

		return false;
	}
	
	public boolean collitionPossibility(N node) {
		N rightTopDiagonal = worldMap.getNode(node.x + 1, node.y + 1);
		if (rightTopDiagonal == null) {
			return true;
		}else if (rightTopDiagonal.type != TiledNode.TILE_FLOOR) {
			return true;
		}
		N leftTopDiagonal = worldMap.getNode(node.x - 1, node.y + 1);
		if (leftTopDiagonal == null) {
			return true;
		}else if (leftTopDiagonal.type != TiledNode.TILE_FLOOR) {
			return true;
		}
		N rightBottomDiagonal = worldMap.getNode(node.x + 1, node.y - 1);
		if (rightBottomDiagonal == null) {
			return true;
		}else if (rightBottomDiagonal.type != TiledNode.TILE_FLOOR) {
			return true;
		}
		N leftBottomDiagonal = worldMap.getNode(node.x - 1, node.y - 1);
		if (leftBottomDiagonal == null) {
			return true;
		}else if (leftBottomDiagonal.type != TiledNode.TILE_FLOOR) {
			return true;
		}
		return false;
	}

	@Override
	public boolean findCollision (Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
		throw new UnsupportedOperationException();
	}

}
