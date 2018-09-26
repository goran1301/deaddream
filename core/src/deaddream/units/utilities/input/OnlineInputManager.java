package deaddream.units.utilities.input;

import com.badlogic.gdx.utils.Array;

import deaddream.players.Player;
import deaddream.units.utilities.input.commandfactory.CommandFactoryInterface;
import deaddream.units.utilities.input.commandfactory.GroupSelectionCommandFactory;
import deaddream.units.utilities.input.commandfactory.MoveCommandFactory;
import deaddream.units.utilities.input.commands.BaseCommandInterface;

public class OnlineInputManager implements CommanderInterface<String> {
	
	private Array<CommandFactoryInterface<?>> factories;
	
	private BaseCommandInterface command;
	
	public OnlineInputManager(Array<Player> players) {
		factories = new Array<CommandFactoryInterface<?>>();
		factories.add(new MoveCommandFactory(players));
		factories.add(new GroupSelectionCommandFactory(players));
	}

	@Override
	public void update(String inputData) {
		command = null;
		for (CommandFactoryInterface<?> factory : factories) {
			command = factory.constructFromJSON(inputData);
			if (command != null) {
				break;
			}
		}
	}

	@Override
	public BaseCommandInterface getCommand() {
		return command;
	}
}
