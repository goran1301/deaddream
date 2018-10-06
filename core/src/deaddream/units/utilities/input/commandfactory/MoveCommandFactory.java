package deaddream.units.utilities.input.commandfactory;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.MoveCommand;

public class MoveCommandFactory implements CommandFactoryInterface<MoveCommand>{

private Array<Player> players;
	
	private JsonReader reader;
	
	public MoveCommandFactory(Array<Player> players) {
		this.players = players;
		reader = new JsonReader();
	}
	
	@Override
	public MoveCommand constructFromJSON(String json) {
		JsonValue parsedJson = reader.parse(json);
		try {
			int code = parsedJson.getInt("code");
			Player player = getPlayerById(parsedJson.getInt("playerId"));
			if (player == null || code != 0) {
				return null;
			}
			
			int id = parsedJson.getInt("id");
			float x = parsedJson.getFloat("x");
			float y = parsedJson.getFloat("y");
			
			return new MoveCommand(id, player, new Vector3(x, y, 0f));
			
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String converToJson(BaseCommandInterface command) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Player getPlayerById(int id) {
		for (Player player : players) {
			if (player.getId() == id) {
				return player;
			}
		}
		return null;
	}

}
