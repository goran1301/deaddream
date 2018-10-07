package deaddream.units.utilities.input.commandfactory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.BaseCommandInterface;
import deaddream.units.utilities.input.commands.GroupSelectionCommand;

public class GroupSelectionCommandFactory  implements CommandFactoryInterface <GroupSelectionCommand>{

	private Array<Player> players;
	
	private JsonReader reader;
	
	public GroupSelectionCommandFactory(Array<Player> players) {
		this.players = players;
		reader = new JsonReader();
	}
	
	@Override
	public GroupSelectionCommand constructFromJSON(String json) {
		JsonValue parsedJson = reader.parse(json);
		try {
			int code = parsedJson.getInt("code");
			Player player = getPlayerById(parsedJson.getInt("playerId"));
			if (player == null || code != 1) {
				return null;
			}
			
			int id = parsedJson.getInt("id");
			float delta = parsedJson.getFloat("delta");
			float x1 = parsedJson.getFloat("x1");
			float x2 = parsedJson.getFloat("x2");
			float y1 = parsedJson.getFloat("y1");
			float y2 = parsedJson.getFloat("y2");
			
			return new GroupSelectionCommand(id, delta, player, x1, x2, y1, y2);
			
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String converToJson(BaseCommandInterface command) {
		// TODO think do we need it
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
