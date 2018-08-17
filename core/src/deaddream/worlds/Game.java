package deaddream.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dd.DeadDream;
import deaddream.backgrounds.BackgroundInterface;
import deaddream.camera.CameraManager;
import deaddream.groupmove.GroupMoveController;
import deaddream.maps.MapManager;
import deaddream.players.Player;
import deaddream.rendering.GameplayInterfaceRenderer;
import deaddream.rendering.SelectionRenderer;
import deaddream.units.Unit;
import deaddream.units.factories.UnitFactory;
import deaddream.units.utilities.input.InputManager;
import deaddream.units.utilities.map.BaseGraphDebugRenderer;
import deaddream.worlds.rendering.ShaderProgrammer;

public class Game {
	protected Player currentPlayer;
	protected Array<Player> players;
	protected Stage stage;
	protected MapManager mapManager;
	protected InputManager inputManager;
	protected BackgroundInterface bg;
	public World world;
	protected DeadDream gameUtilities;
	protected ShaderProgrammer shaderProgrammer;
	public Group unitGroup;
	protected UnitFactory unitFactory;
	protected CameraManager camera;
	private BaseGraphDebugRenderer graphDebugRenderer;
	private GroupMoveController groupMoveController;
	private GameplayInterfaceRenderer Interface;
	private Matrix4 screenMatrix;

	
	public Game(
			DeadDream utilities, 
			Array<Player> players, 
			Player currentPlayer,
			TiledMap map,
			OrthogonalTiledMapRenderer tmr
		) {
		this.players = players;
		this.currentPlayer = currentPlayer;
		gameUtilities = utilities;
		world = new World(new Vector2(0, 0), false);
		stage = new Stage(
			new StretchViewport(
				gameUtilities.V_WIDTH, 
				gameUtilities.V_HEIGHT, 
				gameUtilities.camera
			),
		gameUtilities.batch);
		unitGroup = new Group();
		stage.addActor(unitGroup);
		Gdx.input.setInputProcessor(this.stage);
		gameUtilities.shapeRenderer.setProjectionMatrix(gameUtilities.camera.combined);
		screenMatrix = new Matrix4(gameUtilities.batch.getProjectionMatrix().setToOrtho2D(0, 0, gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT));
		this.Interface = new GameplayInterfaceRenderer(gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT);
		Interface.show();
		
		inputManager = new InputManager();
		shaderProgrammer = new ShaderProgrammer();
		gameUtilities.font.getData().setScale(1);
		
		mapManager = new MapManager(map, tmr);
		camera = new CameraManager(gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT);
		
		graphDebugRenderer = new BaseGraphDebugRenderer(mapManager.pathFinder.graph, gameUtilities.batch);
		groupMoveController = new GroupMoveController(currentPlayer);
		unitFactory = new UnitFactory(gameUtilities, groupMoveController);
	}
	
	public void setBg(BackgroundInterface bg) {
		this.bg = bg;
	}
	
	public void update(float delta) {
		
		System.out.println("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
		GdxAI.getTimepiece().update(delta);
		//System.out.println("CURRENT AI TIME: " + String.valueOf(GdxAI.getTimepiece().getTime()));
		groupMoveController.update();

		world.step(1/60f, 6, 2);
		stage.act(delta);
		world.step(1/60f, 6, 2);
		bg.updateCameraPosition(gameUtilities.camera.position.x, gameUtilities.camera.position.y);
		gameUtilities.shapeRenderer.setProjectionMatrix(gameUtilities.camera.combined);
		gameUtilities.batch.setProjectionMatrix(gameUtilities.camera.combined);
		mapManager.tmr.setView(gameUtilities.camera);
		shaderProgrammer.update(players);
		camera.update(gameUtilities.camera);
		updateInput();
		
	}
	
	private void updateInput() {
		//long before = TimeUtils.nanoTime();
		inputManager.update(camera.getCursorPosition(), currentPlayer);
		//long after = TimeUtils.nanoTime();
		//System.out.println("CLICK TOOK= "+((after-before)/1000 ) +" MILLIS" + " FPS " + Gdx.graphics.getFramesPerSecond());
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
			for(Unit unit : currentPlayer.getSelection().getSelected()) {
				if (unit.getPlayer() == currentPlayer) {
					Array<Vector2> path = mapManager.pathFinder.getPath(unit, camera.getCursorPosition());
					if (path != null) {
						unit.moveTo(path);
					}
				}
			}
		}
	}
	
	public void render(float delta) {
		gameUtilities.batch.begin();
		gameUtilities.batch.enableBlending();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (bg != null) {
			bg.render(gameUtilities.batch);
		}
		//gameUtilities.batch.end();
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			graphDebugRenderer.render(gameUtilities.shapeRenderer, gameUtilities.font);
		}
		//gameUtilities.batch.begin();
		beginShapeRenderer();
		SelectionRenderer.render(currentPlayer.getSelection(), gameUtilities.shapeRenderer);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			groupMoveController.render(gameUtilities.shapeRenderer);
		}
		inputManager.render(gameUtilities.shapeRenderer);
		gameUtilities.shapeRenderer.end();	
		gameUtilities.batch.end();
		stage.draw();
		gameUtilities.batch.begin();
		gameUtilities.batch.setProjectionMatrix(screenMatrix);
		//UI
		
		Interface.render(gameUtilities.batch);
		gameUtilities.batch.end();
		mapManager.render();
			
	}
	
	private void beginShapeRenderer() {
		gameUtilities.shapeRenderer.setAutoShapeType(true);
		gameUtilities.shapeRenderer.begin();
	}
	
	public UnitFactory getUnitFactory() {
		return unitFactory;
	}
	public void dispose() {
		Interface.dispose();
	}
}