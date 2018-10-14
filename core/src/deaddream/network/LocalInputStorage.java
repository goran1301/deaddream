package deaddream.network;

import com.badlogic.gdx.utils.Array;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

public class LocalInputStorage {
	
	private Array<BaseCommandInterface> commands = new Array<BaseCommandInterface>();
	
	public void insert(BaseCommandInterface newCommand) {
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() == newCommand.getFrameId()) {
				return;
			}
		}
		commands.add(newCommand);
	}
	
	public void clear() {
		commands.clear();
	}
	
	public BaseCommandInterface getCommandForFrame(int frameId) {
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() == frameId) {
				return command;
			}
		}
		return null;
	}
	
	public void removeOld(int frameId) {
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() == frameId) {
				commands.removeValue(command, true);
			}
		}
	}
	
	public Array<BaseCommandInterface> getCommandsAfter(int frameId) {
		Array<BaseCommandInterface> needleCommands = new Array<BaseCommandInterface>();
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() > frameId) {
				needleCommands.add(command);
			}
		}
		return needleCommands;
	}
}
