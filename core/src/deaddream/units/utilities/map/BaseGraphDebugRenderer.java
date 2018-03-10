package deaddream.units.utilities.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import deaddream.logic.pathfinding.BaseIndexedGraph;
import deaddream.logic.pathfinding.TiledNode;
import deaddream.logic.pathfinding.TiledSmoothableGraphPath;

public class BaseGraphDebugRenderer {
	
	BaseIndexedGraph<TiledNode> graph;
	
	Color allowColor;
	
	Color disallowColor;
	
	
	public BaseGraphDebugRenderer (BaseIndexedGraph<TiledNode> graph) {
		allowColor = Color.GREEN;
		disallowColor = Color.RED;
		this.graph = graph;
	}
	
	
	public void render(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Line);
		for (int x = 0; x < graph.getSizeX(); x++) {
			int idx = x * graph.getSizeY();
			for (int y = 0; y < graph.getSizeY(); y++) {
				renderNode(graph.getNode(idx + y), renderer);
			}
		}
		renderer.end();
	}
	
	public void renderNode(TiledNode node, ShapeRenderer renderer) {
		if (node.type == TiledNode.TILE_WALL) {
			this.renderNode(node, renderer, disallowColor);
		} else {
			this.renderNode(node, renderer, allowColor);
		}
	}
	
	public void renderNode(TiledNode node, ShapeRenderer renderer, Color color) {
		
		renderer.setColor(color);
		//top horizontal line
		renderer.line(node.x * graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY(),
				node.x * graph.getPixelNodeSizeX() + graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY()
			);
		// bottom horizontal line
		renderer.line(node.x * graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY() + graph.getPixelNodeSizeY(),
				node.x * graph.getPixelNodeSizeX() + graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY() + graph.getPixelNodeSizeY()
			);
		// left vertical line
		renderer.line(node.x * graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY(),
				node.x * graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY() + graph.getPixelNodeSizeY()
			);
		// right vertical line
		renderer.line(node.x * graph.getPixelNodeSizeX() + graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY(),
				node.x * graph.getPixelNodeSizeX() + graph.getPixelNodeSizeX(),
				node.y * graph.getPixelNodeSizeY() + graph.getPixelNodeSizeY()
			);
	}
	
	public void renderPath(TiledSmoothableGraphPath<TiledNode> path, ShapeRenderer renderer) {
		renderer.begin(ShapeType.Line);
		//System.out.println(String.valueOf(path.nodes.size));
		for (int i = 0; i < path.nodes.size; i++) {
			//System.out.println("render path tile" + String.valueOf(i));
			renderNode(path.nodes.get(i), renderer, Color.BLUE);
		}
		renderer.end();
	}
	
}
