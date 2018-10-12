package deaddream.units.utilities.input.commandfactory;

import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.Array;

import deaddream.players.Player;
import deaddream.units.utilities.input.commands.MoveCommand;

public class MoveCommandFactory implements CommandFactoryInterface<MoveCommand>{

private Array<Player> players;
	
	
	public MoveCommandFactory(Array<Player> players) {
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
	public MoveCommand constructFromBytes(byte[] bytes) {
		byte[] codeBytes = {bytes[4], bytes[5], bytes[6], bytes[7]};
		byte[] playerIdBytes = {bytes[12], bytes[13], bytes[14], bytes[15]};
		int playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		int code = ByteBuffer.wrap(codeBytes).getInt();
		Player player = getPlayerById(playerId);
		if (code != 0 || player == null) {
			return null;
		}
		return new MoveCommand(bytes, player);
	}

}
