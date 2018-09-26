package deaddream.units.utilities.input;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

public interface CommanderInterface <T> {
	public void update(T inputData);
	public BaseCommandInterface getCommand();
}
