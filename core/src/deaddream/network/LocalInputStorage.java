package deaddream.network;

import com.badlogic.gdx.utils.Array;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

public class LocalInputStorage {
	
	private Array<BaseCommandInterface> commands = new Array<BaseCommandInterface>();
	private int lastLocalFrame = 0;
	
	public void insert(BaseCommandInterface newCommand) {
		
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() == newCommand.getFrameId()) {
				return;
			}
		}
		if (newCommand.getFrameId() > lastLocalFrame) {
			lastLocalFrame = newCommand.getFrameId();
		}
		commands.add(newCommand);
	}
	
	public int getLastLocalFrame() {
		return lastLocalFrame;
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
