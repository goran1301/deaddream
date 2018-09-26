package deaddream.units.utilities.input.commands;

import com.badlogic.gdx.utils.Json;

import deaddream.players.Player;

public class SingleSelectionCommand implements BaseCommandInterface {
    
	private Player player;
	
	private int index;
	
	public SingleSelectionCommand(Player player, int index) {
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

}
