package deaddream.units.utilities.input.commands;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

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
	
	@Override
	public String toJson() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		return json.toJson(this);
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
}
