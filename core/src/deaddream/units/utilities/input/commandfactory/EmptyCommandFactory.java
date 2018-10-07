package deaddream.units.utilities.input.commandfactory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.EmptyCommand;

public class EmptyCommandFactory implements CommandFactoryInterface<EmptyCommand> {
	
	private Array<Player> players;
	private JsonReader reader;	
	
	public EmptyCommandFactory(Array<Player> players) {
		this.players = players;
		reader = new JsonReader();
	}
	
	@Override
	public EmptyCommand constructFromJSON(String json) {
		JsonValue parsedJson = reader.parse(json);
		try {
			int code = parsedJson.getInt("code");
			Player player = getPlayerById(parsedJson.getInt("playerId"));
			if (player == null || code != 3) {
				return null;
			}
			
			int id = parsedJson.getInt("id");
			float delta = parsedJson.getFloat("delta");
			
			return new EmptyCommand(id, delta, player);
			
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
