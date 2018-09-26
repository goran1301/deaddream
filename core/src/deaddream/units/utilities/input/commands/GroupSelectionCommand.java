package deaddream.units.utilities.input.commands;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import deaddream.players.Player;

public class GroupSelectionCommand implements BaseCommandInterface{

	private transient Player player;
	
	private float x1;
	
	private float x2;
	
	private float y1;
	
	private float y2;
	
	private int code = 1;
	
	private int playerId;
	
	public GroupSelectionCommand(Player player, float x1, float x2, float y1, float y2) {
		this.player = player;
		playerId = player.getId();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
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
	public String toJson() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		return json.toJson(this);
	}
	
}
