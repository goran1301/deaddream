package deaddream.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dd.DeadDream;

import deaddream.backgrounds.BackgroundInterface;
import deaddream.camera.CameraManager;
import deaddream.maps.MapManager;
import deaddream.players.Player;
import deaddream.units.Unit;
import deaddream.units.factories.UnitFactory;
import deaddream.units.utilities.input.InputManager;
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
		
		inputManager = new InputManager();
		shaderProgrammer = new ShaderProgrammer();
		gameUtilities.font.getData().setScale(1);
		
		mapManager = new MapManager(map, tmr);
		camera = new CameraManager(gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT);
		unitFactory = new UnitFactory(gameUtilities);
	}
	
	public void setBg(BackgroundInterface bg) {
		this.bg = bg;
	}
	
	public void update(float delta) {
		gameUtilities.shapeRenderer.setProjectionMatrix(gameUtilities.camera.combined);
		gameUtilities.batch.setProjectionMatrix(gameUtilities.camera.combined);
		bg.updateCameraPosition(gameUtilities.camera.position.x, gameUtilities.camera.position.y);
		camera.update(gameUtilities.camera);
		world.step(1/60f, 6, 2);
		stage.act(delta);
		updateInput();
		mapManager.tmr.setView(gameUtilities.camera);
		shaderProgrammer.update(players);
	}
	
	private void updateInput() {
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
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameUtilities.batch.end();
		if (bg != null) {
			bg.render();
		}
		mapManager.render();
		stage.draw();
	}
	
	public UnitFactory getUnitFactory() {
		return unitFactory;
	}
}
