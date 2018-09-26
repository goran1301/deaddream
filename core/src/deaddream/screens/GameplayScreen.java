package deaddream.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.DeadDream;

import deaddream.backgrounds.WorldBackground;
import deaddream.players.LocalPlayer;
import deaddream.players.Player;
import deaddream.worlds.Game;


public class GameplayScreen implements Screen {
	
	private Game game;
	
	private DeadDream gameUtilities;
	
	private Array<String> remoteCommands = new Array<String>();
	
	public GameplayScreen(final DeadDream gameUtilities) {
		this.gameUtilities = gameUtilities;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		LocalPlayer currentPlayer = new LocalPlayer(0, Player.inGameStatus); 
		Array<Player> players = new Array<Player>();
		players.add(currentPlayer);
		TiledMap map = new TmxMapLoader().load("maps/test2.tmx");
		OrthogonalTiledMapRenderer tmr = new OrthogonalTiledMapRenderer(map);
		game = new Game(gameUtilities, players, currentPlayer, map, tmr);
		
		WorldBackground bg = new WorldBackground(
				gameUtilities.assets.get("backgrounds/world_background/stars.png", Texture.class),
				gameUtilities.assets.get("backgrounds/world_background/middle_layer.png", Texture.class)
			);
		bg.setResolution(gameUtilities.camera.viewportWidth, gameUtilities.camera.viewportHeight);
		game.setBg(bg);
		for (int i = 0; i < 99; i++) {
			game.getUnitFactory().createProtector(game.world, 23f, 23f, game.unitGroup, currentPlayer);
		}
		//game.getUnitFactory().createUCMothership(game.world, 40f, 40f, game.unitGroup, currentPlayer);
	}

	@Override
	public void render(float delta) {
		game.update(1/60f);
		game.updateInput(remoteCommands);
		game.render(1/60f);
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
		// TODO Auto-generated method stub
		
	}

}
