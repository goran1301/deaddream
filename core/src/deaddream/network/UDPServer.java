package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

/**
 * A UDP server tool
 * 
 * @author goran
 *
 */
public class UDPServer {
	
	DatagramSocket socket;
	InetAddress adress;
	int port;
	private Array<String> commands;
	private JsonReader reader;	
	Array<BaseCommandInterface> localCommandsHistory;
	private int remoteFrameId;
	
	public UDPServer() throws Exception {
		commands = new Array<String>();
		port = 9999;
		socket = new DatagramSocket(port);		
		reader = new JsonReader();
		localCommandsHistory = new Array<BaseCommandInterface>();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public Array<String> receiveTestData(BaseCommandInterface command) throws Exception {
		addHistoryCommand(command);
		commands.clear();
		byte[] requestDataBuffer = new byte[1024];
		DatagramPacket requestPacket = new DatagramPacket(requestDataBuffer, requestDataBuffer.length);
		socket.setSoTimeout(300);
		socket.receive(requestPacket);
		System.out.println("HOST received");
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		String gottenData = new String(requestPacket.getData());
		if (gottenData != null) {
			String jsonCommand = gottenData.trim();
			
			System.out.println("Host got a command: " + jsonCommand);
			
			
			updateRemoteFrameId(jsonCommand);
			
			if (remoteFrameId == command.getFrameId()) {
				commands.add(jsonCommand);
			}
			//data processing
			
		}
		
		String commandText = getResponceCommand().toJson();
		byte[] responseData = commandText.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), requestPacket.getPort());
		socket.send(responsePacket);
		
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
			if (localCommandsHistory.get(i).getFrameId() == remoteFrameId) {
				return localCommandsHistory.get(i);
			}
		}
		return localCommandsHistory.get(localCommandsHistory.size - 1);
	}
	
	private void updateRemoteFrameId(String input){
		JsonValue parsedJson = reader.parse(input);
		try {
			remoteFrameId = parsedJson.getInt("id");
			//System.out.println("HOST got a command for the " + String.valueOf(remoteFrameId));
			
		} catch (IllegalArgumentException e) {
			return;
		}
	}
	
}
