package deaddream.logic.pathfinding;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class MapBaseIndexedGraphFactory {
	
	public static BaseIndexedGraph<TiledNode> create(TiledMap map) {
		MapProperties properties = map.getProperties();
		int mapWidth = properties.get("width", Integer.class);
		int mapHeight = properties.get("height", Integer.class);
		int tilePixelWidth = properties.get("tilewidth", Integer.class);
		int tilePixelHeight = properties.get("tileheight", Integer.class);
		
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer)map.getLayers().get(0);
		
		Array<TiledNode> nodes = new Array<TiledNode>(mapWidth * mapHeight);
		
		int[][] mapMatrix = new int[mapWidth][mapHeight];
		
		//default data. All nodes is TILE_FLOOR type
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				mapMatrix[x][y] = Node.TILE_FLOOR;
				if (collisionLayer.getCell(x, y) == null){
					//System.out.println("NO CELL"); 

					continue;
				}
				TiledMapTile tile = collisionLayer.getCell(x, y).getTile();
				if (tile != null) {
					if (collisionLayer.getCell(x, y).getTile().getProperties().containsKey("blocked")){
						//collisionLayer.getCell(x, y).getTile().getProperties().get("blocked");
						mapMatrix[x][y] = Node.TILE_WALL;
						//System.out.println("WALL");
					} 
				}
				
			}
		}
		
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				nodes.add(new TiledNode(x, y, mapHeight, mapMatrix[x][y], 4));
			}
		}
		
		BaseIndexedGraph<TiledNode> graph = new BaseIndexedGraph<TiledNode>(mapWidth, mapHeight, nodes, tilePixelWidth, tilePixelHeight);
		/*for (MapObject object : collisionLayer.getObjects()) {
			if (object instanceof PolylineMapObject) {
				
				((PolylineMapObject) object).getPolyline()
			}else{
				continue;
			}
		}*/
		
		return graph;
	}
	
}
