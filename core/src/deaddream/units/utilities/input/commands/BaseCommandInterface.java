package deaddream.units.utilities.input.commands;

import deaddream.players.Player;

public interface BaseCommandInterface extends CommandInterface<Player>{
	public String toJson();
}
