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
import deaddream.worlds.NetworkGame;

public class HostGameScreen implements Screen {
	
    private NetworkGame game;
	
	private DeadDream gameUtilities;
	
	private UDPServer server;
	
	public HostGameScreen(DeadDream utils) {
		this.gameUtilities = utils;
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		Player currentPlayer = new LocalPlayer(0, Player.inGameStatus); 
		Player onlinePlayer = new OnlinePlayer(1, Player.inGameStatus);
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
			game.getUnitFactory().createProtector(game.world, 500f, 500f, game.unitGroup, onlinePlayer);
		}
		for (int i = 0; i < 99; i++) {
			game.getUnitFactory().createProtector(game.world, 23f, 23f, game.unitGroup, currentPlayer);
		}
		try{
			server = new UDPServer();
		} catch (Exception e) {
			System.out.println("no server: " + e.getMessage());
		}
		//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		game.update(1/60f);
		game.render(1/60f);
		game.clearCommands();
		try{
			Array<String> jsonCommands = server.receiveTestData(game.updateLocalPlyerInput());
			game.updateInput(jsonCommands);
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
