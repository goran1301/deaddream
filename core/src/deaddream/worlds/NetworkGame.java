package deaddream.worlds;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dd.DeadDream;

import deaddream.players.Player;

public class NetworkGame extends Game {

	public NetworkGame(DeadDream utilities, Array<Player> players, Player currentPlayer, TiledMap map,
			OrthogonalTiledMapRenderer tmr) {
		super(utilities, players, currentPlayer, map, tmr);
		// TODO Auto-generated constructor stub
	}

}
