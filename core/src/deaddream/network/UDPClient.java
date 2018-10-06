package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

/**
 * A network UDP Client tool
 * 
 * @author goran
 *
 */
public class UDPClient {
	
	DatagramSocket socket;
	InetAddress adress;
	int port;
	private Array<String> commands;
	private JsonReader reader;	
	Array<BaseCommandInterface> localCommandsHistory;
	private int remoteFrameId;
	
	/**
	 * Construcor
	 * @throws Exception
	 */
	public UDPClient() throws Exception {
		commands = new Array<String>();
		socket = new DatagramSocket();
		adress = InetAddress.getByName("127.0.0.1");
		//adress = InetAddress.getLocalHost();
		port = 9999;
		reader = new JsonReader();
		localCommandsHistory = new Array<BaseCommandInterface>();
		remoteFrameId = -1;
	}
	
	/**
	 * Test dataTransfer
	 * @throws Exception
	 */
	public Array<String> makeTestDataTransfer(BaseCommandInterface command) throws Exception  {
		addHistoryCommand(command);
		String commandString = getResponceCommand().toJson();
		commands.clear();
		//System.out.println("client send 222222");
		byte[] formatedData = commandString.getBytes();
		System.out.println(String.valueOf(formatedData.length));
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		socket.send(packet);
		
		byte[] receivedData = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		socket.setSoTimeout(300);
		
		socket.receive(receivedPacket);
		
		String jsonCommand = new String(receivedPacket.getData());
		jsonCommand = jsonCommand.trim();
		System.out.println("Client received a command " + jsonCommand);
		updateRemoteFrameId(jsonCommand);
		
		if (remoteFrameId == command.getFrameId()) {
			commands.add(jsonCommand);
		}
		return commands;
	}
	
    private void addHistoryCommand(BaseCommandInterface command) {
		
		for (BaseCommandInterface historyCommand : localCommandsHistory) {
			if (historyCommand == command) {
				return;
			}
		}
		localCommandsHistory.add(command);
		return;
	}
	
	private BaseCommandInterface getResponceCommand() {

		for (int i = localCommandsHistory.size - 1; i >= 0; i--) {
			if (localCommandsHistory.get(i).getFrameId() == remoteFrameId + 1) {
				return localCommandsHistory.get(i);
			}
		}
		return localCommandsHistory.get(localCommandsHistory.size - 1);
	}
	
	private void updateRemoteFrameId(String input){
		JsonValue parsedJson = reader.parse(input);
		try {
			remoteFrameId = parsedJson.getInt("id");
			//System.out.println("Client got a command for the " + String.valueOf(remoteFrameId));
			
		} catch (IllegalArgumentException e) {
			return;
		}
	}
	
}
