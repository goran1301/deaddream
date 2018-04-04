package deaddream.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dd.Constants;
import com.mygdx.dd.DeadDream;
import deaddream.logic.pathfinding.BaseIndexedGraph;
import deaddream.logic.pathfinding.MapBaseIndexedGraphFactory;
import deaddream.logic.pathfinding.PathCoordinator;
import deaddream.logic.pathfinding.TiledManhattanDistance;
import deaddream.logic.pathfinding.TiledNode;
import deaddream.logic.pathfinding.TiledSmoothableGraphPath;
import deaddream.maps.TiledObjectUtil;
import deaddream.units.Protector;
import deaddream.units.Stone;
import deaddream.units.Unit;
import deaddream.units.utilities.map.BaseGraphDebugRenderer;
import deaddream.units.utilities.map.MothershipDebugRenderer;
import deaddream.units.UCMothership;

public class GameScreen implements Screen {
	
	private DeadDream game;
	
	private World world;
	
	private Unit unit00, unit01, stone, UCMothership;
	
	private Box2DDebugRenderer b2ddr;
	
	private Matrix4 debugMatrix;
	
	private Texture background;
	
	public OrthogonalTiledMapRenderer tmr;
	
	public TiledMap map;
	
	private IndexedAStarPathFinder<TiledNode> pathFinder;
	
	private BaseIndexedGraph<TiledNode> graph;
	
	private PathSmoother<TiledNode, Vector2> pathSmoother;
	
	private Stage stage;
	
	private Unit selectedUnit;
	
	private BaseGraphDebugRenderer graphDebugRenderer;
	
	private MothershipDebugRenderer msDebugRenderer;
	
	TiledManhattanDistance<TiledNode> heuristic = new TiledManhattanDistance<TiledNode>();
	
	TiledSmoothableGraphPath<TiledNode> path = new TiledSmoothableGraphPath<TiledNode>();
	
	
	public GameScreen(final DeadDream game) {
		this.game = game;
		this.world = new World(new Vector2(0, 0), false);
		this.b2ddr = new Box2DDebugRenderer();
		this.debugMatrix = this.game.camera.combined.cpy();//new Matrix4(this.game.camera.combined.cpy());
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		this.stage = new Stage(new StretchViewport(this.game.V_WIDTH, this.game.V_HEIGHT, this.game.camera));
	}	

	@Override
	public void show() {
		System.out.println("Game");
		Gdx.input.setInputProcessor(this.stage);
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		map = new TmxMapLoader().load("maps/test2.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		this.loadTextures();
		Texture protecterTexture = game.assets.get("skins/units/protector.png", Texture.class);
		protecterTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture stoneTexture = game.assets.get("skins/units/stone.png", Texture.class);
		stoneTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture UCMothershipTexture = game.assets.get("skins/units/ucmothership.png", Texture.class);
		UCMothershipTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.unit00 = new Protector(this.world, new Sprite(protecterTexture), 23f, 23f, 0.0f);
		this.unit01 = new Protector(this.world, new Sprite(protecterTexture), 35f, 40f, 1f);
		this.stone = new Stone(this.world, new Sprite(stoneTexture), 25f, 25f, 1f);
		UCMothership mc = new UCMothership(world, new Sprite(UCMothershipTexture), 40f, 40f, 1f);
		this.UCMothership = mc;
		msDebugRenderer = new MothershipDebugRenderer(mc);
		MapObjects objects =  map.getLayers().get("collision-layer").getObjects();
		TiledObjectUtil.parseTiledObjectLayer(world, objects);
		graph = MapBaseIndexedGraphFactory.create(map);
		graphDebugRenderer = new BaseGraphDebugRenderer(graph);
		
		Group group = new Group();
		group.addActor(UCMothership);
		group.addActor(unit00);
		group.addActor(unit01);
		
		//stage.addActor(UCMothership);
		//stage.addActor(unit00);
		//stage.addActor(unit01);
		stage.addActor(group);
		stage.addActor(stone);
		
		unit00.setZIndex(1);
		unit01.setZIndex(2);
		UCMothership.setZIndex(0);
		
		UCMothership.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("UCMothership selected" + String.valueOf(UCMothership.getZIndex()));
				selectUnit(UCMothership);
			}
		});
		
		unit00.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Unit00 selected" + String.valueOf(unit00.getZIndex()));
				selectUnit(unit00);
			}
		});
		
		unit01.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Unit01 selected" + String.valueOf(unit01.getZIndex()));
				selectUnit(unit01);
			}
		});
		
		
		
		
		
		/*this.buttonExit.addListener(new ClickListener(){
			@Override
			public void clicked(rendererInputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});*/
        pathFinder = new IndexedAStarPathFinder<TiledNode>(graph, true);
        pathSmoother = new PathSmoother<TiledNode, Vector2>(new deaddream.logic.pathfinding.TiledRaycastCollisionDetector<TiledNode>(graph));
        

	}
	
	private void selectUnit(Unit unit) {
		selectedUnit = unit;
	}
	
	private void loadTextures() {
		this.background = game.assets.get("backgrounds/bg1.jpg", Texture.class);
		this.background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//this.testUnitSkin = game.assets.get("skins/units/protector.png", Texture.class);
		//this.background = new Image(background);
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		beginBatch();
		renderBackground();
		game.batch.end();
		tmr.render();
		stage.draw();
		
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			graphDebugRenderer.render(game.shapeRenderer);
			if (selectedUnit != null) {
				game.shapeRenderer.begin(ShapeType.Line);
				TiledNode node = graph.getNodeByCoordinates(selectedUnit.getBody().getPosition().x * Constants.PPM,
						selectedUnit.getBody().getPosition().y * Constants.PPM);
				graphDebugRenderer.renderNode(node, game.shapeRenderer, Color.BLUE);
				game.shapeRenderer.end();
			}
			graphDebugRenderer.renderPath(path, game.shapeRenderer);
			msDebugRenderer.render(delta, game.shapeRenderer);
		}
		
	}
	
	private void beginBatch() {
		this.game.batch.begin();
		this.game.batch.enableBlending();
		
	}
	
	private void renderBackground() {
		
		this.game.batch.draw(this.background, 
				this.game.camera.position.x - this.game.V_WIDTH / 2, 
				this.game.camera.position.y - this.game.V_HEIGHT / 2,
                this.game.V_WIDTH, this.game.V_HEIGHT
                );
	}
	
	public void update(float delta) {
		
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
				
		world.step(1/60f, 6, 2);
		stage.act(delta);
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		
		this.updateInput();
		
		float cameraX = this.game.camera.position.x;
		float cameraY = this.game.camera.position.y;
		if (tmp.x >= cameraX + game.V_WIDTH / 2 - 5){
			cameraX += 10f;
		}
		if (tmp.x <= cameraX - game.V_WIDTH / 2 + 5){
			cameraX -= 10f;
		}
		if (tmp.y >= cameraY + game.V_HEIGHT / 2 - 5){
			cameraY += 10f;
		}
		if (tmp.y <= cameraY - game.V_HEIGHT / 2 + 5){
			cameraY -= 10f;
		}
		
		this.cameraUpdate(cameraX, cameraY);
		tmr.setView(game.camera);
		
	}
	
	
	
	private void updateInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
		{
			
		    Vector3 tmp = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		    if (selectedUnit != null) {
		    	path.clear();
				graph.startNode = graph.getNodeByCoordinates(
						selectedUnit.getBody().getPosition().x * Constants.PPM,
						selectedUnit.getBody().getPosition().y * Constants.PPM
					);
				graph.unitSize.x = selectedUnit.getWidth();
				graph.unitSize.y = selectedUnit.getHeight();
				
				TiledNode endNode = graph.getNodeByCoordinates(tmp.x, tmp.y);
				if (endNode != null) {
					pathFinder.searchNodePath(graph.startNode, endNode, heuristic, path);
					pathSmoother.smoothPath(path);
					selectedUnit.moveTo(PathCoordinator.getCoordinatesPath(path, tmp.x, tmp.y, graph.getPixelNodeSizeX(), graph.getPixelNodeSizeY()));
				}
		    }
		}
	}
	
	public void setCameraToMeters(float x, float y) {
		cameraUpdate(x * Constants.PPM, y * Constants.PPM);
	}
	
	public void cameraUpdate(float x, float y) {
		Vector3 position = this.game.camera.position;
		position.x = x;
		position.y = y;
		game.camera.position.set(position);
		this.debugMatrix = this.game.camera.combined.cpy();
		this.debugMatrix.scale(Constants.PPM, Constants.PPM, 0.0f);
		game.camera.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		this.background.dispose();
		this.stage.dispose();
		world.dispose();
		b2ddr.dispose();
		map.dispose();
		tmr.dispose();
		// TODO Auto-generated method stub
		
	}

}
