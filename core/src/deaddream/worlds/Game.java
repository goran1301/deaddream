package deaddream.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import deaddream.network.LockstepPrecessor;
import deaddream.players.LocalPlayer;
import deaddream.players.Player;
import deaddream.rendering.HUDRenderer;
import deaddream.rendering.SelectionRenderer;
import deaddream.units.factories.UnitFactory;
import deaddream.units.utilities.input.InputManager;
import deaddream.units.utilities.input.OnlineInputManager;
import deaddream.units.utilities.input.commandhandlers.CommandHandler;
import deaddream.units.utilities.input.commandhandlers.GroupSelectionCommandHandler;
import deaddream.units.utilities.input.commandhandlers.MoveCommandHandler;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.map.BaseGraphDebugRenderer;
import deaddream.worlds.rendering.ShaderProgrammer;

public class Game {
	protected LocalPlayer currentPlayer;
	protected Array<Player> players;
	protected Stage stage;
	protected Stage interfaceStage;
	protected MapManager mapManager;
	protected InputManager inputManager;
	protected BackgroundInterface bg;
	public World world;
	protected DeadDream gameUtilities;
	protected ShaderProgrammer shaderProgrammer;
	public Group unitGroup;
	
	protected UnitFactory unitFactory;
	protected CameraManager camera;
	protected BaseGraphDebugRenderer graphDebugRenderer;
	protected GroupMoveController groupMoveController;
	protected HUDRenderer HUD;
	protected Matrix4 screenMatrix;
	protected Array<CommandHandler<?>> commandHandlers;
	protected OnlineInputManager onlineInputManager;
	protected OrthographicCamera HUDCamera;
	protected StretchViewport HUDViewport;
	protected InputMultiplexer multiplexer;
	protected BaseCommandInterface currentCommand;
	
	protected LockstepPrecessor lockstepPrecessor;
	
	protected float choosedDelta;
	protected boolean updateLogic = true;
	
	public Game(
			DeadDream utilities, 
			Array<Player> players, 
			LocalPlayer currentPlayer,
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
		//Gdx.input.setInputProcessor(this.stage);
		gameUtilities.shapeRenderer.setProjectionMatrix(gameUtilities.camera.combined);
		this.HUDCamera = new OrthographicCamera();
		this.HUDCamera.setToOrtho(false, gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT);
		HUDViewport = new StretchViewport(gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT, HUDCamera);		
		screenMatrix = new Matrix4(gameUtilities.batch.getProjectionMatrix().cpy().setToOrtho2D(0, 0, gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT));
		gameUtilities.batch.setProjectionMatrix(screenMatrix);
		interfaceStage = new Stage(HUDViewport,gameUtilities.batch);
		this.HUD = new HUDRenderer(this.interfaceStage);
		HUD.addToStage(interfaceStage);
		//Gdx.input.setInputProcessor(this.interfaceStage);
		HUD.show();
		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(this.multiplexer);
		multiplexer.addProcessor(new InputAdapter () {
			@Override
			   public boolean touchDown (int x, int y, int pointer, int button) {
				HUD.showCursor(true);
			      return false;
			   }

			   @Override
			   public boolean touchUp (int x, int y, int pointer, int button) {
			      HUD.showCursor(false);
			      return false;
			   }
		}
		);
		multiplexer.addProcessor(interfaceStage);
		multiplexer.addProcessor(stage);
		
		
		shaderProgrammer = new ShaderProgrammer();
		gameUtilities.font.getData().setScale(1);
		
		mapManager = new MapManager(map, tmr);
		camera = new CameraManager(gameUtilities.V_WIDTH, gameUtilities.V_HEIGHT);
		
		graphDebugRenderer = new BaseGraphDebugRenderer(mapManager.pathFinder.graph, gameUtilities.batch);
		groupMoveController = new GroupMoveController(players);
		unitFactory = new UnitFactory(gameUtilities, groupMoveController);
		

		initCommandHandlers();
		inputManager = (InputManager)currentPlayer.getController();
		onlineInputManager = new OnlineInputManager(players);
		lockstepPrecessor = new LockstepPrecessor(onlineInputManager);
	}
	
	public void setBg(BackgroundInterface bg) {
		this.bg = bg;
	}
	
	public void update(float delta) {

		System.out.println("CURRENT STEP " + lockstepPrecessor.getFrameId());
		if (updateLogic){
			//System.out.println("DELTA IS " + String.valueOf(delta));
			InputManager inputManager = (InputManager)currentPlayer.getController();
			inputManager.updateDelta(delta);
			//System.out.println("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
			GdxAI.getTimepiece().update(choosedDelta);
			//System.out.println("CURRENT AI TIME: " + String.valueOf(GdxAI.getTimepiece().getTime()));
			groupMoveController.update();
			
			world.step(1/60f, 6, 2);
			stage.act(choosedDelta);
		}
		//world.step(1/60f, 6, 2);
		bg.updateCameraPosition(gameUtilities.camera.position.x, gameUtilities.camera.position.y);
		gameUtilities.shapeRenderer.setProjectionMatrix(gameUtilities.camera.combined);
		gameUtilities.batch.setProjectionMatrix(gameUtilities.camera.combined);
		mapManager.tmr.setView(gameUtilities.camera);
		shaderProgrammer.update(players);
		camera.update(gameUtilities.camera);
		
	}
	
	protected void initCommandHandlers() {
		commandHandlers = new Array<CommandHandler<?>>();
		commandHandlers.add(new MoveCommandHandler(mapManager));
		commandHandlers.add(new GroupSelectionCommandHandler());
	}
	
	public void updateRemoteInput(Array<byte[]> remoteCommands) {
		lockstepPrecessor.updateRemote(remoteCommands);
		BaseCommandInterface[] stepCommands = lockstepPrecessor.getStepCommands();
		if (stepCommands == null) {
			updateLogic = false;
			return;
		}
		updateLogic = true;
		chooseDelta(stepCommands);
		for (BaseCommandInterface command : stepCommands){
			for (CommandHandler<?> commandHandler : commandHandlers) {
				commandHandler.handle(command);
			}
		}
	}
	
	public void updateLocalInput(BaseCommandInterface localCommand) {
		lockstepPrecessor.updateLocal(localCommand);
	}
	
	public byte[] getCommandsForPlayer(int playerId) {
		return lockstepPrecessor.getCommandsForPlayer(playerId);
	}
	
	public BaseCommandInterface updateLocalPlyerInput() {
		currentPlayer.getController().update(camera.getCursorPosition());
		BaseCommandInterface command = currentPlayer.getController().getCommand();
		return command;
	}
	
	//public void execute
	
	
	public void render(float delta) {
		gameUtilities.batch.begin();
		gameUtilities.batch.enableBlending();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (bg != null) {
			bg.render(gameUtilities.batch);
		}
		gameUtilities.batch.end();
		mapManager.render();
		stage.draw();
		
		
		beginShapeRenderer();
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			graphDebugRenderer.render(gameUtilities.shapeRenderer, gameUtilities.font);
		}
		SelectionRenderer.render(currentPlayer.getSelection(), gameUtilities.shapeRenderer);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			groupMoveController.render(gameUtilities.shapeRenderer);
		} 
		inputManager.render(gameUtilities.shapeRenderer);
		gameUtilities.shapeRenderer.end();
		//UI
		gameUtilities.batch.setProjectionMatrix(screenMatrix);
		interfaceStage.draw();	
		gameUtilities.shapeRenderer.setProjectionMatrix(screenMatrix);
		beginShapeRenderer();
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			HUD.drawDebug(gameUtilities.shapeRenderer);
		} 
		gameUtilities.shapeRenderer.end();
	}
	
	private void beginShapeRenderer() {
		gameUtilities.shapeRenderer.setAutoShapeType(true);
		gameUtilities.shapeRenderer.begin();
	}
	
	public UnitFactory getUnitFactory() {
		return unitFactory;
	}
	public void dispose() {
		HUD.dispose();
		interfaceStage.dispose();
	}
	
	public void chooseDelta(BaseCommandInterface[] commands) {
		float delta = commands[0].getDelta();
		for (BaseCommandInterface command : commands) {
			if (command.getDelta() > delta ) {
				delta = command.getDelta();
			}
		}
		choosedDelta = delta;
	}
}
