package deaddream.units.utilities.input.commandfactory;

import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.Array;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.GroupSelectionCommand;

public class GroupSelectionCommandFactory  implements CommandFactoryInterface <GroupSelectionCommand>{

	private Array<Player> players;
	
	public GroupSelectionCommandFactory(Array<Player> players) {
		this.players = players;
	}
	
	
	
	private Player getPlayerById(int id) {
		for (Player player : players) {
			if (player.getId() == id) {
				return player;
			}
		}
		return null;
	}


	@Override
	public GroupSelectionCommand constructFromBytes(byte[] bytes) {
		//System.out.println("TRY TO MAKE A GROUP SELECTION COMMAND");
		byte[] codeBytes = {bytes[4], bytes[5], bytes[6], bytes[7]};
		byte[] playerIdBytes = {bytes[12], bytes[13], bytes[14], bytes[15]};
		int playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		int code = ByteBuffer.wrap(codeBytes).getInt();
		Player player = getPlayerById(playerId);
		//System.out.println("GROUP SELECTION COMMAND PARSING HAS DONE. PLAYERID " + playerId + " CODE " + code);
		if (player == null || code != 1) {
			return null;
		}
		return new GroupSelectionCommand(bytes, player);
	}

}
