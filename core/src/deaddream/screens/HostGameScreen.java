package deaddream.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.DeadDream;

import deaddream.backgrounds.WorldBackground;
import deaddream.network.UDPServer;
import deaddream.players.LocalPlayer;
import deaddream.players.OnlinePlayer;
import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.worlds.NetworkGame;

public class HostGameScreen implements Screen {
	
    private NetworkGame game;
	
	private DeadDream gameUtilities;
	
	private UDPServer server;
	
	private boolean successCommandExchange;
	
	private BaseCommandInterface currentLocalCommand;
	
	public HostGameScreen(DeadDream utils) {
		this.gameUtilities = utils;
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		LocalPlayer currentPlayer = new LocalPlayer(0, Player.inGameStatus); 
		OnlinePlayer onlinePlayer = new OnlinePlayer(1, Player.inGameStatus);
		Array<Player> players = new Array<Player>();
		players.add(currentPlayer);
		players.add(onlinePlayer);
		TiledMap map = new TmxMapLoader().load("maps/test2.tmx");
		OrthogonalTiledMapRenderer tmr = new OrthogonalTiledMapRenderer(map);
		game = new NetworkGame(gameUtilities, players, currentPlayer, map, tmr);
		
		WorldBackground bg = new WorldBackground(
				gameUtilities.assets.get("backgrounds/world_background/stars.png", Texture.class),
				gameUtilities.assets.get("backgrounds/world_background/middle_layer.png", Texture.class)
			);
		bg.setResolution(gameUtilities.camera.viewportWidth, gameUtilities.camera.viewportHeight);
		game.setBg(bg);
		for (int i = 0; i < 99; i++) {
			game.getUnitFactory().createProtector(game.world, 23f, 40f, game.unitGroup, onlinePlayer);
		}
		for (int i = 0; i < 99; i++) {
			game.getUnitFactory().createProtector(game.world, 23f, 23f, game.unitGroup, currentPlayer);
		}
		try{
			server = new UDPServer();
			//server.receiveTestData(null);
		} catch (Exception e) {
			System.out.println("no server: " + e.getMessage());
		}
		successCommandExchange = true;
		
		//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		if (successCommandExchange) {
			//System.out.println("HOST UPDATE GAME LOGIC");
			game.update(delta);
			game.render(delta);
			game.clearCommands();
			currentLocalCommand = game.updateLocalPlyerInput();
			successCommandExchange = false;
		}
		//game.render(1/60);
		//System.out.println("currentLocalCommand frame " + String.valueOf(currentLocalCommand.getFrameId()));
		
		try{
			Array<String> jsonCommands = server.receiveTestData(currentLocalCommand);
			if (jsonCommands.size > 0) {
				game.updateInput(jsonCommands, currentLocalCommand);
				successCommandExchange = true;
				//System.out.println("HOST SUCCESS INPUT UPDATE");
			} else {
				successCommandExchange = false;
			}
			
		} catch (Exception e) {
			System.out.println("no server: " + e.getMessage());
		}
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
		game.dispose();
	}
	
}
