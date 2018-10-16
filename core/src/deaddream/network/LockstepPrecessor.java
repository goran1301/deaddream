package deaddream.network;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.utils.Array;

import deaddream.units.utilities.input.OnlineInputManager;
import deaddream.units.utilities.input.commands.BaseCommandInterface;

public class LockstepPrecessor {
	private LocalInputStorage localInputStorage;
	private OnlineInputManager remoteInput;
	private int frameId = 0;
	private int commandLimit = 8;
	//private boolean techPause = true;
	
	public LockstepPrecessor(OnlineInputManager remoteInput) {
		this.remoteInput = remoteInput;
		localInputStorage = new LocalInputStorage();
	}
	
	public void updateRemote(Array<byte[]> remoteCommandsBytes) {
		
		/*if (techPause && remoteCommandsBytes.size > 0) {
			techPause = false;
		}*/
		
		
		
		for (byte[] bytes : remoteCommandsBytes) {
			remoteInput.update(bytes);
		}
	}
	
	public void updateLocal(BaseCommandInterface command) {
		if (command != null) {
			localInputStorage.insert(command);
		}
	}
	
	
	
	public BaseCommandInterface[] getStepCommands() {
		
		BaseCommandInterface localCommand = localInputStorage.getCommandForFrame(frameId);
		if (localCommand == null) return null;
		BaseCommandInterface[] commands  = remoteInput.getCommandsFor(localCommand, true);
		
		if (commands != null) {
			frameId++;
		}
		return commands;
	}
	
	public int getFrameId() {
		return frameId;
	}
	
	
	public byte[] getCommandsForPlayer(int playerId) {
		int lastDeliveredFrameNumber = remoteInput.getLastDeliveredFrameNumber(playerId);
		int lastReceivedFrameNumber = remoteInput.getLastReceivedFrameNumber(playerId);
		byte[] lastReceivedFrameNumberBytes = ByteBuffer.allocate(4).putInt(lastReceivedFrameNumber).array();

		Array<Byte> commandBytes = new Array<Byte>();
		commandBytes.add(lastReceivedFrameNumberBytes[0]);
		commandBytes.add(lastReceivedFrameNumberBytes[1]);
		commandBytes.add(lastReceivedFrameNumberBytes[2]);
		commandBytes.add(lastReceivedFrameNumberBytes[3]);
		
		Array<BaseCommandInterface> commands = localInputStorage.getCommandsAfter(lastDeliveredFrameNumber);
		for (BaseCommandInterface command : commands) {
			byte[] bytes = command.getBytes();
			for (byte b : bytes) {
				commandBytes.add(b);
			}
		}
		byte[] commcandBytesReal = new byte[commandBytes.size];
		for (int i = 0; i < commandBytes.size; i++) {
			commcandBytesReal[i] = commandBytes.get(i);
		}
		
		return commcandBytesReal;
	}
	
	public int getStepLatency() {
		return localInputStorage.getLastLocalFrame() - remoteInput.getBiggestFrameId();
	}
	
	public int getLatency() {
		return localInputStorage.getLastLocalFrame() - frameId;
	}
	
	public boolean isTechPaused() {
		return getStepLatency() > 10 || getLatency() > 10;
	}
}
