package deaddream.units.utilities.input.commandfactory;

import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.Array;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.EmptyCommand;

public class EmptyCommandFactory implements CommandFactoryInterface<EmptyCommand> {
	
	private Array<Player> players;
	
	public EmptyCommandFactory(Array<Player> players) {
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
	public EmptyCommand constructFromBytes(byte[] bytes) {
		byte[] codeBytes = {bytes[4], bytes[5], bytes[6], bytes[7]};
		byte[] playerIdBytes = {bytes[8], bytes[9], bytes[10], bytes[11]};
		int code = ByteBuffer.wrap(codeBytes).getInt();
		int playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		Player player = getPlayerById(playerId);
		if (code != 3 || player == null) {
			return null;
		}
		return new EmptyCommand(bytes, player);
	}

}
