package deaddream.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.DeadDream;

import deaddream.backgrounds.WorldBackground;
import deaddream.network.UDPClientTransmission;
import deaddream.players.Colors;
import deaddream.players.LocalPlayer;
import deaddream.players.OnlinePlayer;
import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.EmptyCommand;
import deaddream.worlds.NetworkGame;

public class ClientScreen implements Screen{

    private NetworkGame game;
	
	private DeadDream gameUtilities;
	
	private UDPClientTransmission client;
	
	public ClientScreen(DeadDream utils) {
		this.gameUtilities = utils;
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}
	
	@Override
	public void show() {
		LocalPlayer currentPlayer = new LocalPlayer(1, Player.inGameStatus, Colors.BLUE); 
		OnlinePlayer onlinePLayer = new OnlinePlayer(0, Player.inGameStatus, Colors.ORANGE);
		Array<Player> players = new Array<Player>();
		players.add(onlinePLayer);
		players.add(currentPlayer);
		TiledMap map = new TmxMapLoader().load("maps/test2.tmx");
		OrthogonalTiledMapRenderer tmr = new OrthogonalTiledMapRenderer(map);
		game = new NetworkGame(gameUtilities, players, currentPlayer, map, tmr, false);
		
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
			client = new UDPClientTransmission();
			client.startReceive();
			//client.makeTestDataTransfer(null);
		} catch(Exception e) {
			System.out.println("no client init");
			e.printStackTrace();
			
		}
		//successCommandExchange = true;
		
				//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		
			//System.out.println("CLIENT UPDATE GAME LOGIC");
		
			
			//game.clearCommands();
		
			//successCommandExchange = false;
		
		
		//System.out.println("CLIENT currentLocalCommand frame " + String.valueOf(currentLocalCommand.getFrameId()));
		//game.render(delta);
		if (client != null) {
			/*if (!client.isTransferDone()) {
				byte[] bytes = new byte[2];
				try {
					client.exchange(bytes);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			try{
				
				//System.out.println("Client update");
				if(!game.techPaused())
				game.updateLocalInput(game.updateLocalPlyerInput());
				
				Array<byte[]> remoteCommands = client.exchange(game.getCommandsForPlayer(0));
				
				if (remoteCommands.size > 0) {
					if (remoteCommands.get(0).length > 100 || game.techPaused() || game.getStepLatency() > 10)
					System.out.println("CLIENT GOT PACKAGE LENGTH " + remoteCommands.get(0).length + " stepLatency " + game.getStepLatency() + " pause " + game.techPaused());
				}
				
				
				try{
					game.updateRemoteInput(remoteCommands);
					game.update(delta);
					while (game.updateRemoteInput()){
						game.update(delta);
					}
				}catch(Exception e){
					
				}
				
				
				
				
				game.render(delta);
				//System.out.println("CLIENT STEPS LATENCY: " + game.getStepLatency());	
					//System.out.println("CLIENT SUCCESS INPUT UPDATE");
				
				
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
