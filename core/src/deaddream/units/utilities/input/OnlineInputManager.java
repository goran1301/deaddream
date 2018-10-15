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
			    
			    /*byte[] number0 = {inputData[0], inputData[1], inputData[2], inputData[3]};
			    byte[] number1 = {inputData[4], inputData[5], inputData[6], inputData[7]};
			    byte[] number2 = {inputData[8], inputData[9], inputData[10], inputData[11]};
			    byte[] number3 = {inputData[12], inputData[13], inputData[14], inputData[15]};
			    int test0 = ByteBuffer.wrap(number0).getInt();
			    int test1 = ByteBuffer.wrap(number1).getInt();
			    int test2 = ByteBuffer.wrap(number2).getInt();
			    float test3 = ByteBuffer.wrap(number3).getFloat();*/
			    //System.out.println("TEST COMMAND test0 " + test0 + " test1 " + test1 + " test2 " + test2 + " test3 " + test3);
			    
			    
			    if (command != null) {
			    	
			    	receivedFrames.put(command.getPlayer().getId(), lastreceivedFrameNumber);
			    	
			    	inputData = Arrays.copyOfRange(inputData, command.getSize(), inputData.length);
			    	BaseCommandInterface duplicate = getUserCommandForFrame(command.getPlayer().getId(), command.getFrameId());
			    	System.out.println("COMMAND DONE!  " + command.getCode() + " " + command.getFrameId());
			    	if (duplicate == null) {
			    		remoteCommands.get(command.getPlayer().getId()).add(command);
			    		System.out.println("SUCCESS ADD COMMAND" + command.getCode() + " " + command.getFrameId());
			    	}else {
			    		System.out.println("DUPLICATE FOR " + command.getCode() + " " + command.getFrameId());
			    	}
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
}
