package deaddream.units.utilities.input.commands;

import java.nio.ByteBuffer;

import deaddream.players.Player;

public class EmptyCommand implements BaseCommandInterface {

	private int id;
	private transient Player player;
	private int code = 3;
	private int playerId;
	private float delta;
	
	public EmptyCommand(int id, float delta, Player player) {
		this.id = id;
		this.delta = delta;
		this.player = player;
		playerId = player.getId();
	}
	
	public EmptyCommand(byte[] bytes, Player player) {
		byte[] idBytes = {bytes[0], bytes[1], bytes[2], bytes[3]};
		//byte[] playerIdBytes = {bytes[8], bytes[9], bytes[10], bytes[11]};
		byte[] deltaBytes = {bytes[12], bytes[13], bytes[14], bytes[15]};
		id = ByteBuffer.wrap(idBytes).getInt();
		playerId = player.getId();
		this.player = player;
		//playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		delta = ByteBuffer.wrap(deltaBytes).getFloat();
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public int getFrameId() {
		return id;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	@Override
	public float getDelta() {
		return delta;
	}

	@Override
	public byte[] getBytes() {
		byte[] idBytes = ByteBuffer.allocate(4).putInt(id).array();
		byte[] codeBytes = ByteBuffer.allocate(4).putInt(code).array();
		byte[] playerIdByte = ByteBuffer.allocate(4).putInt(playerId).array();
		byte[] deltaBytes = ByteBuffer.allocate(4).putFloat(delta).array();
		byte[] bytes = {
				idBytes[0], 
				idBytes[1], 
				idBytes[2],
				idBytes[3],
				codeBytes[0],
				codeBytes[1],
				codeBytes[2],
				codeBytes[3],
				playerIdByte[0],
				playerIdByte[1],
				playerIdByte[2],
				playerIdByte[3],
				deltaBytes[0],
				deltaBytes[1],
				deltaBytes[2],
				deltaBytes[3]
		};
		return bytes;
	}

}
