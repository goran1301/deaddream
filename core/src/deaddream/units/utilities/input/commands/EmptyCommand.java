package deaddream.units.utilities.input.commands;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

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
	
	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String toJson() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		return json.toJson(this);
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

}
