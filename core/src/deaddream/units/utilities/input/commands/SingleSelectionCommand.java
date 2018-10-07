package deaddream.units.utilities.input.commands;

import com.badlogic.gdx.utils.Json;

import deaddream.players.Player;

public class SingleSelectionCommand implements BaseCommandInterface {
    
	private int id;
	
	private Player player;
	
	private int index;
	
	private float delta;
	
	public SingleSelectionCommand(int id, float delta, Player player, int index) {
		this.id = id;
		this.delta = delta;
		this.player = player;
		this.index = index;
	}
	
	
	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getCode() {
		return 2;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toJson() {
		Json json = new Json();
		return json.toJson(this);
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
