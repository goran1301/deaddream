package deaddream.logic.pathfinding;

import com.badlogic.gdx.utils.Array;

public class PathCollitionDetector<N extends Node<N>> {
	
	BaseIndexedGraph<N> worldMap;
	
	public PathCollitionDetector(BaseIndexedGraph<N> worldMap) {
		this.worldMap = worldMap;
	}
	
	public boolean detect(N node) {
		for (N areasNode : findArea(node)) {
			if (areasNode == null) {
				return true;
			}
			if (areasNode.type != Node.TILE_FLOOR) {
				return true;
			}
		}
		return false;
	}
	
	public Array<N> findArea(N node) {
		//System.out.println("node's x:y  " + String.valueOf(node.x) + " : " + String.valueOf(node.y));
		int numberOfNeededTiles = getNumberOfNeededTiles(node);
		int xOffsetStart = node.x - numberOfNeededTiles;
		//System.out.println("numberOfNeededTiles: " + String.valueOf(numberOfNeededTiles));
		if (xOffsetStart < 0) {
			xOffsetStart = 0;
		}
		if (xOffsetStart > worldMap.sizeX) {
			xOffsetStart = worldMap.sizeX;
		}
		int yOffsetStart = node.y - numberOfNeededTiles;
		if (yOffsetStart < 0) {
			yOffsetStart = 0;
		}
		if (yOffsetStart > worldMap.sizeY) {
			yOffsetStart = worldMap.sizeY;
		}
		int xOffsetEnd = node.x + numberOfNeededTiles;
		int yOffsetEnd = node.y + numberOfNeededTiles;
		//System.out.println("xOffsetStart: " + String.valueOf(xOffsetStart));
		//System.out.println("xOffsetEnd: " + String.valueOf(xOffsetEnd));
		//System.out.println("yOffsetStart: " + String.valueOf(yOffsetStart));
		//System.out.println("yOffsetEnd: " + String.valueOf(yOffsetEnd));
		Array<N> area = new Array<N>(numberOfNeededTiles * numberOfNeededTiles * 4);
		
		for (int i = xOffsetStart; i < xOffsetEnd; i++) {
			for (int j = yOffsetStart; j < yOffsetEnd; j++) {
				area.add(worldMap.getNode(i, j));
			}
		}
		
		return area;
	}
	
	public int getNumberOfNeededTiles(N node) {
		//System.out.println("unit width: " + String.valueOf(worldMap.unitSize.x) + " - unit height: " + String.valueOf(worldMap.unitSize.x));
		int tiledUnitHeight = (int)Math.ceil(worldMap.unitSize.y / worldMap.pixelNodeSizeY)/2;
		int tiledUnitWidth = (int)Math.ceil(worldMap.unitSize.x / worldMap.pixelNodeSizeX)/2;
		if (tiledUnitHeight >= tiledUnitWidth) {
			//System.out.println("tiledUnitHeight: " + String.valueOf(tiledUnitHeight));
			return tiledUnitHeight;
		}
		//System.out.println("tiledUnitWidth: " + String.valueOf(tiledUnitWidth));
		return tiledUnitWidth;
	}
}
