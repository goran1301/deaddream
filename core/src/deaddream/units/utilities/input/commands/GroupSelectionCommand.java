package deaddream.units.utilities.input.commands;


import java.nio.ByteBuffer;

import deaddream.players.Player;

public class GroupSelectionCommand implements BaseCommandInterface{

	private int id;
	
	private transient Player player;
	
	private float x1;
	
	private float x2;
	
	private float y1;
	
	private float y2;
	
	private int code = 1;
	
	private int playerId;
	
	private float delta;
	
	public GroupSelectionCommand(int id, float delta, Player player, float x1, float x2, float y1, float y2) {
		this.id = id;
		this.delta = delta;
		this.player = player;
		playerId = player.getId();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public GroupSelectionCommand(byte[] bytes, Player player) {
		this.player = player;
		playerId = player.getId();
		byte[] idBytes = {bytes[0], bytes[1], bytes[2], bytes[3]};
		//byte[] codeBytes = {bytes[4], bytes[5], bytes[6], bytes[7]};
		byte[] x1Bytes = {bytes[16], bytes[17], bytes[18], bytes[19]};
		byte[] x2Bytes = {bytes[20], bytes[21], bytes[22], bytes[23]};
		byte[] y1Bytes = {bytes[24], bytes[25], bytes[26], bytes[27]};
		byte[] y2Bytes = {bytes[28], bytes[29], bytes[30], bytes[31]};
		byte[] deltaBytes = {bytes[8], bytes[9], bytes[10], bytes[11]};
		//byte[] playerIdBytes = {bytes[12], bytes[13], bytes[14], bytes[15]};
		
		id = ByteBuffer.wrap(idBytes).getInt();
		//playerId = ByteBuffer.wrap(playerIdBytes).getInt();
		delta = ByteBuffer.wrap(deltaBytes).getFloat();
		x1 = ByteBuffer.wrap(x1Bytes).getFloat();
		x2 = ByteBuffer.wrap(x2Bytes).getFloat();
		y1 = ByteBuffer.wrap(y1Bytes).getFloat();
		y2 = ByteBuffer.wrap(y2Bytes).getFloat();
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getCode() {
		return code;
	}

	public float getX1() {
		return x1;
	}
	
	public float getY1(){
		return y1;
	}
	
	public float getX2() {
		return x2;
	}
	
	public float getY2(){
		return y2;
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
		byte[] x1Bytes = ByteBuffer.allocate(4).putFloat(x1).array();
		byte[] x2Bytes = ByteBuffer.allocate(4).putFloat(x2).array();
		byte[] y1Bytes = ByteBuffer.allocate(4).putFloat(y1).array();
		byte[] y2Bytes = ByteBuffer.allocate(4).putFloat(y2).array();
		byte [] bytes = {
			idBytes[0], idBytes[1], idBytes[2], idBytes[3],
			codeBytes[0], codeBytes[1], codeBytes[2], codeBytes[3],
			deltaBytes[0], deltaBytes[1], deltaBytes[2], deltaBytes[3],
			playerIdBytes[0], playerIdBytes[1], playerIdBytes[2], playerIdBytes[3],
			x1Bytes[0], x1Bytes[1], x1Bytes[2], x1Bytes[3],
			x2Bytes[0], x2Bytes[1], x2Bytes[2], x2Bytes[3],
			y1Bytes[0], y1Bytes[1], y1Bytes[2], y1Bytes[3],
			y2Bytes[0], y2Bytes[1], y2Bytes[2], y2Bytes[3]
		};
		return bytes;
	}
	
}
