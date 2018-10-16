package deaddream.units.utilities.input;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import deaddream.players.LocalPlayer;
import deaddream.players.Player;
import deaddream.units.utilities.input.commandfactory.CommandFactoryInterface;
import deaddream.units.utilities.input.commandfactory.EmptyCommandFactory;
import deaddream.units.utilities.input.commandfactory.GroupSelectionCommandFactory;
import deaddream.units.utilities.input.commandfactory.MoveCommandFactory;
import deaddream.units.utilities.input.commands.BaseCommandInterface;

public class OnlineInputManager {
	
	private Array<CommandFactoryInterface<?>> factories;
		
	private ArrayMap<Integer, Array<BaseCommandInterface>> remoteCommands;
	
	private ArrayMap<Integer, Integer> receivedFrames;
	
	private int biggestFrameId = 0;
	
	
	public OnlineInputManager(Array<Player> players, boolean sgf) {
		remoteCommands = new ArrayMap<Integer, Array<BaseCommandInterface>>();
		for (Player player : players) {
			if (player instanceof LocalPlayer) {
				continue;
			}
			remoteCommands.put(player.getId(), new Array<BaseCommandInterface>());
		}
		
		receivedFrames = new ArrayMap<Integer, Integer>();
		for (Player player : players) {
			if (player instanceof LocalPlayer) {
				continue;
			}
			receivedFrames.put(player.getId(), -1);
		}
		factories = new Array<CommandFactoryInterface<?>>();
		factories.add(new EmptyCommandFactory(players));
		factories.add(new MoveCommandFactory(players));
		factories.add(new GroupSelectionCommandFactory(players));
	}
	

	
	public void update(byte[] inputData) {
		byte[] receivedFrameNumberBytes = {inputData[0], inputData[1], inputData[2], inputData[3]};
		int lastreceivedFrameNumber = ByteBuffer.wrap(receivedFrameNumberBytes).getInt();
		inputData = Arrays.copyOfRange(inputData, 4, inputData.length);
		while (inputData.length != 0) {
		    BaseCommandInterface command = null;
		    for (CommandFactoryInterface<?> factory : factories) {
			    command = factory.constructFromBytes(inputData);
			    
			     
			    if (command != null) {
			    	
			    	insert(command, lastreceivedFrameNumber);
			    	inputData = Arrays.copyOfRange(inputData, command.getSize(), inputData.length);
				    break;
			    }
			    
		    }
		    
		}
	}
	
	public void removeOldCommands(int curentFrame) {
		for (Array<BaseCommandInterface> commands : remoteCommands.values()) {
			for (BaseCommandInterface command : commands) {
				if (command.getFrameId() < curentFrame) {
					commands.removeValue(command, true);
				}
			}
		}
	}
	
	public void clear() {
		for (Array<BaseCommandInterface> commands : remoteCommands.values()) {
			commands.clear();
		}
	}
	
	public BaseCommandInterface[] getCommandsFor(BaseCommandInterface localCommnad, boolean clearOld) {
		if (clearOld) {
			removeOldCommands(localCommnad.getFrameId());
		}
		BaseCommandInterface[] commands = new BaseCommandInterface[remoteCommands.size+1];
		commands[localCommnad.getPlayer().getId()] = localCommnad;
		for (int playerId : remoteCommands.keys()) {
			BaseCommandInterface command = getUserCommandForFrame(playerId, localCommnad.getFrameId());
			if (command == null) {
				return null;
			}
			commands[playerId] = command;
		}
		return commands;
	}
	
	private BaseCommandInterface getUserCommandForFrame(int playerId, int frameId) {
		Array<BaseCommandInterface> userCommands = remoteCommands.get(playerId);
		for (BaseCommandInterface command : userCommands) {
			if (command.getFrameId() == frameId) {
				return command;
			}
		}
		return null;
	}
	
	public int getLastDeliveredFrameNumber(int playerId) {
		return receivedFrames.get(playerId);
	}
	
	public int getLastReceivedFrameNumber(int playerId) {
		Array<BaseCommandInterface> commands = remoteCommands.get(playerId);
		int frameId = -1;
		for (BaseCommandInterface command : commands) {
			if (command.getFrameId() > frameId) {
				frameId = command.getFrameId();
			}
		}
		return frameId;
	}
	
	public void insert(BaseCommandInterface command, int lastreceivedFrameNumber) {
		receivedFrames.put(command.getPlayer().getId(), lastreceivedFrameNumber);
    	
    	BaseCommandInterface duplicate = getUserCommandForFrame(command.getPlayer().getId(), command.getFrameId());
    	if (duplicate == null) {
    		if(command.getFrameId() > biggestFrameId) {
    			biggestFrameId = command.getFrameId();
    		}
    		remoteCommands.get(command.getPlayer().getId()).add(command);
    	}
	}
	
	public int getBiggestFrameId() {
		return biggestFrameId;
	}
}
