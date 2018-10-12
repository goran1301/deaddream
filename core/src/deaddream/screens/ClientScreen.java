package deaddream.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.DeadDream;

import deaddream.backgrounds.WorldBackground;
import deaddream.network.UDPClient;
import deaddream.players.LocalPlayer;
import deaddream.players.OnlinePlayer;
import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.worlds.NetworkGame;

public class ClientScreen implements Screen{

    private NetworkGame game;
	
	private DeadDream gameUtilities;
	
	private UDPClient client;

	private boolean successCommandExchange;
	
	private BaseCommandInterface currentLocalCommand;
	
	
	public ClientScreen(DeadDream utils) {
		this.gameUtilities = utils;
		
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		LocalPlayer currentPlayer = new LocalPlayer(1, Player.inGameStatus); 
		OnlinePlayer onlinePLayer = new OnlinePlayer(0, Player.inGameStatus);
		Array<Player> players = new Array<Player>();
		players.add(onlinePLayer);
		players.add(currentPlayer);
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
			game.getUnitFactory().createProtector(game.world, 23f, 40f, game.unitGroup, currentPlayer);
		}
		for (int i = 0; i < 99; i++) {
			game.getUnitFactory().createProtector(game.world, 23f, 23f, game.unitGroup, onlinePLayer);
		}
		try {
			client = new UDPClient();
			client.startReceive();
			//client.makeTestDataTransfer(null);
		} catch(Exception e) {
			System.out.println("no client init");
		}
		successCommandExchange = true;
		
				//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		if (successCommandExchange) {
			System.out.println("CLIENT UPDATE GAME LOGIC");
			
			
			game.clearCommands();
			currentLocalCommand = game.updateLocalPlyerInput();
			//successCommandExchange = false;
		}
		game.update(delta, successCommandExchange);
		game.render(delta);
		System.out.println("CLIENT currentLocalCommand frame " + String.valueOf(currentLocalCommand.getFrameId()));
		//game.render(delta);
		if (client != null) {
			try{
				//System.out.println("Client update");
				Array<byte[]> jsonCommands = client.makeTestDataTransfer(currentLocalCommand);
				if (jsonCommands.size > 0){
					
					game.updateInput(jsonCommands, currentLocalCommand);
					successCommandExchange = true;
					//System.out.println("CLIENT SUCCESS INPUT UPDATE");
				} else {
					successCommandExchange = false;
				}
				
			} catch (Exception e) {
				 e.printStackTrace();
			}
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
