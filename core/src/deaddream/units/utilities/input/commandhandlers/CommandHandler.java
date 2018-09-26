package deaddream.units.utilities.input.commandhandlers;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

/**
 * 
 * @author goran
 *
 * @param <C> command class
 */
abstract public class CommandHandler <C extends BaseCommandInterface> {
	
	/**
	 * Checks is command class a type of C
	 * 
	 * @param command
	 * @return
	 */
	abstract public boolean checkCommandType(BaseCommandInterface command);
	
	/**
	 * execution of command
	 * 
	 * @param command
	 */
	abstract protected void handleProcess(C command);
	
	/**
	 * safe execution with checking command type
	 * 
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean handle(BaseCommandInterface command) {
		if (!checkCommandType(command)) {
			return false;
		}
		//System.out.println("handle command");
		handleProcess((C)command);
		return true;
	}
}
