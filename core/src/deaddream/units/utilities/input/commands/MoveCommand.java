package deaddream.units.utilities.input.commands;

import java.nio.ByteBuffer;

import com.badlogic.gdx.math.Vector3;

import deaddream.players.Player;

public class MoveCommand implements BaseCommandInterface {
	private int id;
	private float x;
	private float y;
	private transient Player player;
	private transient Vector3 cursorPosition; 
	private int code = 0;
	private int playerId;
	private float delta;
	
	public MoveCommand(int id, float delta, Player player, Vector3 cursorPosition) {
		this.id = id;
		this.delta = delta;
		this.player = player;
		playerId = player.getId();
		this.x = cursorPosition.x;
		this.y = cursorPosition.y;
		this.cursorPosition = cursorPosition;
	}
	
	public MoveCommand(byte[] bytes, Player player) {
		this.player = player;
		playerId = player.getId();
		byte[] idBytes = {bytes[0], bytes[1], bytes[2], bytes[3]};
		//byte[] codeBytes = {bytes[4], bytes[5], bytes[6], bytes[7]};
		byte[] deltaBytes = {bytes[8], bytes[9], bytes[10], bytes[11]};
		//byte[] playerIdBytes = {bytes[12], bytes[13], bytes[14], bytes[15]};
		byte[] xBytes = {bytes[16], bytes[17], bytes[18], bytes[19]};
		byte[] yBytes = {bytes[20], bytes[21], bytes[22], bytes[23]};
		
		id = ByteBuffer.wrap(idBytes).getInt();
		//playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		delta = ByteBuffer.wrap(deltaBytes).getFloat();
		x = ByteBuffer.wrap(xBytes).getFloat();
		y = ByteBuffer.wrap(yBytes).getFloat();
		
		cursorPosition = new Vector3(x, y, 0f);
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	@Override
	public int getCode() {
		return code;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public Vector3 getCursorPosition() {
		return cursorPosition;
	}
	
	
	public int getPlayerId() {
		return playerId;
	}

	@Override
	public int getFrameId() {
		return id;
	}

	@Override
	public float getDelta() {
		return delta;
	}

	@Override
	public byte[] getBytes() {
		byte[] idBytes = ByteBuffer.allocate(4).putInt(id).array();
		byte[] codeBytes = ByteBuffer.allocate(4).putInt(code).array();
		byte[] deltaBytes = ByteBuffer.allocate(4).putFloat(delta).array();
		byte[] playerIdBytes = ByteBuffer.allocate(4).putInt(playerId).array();
		byte[] xBytes = ByteBuffer.allocate(4).putFloat(x).array();
		byte[] yBytes = ByteBuffer.allocate(4).putFloat(y).array();
		byte [] bytes = {
			idBytes[0], idBytes[1], idBytes[2], idBytes[3],
			codeBytes[0], codeBytes[1], codeBytes[2], codeBytes[3],
			deltaBytes[0], deltaBytes[1], deltaBytes[2], deltaBytes[3],
			playerIdBytes[0], playerIdBytes[1], playerIdBytes[2], playerIdBytes[3],
			xBytes[0], xBytes[1], xBytes[2], xBytes[3],
			yBytes[0], yBytes[1], yBytes[2], yBytes[3],
		};
		return bytes;
	}
}
