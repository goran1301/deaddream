package deaddream.logic.pathfinding;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.Constants;

import deaddream.logic.pathfinding.TiledRaycastCollisionDetector;
import deaddream.players.Player;
import deaddream.units.Unit;

/**
 * Game pathfinding class
 * 
 * @author goran
 *
 */
public class PathFinder {
    private IndexedAStarPathFinder<TiledNode> pathFinder;
	public BaseIndexedGraph<TiledNode> graph;
	private PathSmoother<TiledNode, Vector2> pathSmoother;
    private TiledManhattanDistance<TiledNode> heuristic;
	private TiledSmoothableGraphPath<TiledNode> path;
	
	/**
	 * Constructor
	 * 
	 * @param map Game map
	 */
	public PathFinder(TiledMap map) {
		heuristic = new TiledManhattanDistance<TiledNode>();
		path = new TiledSmoothableGraphPath<TiledNode>();
		graph = MapBaseIndexedGraphFactory.create(map);
		pathFinder = new IndexedAStarPathFinder<TiledNode>(graph, true);
        pathSmoother = new PathSmoother<TiledNode, Vector2>(new TiledRaycastCollisionDetector<TiledNode>(graph));
	}
	
	/**
	 * Finds a path for one unit
	 * 
	 * @param unit a unit requested a path
	 * @param destination a destination point
	 * @return
	 */
	public Array<Vector2> getPath(Unit unit, Vector3 destination) {
		path.clear();
		graph.startNode = graph.getNodeByCoordinates(
			unit.getBody().getPosition().x * Constants.PPM,
			unit.getBody().getPosition().y * Constants.PPM
		);
		graph.unitSize.x = unit.getWidth();
		graph.unitSize.y = unit.getHeight();
		//System.out.println(String.valueOf(unit.getBody().getPosition().x) + " : " + String.valueOf(unit.getBody().getPosition().y));
		TiledNode endNode = graph.getNodeByCoordinates(destination.x, destination.y);
		if (endNode != null) {
			graph.setBodyWeight(unit.getWeight());
			//System.out.println("Unit WEIGHT" + String.valueOf(graph.getBodyWeight()));
			pathFinder.searchNodePath(graph.startNode, endNode, heuristic, path);
			pathSmoother.smoothPath(path);
			return PathCoordinator.getCoordinatesPath(path, destination.x, destination.y, graph.getPixelNodeSizeX(), graph.getPixelNodeSizeY(), graph.getBodyWeight() % 2 == 0);
		}
		return null;
	}
	
	public void update(Array<Player> players) {
		for (Player player : players) {
			for (Unit unit : player.getUnits()) {
				TiledNode node = graph.getNodeByCoordinates(
						unit.getBody().getPosition().x * Constants.PPM,
						unit.getBody().getPosition().y * Constants.PPM
					);
				int weight = 0;
				if (node != null) {
					weight = node.getWeight();
				}
				unit.setPositionWeight(weight);
			}
		}
	}
	
}
