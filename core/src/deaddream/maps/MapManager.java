package deaddream.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import deaddream.logic.pathfinding.PathFinder;
import deaddream.players.Player;
import deaddream.units.Unit;
import deaddream.units.utilities.map.BaseGraphDebugRenderer;

/**
 * Provides operations with map
 * 
 * @author goran
 *
 */
public class MapManager {
    public OrthogonalTiledMapRenderer tmr;
	public TiledMap map;
	public PathFinder pathFinder;
	
	
	public MapManager(TiledMap map, OrthogonalTiledMapRenderer tmr) {
		this.map = map;
		this.tmr = tmr;
		pathFinder = new PathFinder(map);
		
	}
	
	public PathFinder getPathFinder() {
		return pathFinder;
	}
	
	public void render() {
		tmr.render();
	}
	
	public void dispose() {
		tmr.dispose();
		map.dispose();
	}
	
	
}
