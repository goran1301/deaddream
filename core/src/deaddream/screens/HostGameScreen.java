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
import deaddream.network.UDPServerTransmission;
import deaddream.players.Colors;
import deaddream.players.LocalPlayer;
import deaddream.players.OnlinePlayer;
import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.worlds.NetworkGame;

public class HostGameScreen implements Screen {
	
    private NetworkGame game;
	
	private DeadDream gameUtilities;
	
	private UDPServerTransmission server;
	
	private boolean doUpdate = false;
	
	public HostGameScreen(DeadDream utils) {
		this.gameUtilities = utils;
	}
	
	public void setGame(NetworkGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		LocalPlayer currentPlayer = new LocalPlayer(0, Player.inGameStatus, Colors.ORANGE); 
		OnlinePlayer onlinePlayer = new OnlinePlayer(1, Player.inGameStatus, Colors.BLUE);
		Array<Player> players = new Array<Player>();
		players.add(currentPlayer);
		players.add(onlinePlayer);
		TiledMap map = new TmxMapLoader().load("maps/test2.tmx");
		OrthogonalTiledMapRenderer tmr = new OrthogonalTiledMapRenderer(map);
		game = new NetworkGame(gameUtilities, players, currentPlayer, map, tmr, true);
		
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
			server = new UDPServerTransmission();
			server.startReceive();
			//server.receiveTestData(null);
		} catch (Exception e) {
			System.out.println("no server: " + e.getMessage());
		}
		
		//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		//System.out.println("CLIENT UPDATE GAME LOGIC");
		server.transferCheck();
		if (server.isTransferDone()){
			 if (server != null) {
				 
				 
				    try{
				    	if (doUpdate) {
				    		if(!game.techPaused())
							    game.updateLocalInput(game.updateLocalPlyerInput());
							   
							    Array<byte[]> remoteCommands = server.exchange(game.getCommandsForPlayer(1));
							    
							    
							    if (remoteCommands.size > 0) {
									if (remoteCommands.get(0).length > 100 || game.techPaused() || game.getStepLatency() > 10)
									System.out.println("HOST GOT PACKAGE LENGTH " + remoteCommands.get(0).length + " stepLatency " + game.getStepLatency() + " pause " + game.techPaused());
								}
							    
							    
							   try {
								    game.updateRemoteInput(remoteCommands);
								    game.update(delta);
								    while (game.updateRemoteInput()) {
								    	game.update(delta);
				     			    }
							   }catch(Exception e) {
								   
							   }
							   
						 }
				    	
				    	doUpdate = !doUpdate;
					    
					    game.render(delta);
					    
					    //System.out.println("HOST STEPS LATENCY: " + game.getStepLatency());
					
				    } catch (Exception e) {
				    	 e.printStackTrace();
				    }
			    }
		}
		//game.clearCommands();
	    
		//successCommandExchange = false;
	
	    
	    //System.out.println("CLIENT currentLocalCommand frame " + String.valueOf(currentLocalCommand.getFrameId()));
	    //game.render(delta);
	   
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
