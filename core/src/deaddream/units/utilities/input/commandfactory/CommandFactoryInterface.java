package deaddream.units.utilities.input.commandfactory;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

public interface CommandFactoryInterface <C extends BaseCommandInterface> {
	public C constructFromBytes(byte[] bytes);
}
