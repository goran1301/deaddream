package deaddream.units.utilities.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import deaddream.logic.pathfinding.BaseIndexedGraph;
import deaddream.logic.pathfinding.TiledNode;
import deaddream.logic.pathfinding.TiledSmoothableGraphPath;

public class BaseGraphDebugRenderer {
	
	BaseIndexedGraph<TiledNode> graph;
	
	Color allowColor;
	
	Color disallowColor;
	
	Batch batch;
	
	
	public BaseGraphDebugRenderer (BaseIndexedGraph<TiledNode> graph, Batch batch) {
		allowColor = Color.GREEN;
		disallowColor = Color.RED;
		this.graph = graph;
		this.batch = batch;
	}
	
	
	public void render(ShapeRenderer renderer, BitmapFont font) 
	{	
		//renderer.begin(ShapeType.Line);
		for (int x = 0; x < graph.getSizeX(); x++) {
			int idx = x * graph.getSizeY();
			for (int y = 0; y < graph.getSizeY(); y++) {
				renderNode(graph.getNode(idx + y), renderer, font);
			}
		}
		//renderer.end();
		batch.begin();
		writeWeights(font);
		batch.end();	
	}
	
	public void renderNode(TiledNode node, ShapeRenderer renderer, BitmapFont font) {
		if (node.type == TiledNode.TILE_WALL) {
			this.renderNode(node, renderer, disallowColor, font);
		} else {
			this.renderNode(node, renderer, allowColor, font);
		}
	}
	
	public void renderNode(TiledNode node, ShapeRenderer renderer, Color color, BitmapFont font) {
		
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
	
	private void writeWeights(BitmapFont font)
	{
		for (int x = 0; x < graph.getSizeX(); x++) {
			int idx = x * graph.getSizeY();
			for (int y = 0; y < graph.getSizeY(); y++) {
				TiledNode node = graph.getNode(idx + y);
				font.draw(
						batch, String.valueOf(node.getWeight()),
						node.x * graph.getPixelNodeSizeX(),
				        (node.y + 0.5f) * graph.getPixelNodeSizeY()
				    );
			}
		}
	}
	
	public void renderPath(TiledSmoothableGraphPath<TiledNode> path, ShapeRenderer renderer, BitmapFont font) {
		renderer.begin(ShapeType.Line);
		//System.out.println(String.valueOf(path.nodes.size));
		for (int i = 0; i < path.nodes.size; i++) {
			//System.out.println("render path tile" + String.valueOf(i));
			renderNode(path.nodes.get(i), renderer, Color.BLUE, font);
		}
		renderer.end();
	}
	
}
